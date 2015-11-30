package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.library.experiments.Compiler.Commands.PathConfig;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CommandExecutor;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class BreakPointManager {
	
	enum DEBUG_MODE { STEP, SKIP, NEXT, BREAK, RETURN };
	
	private List<BreakPoint> breakpoints;
	int uid;
	
	DEBUG_MODE mode = DEBUG_MODE.BREAK;
	
	Frame currentFrame = null;  // next mode, only break in current function
	Frame returnFrame = null;	// only break on return from this frame

	private PrintWriter stdout;
	private final PathConfig pcfg;
	
	private final String listingIndent = "\t";
	private boolean autoList = true;
	private final int defaultListingDelta = 5;
	
	BreakPointManager(PrintWriter stdout, PathConfig pcfg){
		this.stdout = stdout;
		this.pcfg = pcfg;
		breakpoints = new ArrayList<>();
		uid = 1;
	}
	
	void reset(){
		mode =  DEBUG_MODE.BREAK;
		currentFrame = null;
		returnFrame = null;
	}
	
	private boolean isBlackListed(Frame frame){
		return frame == null || frame.src.getPath().equals(CommandExecutor.consoleInputPath) ||
			   frame.function.getPrintableName().endsWith("_init") ||
			   frame.src.getPath().equals(CommandExecutor.muLibraryPath);
	}
	
	void setStdOut(PrintWriter stdout){
		this.stdout = stdout;
	}
	
	void setAutoList(boolean autoList){
		this.autoList = autoList;
	}
	
	void setBreakMode(Frame frame){
		if(mode == DEBUG_MODE.RETURN){
			return;
		}
		mode = DEBUG_MODE.BREAK;
		this.currentFrame = null;
	}
	
	void setStepMode(Frame frame){
		mode = isBlackListed(frame) ?  DEBUG_MODE.SKIP : DEBUG_MODE.STEP;
		this.currentFrame = null;
	}
	
	void setNextMode(Frame currentFrame){
		mode = DEBUG_MODE.NEXT;
		this.currentFrame = currentFrame;
	}
	
	void setReturnMode(Frame currentFrame){
		mode = DEBUG_MODE.RETURN;
		this.returnFrame = currentFrame;
	}
	
	boolean hasEnabledBreakPoints(){
		for(BreakPoint breakpoint : breakpoints){
			if(breakpoint.enabled){
				return true;
			}
		}
		return false;
	}
	
	boolean doAutoList(Frame frame){
		if(autoList){
			listingDirective(frame, defaultListingDelta);
		}
		return true;
	}
	
	/****************************************************************/
	/*				Handle all frame events							*/
	/****************************************************************/
	
	boolean matchOnObserve(Frame frame){
		switch(mode){
		
		case STEP:
			return doAutoList(frame);
			
		case SKIP:
			return false;
		
		case NEXT:
			if(frame != currentFrame){
				return false;
			}
			return doAutoList(frame);
			
		case RETURN:
			return false;
			
		case BREAK:
			for(BreakPoint bp : breakpoints){
				if(bp.matchOnObserve(frame)){
					return doAutoList(frame);
				}	
			}
		}
		return false;
	}
	
	boolean matchOnEnter(Frame frame){
		switch(mode){
		
		case STEP:
			if(isBlackListed(frame)){
				mode = DEBUG_MODE.SKIP;
				return false;
			}
			
		case SKIP:
			if(!isBlackListed(frame)){
				mode = DEBUG_MODE.STEP;
				return doAutoList(frame);
			}
			return false;
	
		case NEXT:
			return false;
			
		case RETURN:
			return false;
			
		case BREAK:
			for(BreakPoint bp : breakpoints){
				if(bp.matchOnEnter(frame)){
					return doAutoList(frame);
				}	
			}
		}
		return false;
	}
	
	boolean matchOnLeave(Frame frame, Object rval){
		
		switch(mode){
		
		case STEP:
			if(isBlackListed(frame.previousCallFrame)){
				mode = DEBUG_MODE.SKIP;
			}
			return doAutoList(frame);
			
		case SKIP:
			if(!isBlackListed(frame.previousCallFrame)){
				mode = DEBUG_MODE.STEP;
			}
			return false;
		
		case NEXT:
			return false;
			
		case RETURN:
			if(returnFrame == frame){
				returnFrame = null;
				mode = DEBUG_MODE.BREAK;
				if(autoList){
					listingDirective(frame, defaultListingDelta);
				}
				stdout.println(BaseREPL.PRETTY_PROMPT_PREFIX  + "Function " + frame.function.getPrintableName() + " will return: " + rval +  BaseREPL.PRETTY_PROMPT_POSTFIX);
				stdout.flush();
				return true;
			}
			return false;
			
		case BREAK:
			for(BreakPoint bp : breakpoints){
				if(bp.matchOnLeave(frame)){
					return doAutoList(frame);
				}	
			}
		}
		return false;
	}
	
	/****************************************************************/
	/*				Handle all debug directives						*/
	/****************************************************************/
	
	// break directive during debug mode
	void breakDirective(Frame frame, String[] args) throws NumberFormatException {
		if(args.length == 2 && args[1].matches("[0-9]+")){	// break <lino>
			int lino = Integer.parseInt(args[1]);
			String path = frame.src.getPath();
			add(new LineBreakpoint(uid++, path, lino));
		} else {
			breakDirective(args);
		}
	}
	
	// break directive outside debug mode
	public void breakDirective(String[] args) throws NumberFormatException {
		if(args.length == 1){								// break
			printBreakPoints(stdout);
			return;
		}
		if(args.length == 2){								// break <functionName>
			add(new FunctionEnterBreakpoint(uid++, args[1]));
		}
		if(args.length == 3){								// break <moduleName> <lino>
			ISourceLocation modSrc = pcfg.getRascalResolver().resolveModule(args[1]);
			if(modSrc != null){
				add(new LineBreakpoint(uid++, modSrc.getPath(), Integer.parseInt(args[2])));
			} else {
				stdout.println("Module " + args[1] + " not found");
				stdout.flush();
			}
		}
	}
	
	void returnDirective(Frame frame, String[] args){
		setReturnMode(frame);
	}
	
	public void clearDirective(String[] args) throws NumberFormatException {
		if(args.length == 1){
			breakpoints = new ArrayList<BreakPoint>();
			return;
		}
		ArrayList<BreakPoint> newBreakpoints = new ArrayList<BreakPoint>();

		next:
			for(BreakPoint breakpoint : breakpoints){
				for(int i = 1; i < args.length; i++){
					int bpno = Integer.parseInt(args[i]);
					if(breakpoint.getId() == bpno){
						continue next;
					}
				}
				newBreakpoints.add(breakpoint);
			}
		breakpoints = newBreakpoints;
	}
	
	void listingDirective(Frame frame, String[] args){
		int delta = defaultListingDelta;
		if(args.length > 1){
			try {
				delta = Integer.parseInt(args[1]);
			} catch(NumberFormatException e){
				// use the default value
			}
		}
		listingDirective(frame, delta);
	}
	
	// private helper functions

	private void add(BreakPoint breakpoint){
		breakpoints.add(breakpoint);
	}
	
	private void printBreakPoints(PrintWriter stdout){
		if(breakpoints.isEmpty()){
			stdout.println("No breakpoints");
		} else {
			stdout.println("Id\tEnabled\tKind\tDetails");
			for(BreakPoint breakpoint : breakpoints){
				breakpoint.println(stdout);
			}
		}
		stdout.flush();
	}
	
	
	
	private void listingDirective(Frame frame, int delta){
		ISourceLocation breakpointSrc = frame.src;
		int breakpointSrcBegin = breakpointSrc.getBeginLine();
		int breakpointSrcEnd = breakpointSrc.getEndLine();
		
		int windowBegin;
		int windowEnd;
		
		ISourceLocation functionSrc = frame.function.src;
		if(functionSrc.getEndLine() - functionSrc.getBeginLine() <= 2 * delta + 1){
			windowBegin = functionSrc.getBeginLine();
			windowEnd = functionSrc.getEndLine();
		} else {
			
			windowBegin = Math.max(1, breakpointSrcBegin - delta);
			windowEnd = breakpointSrcEnd + delta;
		}
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		try {
			ISourceLocation srcFile = vf.sourceLocation(breakpointSrc.getScheme(), "", breakpointSrc.getPath());
			try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(srcFile)) {
				try (BufferedReader buf = new BufferedReader(reader)) {
					String line;
					int lino = 1;
					while ((line = buf.readLine()) != null) {
						String prefix = String.format("%4d", lino) + listingIndent;
						if(breakpointSrcBegin == lino){
							String before = line.substring(0, breakpointSrc.getBeginColumn());
							if(breakpointSrcBegin == breakpointSrcEnd){
								String middle = line.substring(breakpointSrc.getBeginColumn(), breakpointSrc.getEndColumn());
								String after = line.substring(breakpointSrc.getEndColumn());
								stdout.println(prefix + before + BaseREPL.PRETTY_PROMPT_PREFIX + middle + BaseREPL.PRETTY_PROMPT_POSTFIX + after);
							} else {
								String after = line.substring(breakpointSrc.getBeginColumn());
								stdout.println(prefix + before + BaseREPL.PRETTY_PROMPT_PREFIX + after + BaseREPL.PRETTY_PROMPT_POSTFIX);
							}
						} else if(breakpointSrcEnd == lino){
							String before = line.substring(0, breakpointSrc.getEndColumn());
							String after = line.substring(breakpointSrc.getEndColumn());
							stdout.println(prefix + BaseREPL.PRETTY_PROMPT_PREFIX + before + BaseREPL.PRETTY_PROMPT_POSTFIX + after);
						} else
						if(lino >= windowBegin && lino <= windowEnd){
							if(lino >= breakpointSrcBegin && lino <= breakpointSrcEnd){
								stdout.println(prefix + BaseREPL.PRETTY_PROMPT_PREFIX + line + BaseREPL.PRETTY_PROMPT_POSTFIX);
							} else {
								stdout.println(prefix + line);
							}
						}
						lino++;
					}
				}
			}
			catch(Exception e){
				stdout.println("Cannot create listing: " + e.getMessage());
			}
		} catch (URISyntaxException e){
			stdout.println("Cannot create URI for source file");
		}
		stdout.flush();
	}
	
	

}
