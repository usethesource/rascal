package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.library.experiments.Compiler.Commands.PathConfig;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class BreakPointManager {
	private List<BreakPoint> breakpoints;
	int uid;
	boolean stepMode;
	Frame nextFrame = null;
	
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
		stepMode = true;
	}
	
	void reset(){
		nextFrame = null;
		stepMode = false;
		for(BreakPoint breakpoint : breakpoints){
			breakpoint.reset();
		}
	}
	
	private boolean isBlackListed(Frame frame){
		return frame.src.getPath().equals(CommandExecutor.consoleInputPath) ||
			   frame.function.getPrintableName().endsWith("_init");
	}
	
	void setStdOut(PrintWriter stdout){
		this.stdout = stdout;
	}
	
	void setAutoList(boolean autoList){
		this.autoList = autoList;
	}
	
	void setStepMode(boolean stepMode){
		this.stepMode = stepMode;
		this.nextFrame = null;
	}
	
	void setNextMode(Frame currentFrame){
		stepMode = false;
		this.nextFrame = currentFrame;
	}
	
	boolean hasEnabledBreakPoints(){
		for(BreakPoint breakpoint : breakpoints){
			if(breakpoint.enabled){
				return true;
			}
		}
		return false;
	}
	
	void breakDirective(Frame currentFrame, String[] args) throws NumberFormatException {
		if(args.length == 1){						// break
			printBreakPoints(stdout);	
			return;
		}
		if(args.length == 2){						// break <lino>
			if(args[1].matches("[0-9]+")){
				int lino = Integer.parseInt(args[1]);
				String path = currentFrame.src.getPath();
				
				add(new LineBreakpoint(uid++, path, lino));
			} else {								// break <functionName>
				add(new FunctionEnterBreakpoint(uid++, args[1]));
			}	
		} else if(args.length == 3){				// break <function> <lino>
			add(new FunctionLineBreakpoint(uid++, args[1], Integer.parseInt(args[2])));
		}
	}
	
	void breakDirective(String[] args) throws NumberFormatException {
		if(args.length == 1){						// break
			printBreakPoints(stdout);
			return;
		}
		if(args.length == 2){						// break <functionName>
			add(new FunctionEnterBreakpoint(uid++, args[1]));
		}
		if(args.length == 3){						// break <function> <lino>
			int lino = Integer.parseInt(args[2]);
			add(new FunctionLineBreakpoint(uid++, args[1], lino));
		}
	}
	
	void returnDirective(Frame currentFrame, String[] args){
		add(new FunctionLeaveBreakpoint(uid++, currentFrame.function.getPrintableName()));
	}
	
	void clearDirective(String[] args) throws NumberFormatException {
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
	
	boolean matchOnObserve(Frame frame){
		if(isBlackListed(frame)){
			return false;
		}
		if(stepMode){
			if(autoList){
				listingDirective(frame, defaultListingDelta);
			}
			return true;
		}
		if(nextFrame != null && frame != nextFrame){
			return false;
		}
		for(BreakPoint bp : breakpoints){
			if(bp.matchOnLeave(frame)){
				if(autoList){
					listingDirective(frame, defaultListingDelta);
				}
				return true;
			}
		}
		for(BreakPoint bp : breakpoints){
			if(bp.matchOnObserve(frame)){
				if(autoList){
					listingDirective(frame, defaultListingDelta);
				}
				return true;
			}	
		}
		return false;
	}
	
	boolean matchOnEnter(Frame frame){
		if(isBlackListed(frame)){
			return false;
		}
		if(nextFrame != null && frame != nextFrame){
			return false;
		}
		if(stepMode){
			if(autoList){
				listingDirective(frame, defaultListingDelta);
			}
			return true;
		}
		for(BreakPoint bp : breakpoints){
			if(bp.matchOnEnter(frame)){
				if(autoList){
					listingDirective(frame, defaultListingDelta);
				}
				return true;
			}	
		}
		return false;
	}
	
	boolean matchOnLeave(Frame frame){
		if(isBlackListed(frame)){
			return false;
		}
		if(nextFrame != null && frame != nextFrame){
			return false;
		}
		if(stepMode){
			if(autoList){
				listingDirective(frame, defaultListingDelta);
			}
			return true;
		}
		for(BreakPoint bp : breakpoints){
			if(bp.matchOnLeave(frame)){
				if(autoList){
					listingDirective(frame, defaultListingDelta);
				}
				return true;
			}	
		}
		return false;
	}
	
	void listingDirective(Frame currentFrame, String[] args){
		int delta = defaultListingDelta;
		if(args.length > 1){
			try {
				delta = Integer.parseInt(args[1]);
			} catch(NumberFormatException e){
				// use the default value
			}
		}
		listingDirective(currentFrame, delta);
	}
	
	private void listingDirective(Frame currentFrame, int delta){
		ISourceLocation src = currentFrame.src;
		int srcBegin = src.getBeginLine();
		int srcEnd = src.getEndLine();
		
		int fileBegin = Math.max(1, srcBegin - delta);
		int fileEnd = srcEnd + delta;
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		try {
			ISourceLocation srcFile = vf.sourceLocation(src.getScheme(), "", src.getPath());
			try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(srcFile)) {
				try (BufferedReader buf = new BufferedReader(reader)) {
					String line;
					int lino = 1;
					while ((line = buf.readLine()) != null) {
						String prefix = String.format("%4d", lino) + listingIndent;
						if(srcBegin == lino){
							String before = line.substring(0, src.getBeginColumn());
							if(srcBegin == srcEnd){
								String middle = line.substring(src.getBeginColumn(), src.getEndColumn());
								String after = line.substring(src.getEndColumn());
								stdout.println(prefix + before + BaseREPL.PRETTY_PROMPT_PREFIX + middle + BaseREPL.PRETTY_PROMPT_POSTFIX + after);
							} else {
								String after = line.substring(src.getBeginColumn());
								stdout.println(prefix + before + BaseREPL.PRETTY_PROMPT_PREFIX + after + BaseREPL.PRETTY_PROMPT_POSTFIX);
							}
						} else if(srcEnd == lino){
							String before = line.substring(0, src.getEndColumn());
							String after = line.substring(src.getEndColumn());
							stdout.println(prefix + BaseREPL.PRETTY_PROMPT_PREFIX + before + BaseREPL.PRETTY_PROMPT_POSTFIX + after);
						} else
						if(lino >= fileBegin && lino <= fileEnd){
							if(lino >= srcBegin && lino <= srcEnd){
								stdout.println(prefix + BaseREPL.PRETTY_PROMPT_PREFIX + line + BaseREPL.PRETTY_PROMPT_POSTFIX);
							} else {
								stdout.println(prefix + line);
							}
						}
						lino++;
					}
					stdout.flush();
				}
			}
			catch(Exception e){
				stdout.println("Cannot read source file");
			}
		} catch (URISyntaxException e){
			stdout.println("Cannot create URI for source file");
		}
	}
	
	

}
