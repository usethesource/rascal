package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class BreakPointManager {
	private List<BreakPoint> breakpoints;
	int uid;
	boolean firstBreak;
	boolean stepMode;
	
	private PrintWriter stdout;
	
	private final String listingIndent = "\t";
	private boolean autoList = true;
	private final int defaultListingDelta = 5;
	
	BreakPointManager(PrintWriter stdout){
		this.stdout = stdout;
		breakpoints = new ArrayList<>();
		uid = 1;
		firstBreak = true;
		stepMode = true;
	}
	
	void reset(){
		for(BreakPoint breakpoint : breakpoints){
			breakpoint.reset();
		}
	}
	
	private boolean inConsoleMain(ISourceLocation src){
		return src.getPath().equals(CommandExecutor.consoleInputPath);
	}
	
	void setStdOut(PrintWriter stdout){
		this.stdout = stdout;
	}
	
	void setAutoList(boolean autoList){
		this.autoList = autoList;
	}
	
	void setStepMode(boolean stepMode){
		this.stepMode = stepMode;
	}
	
	void breakDirective(Frame currentFrame, String[] args) throws NumberFormatException {
		if(args.length == 1){
			printBreakPoints(stdout);
			return;
		}
		if(args.length == 2){
			if(args[1].matches("[0-9]+")){
				int lino = Integer.parseInt(args[1]);
				String path = currentFrame.src.getPath();
				
				add(new LineBreakpoint(uid++, path, lino));
			} else {
				add(new FunctionEntryBreakpoint(uid++, args[1]));
			}
			
		}
		/*
		"          b <name> <lino>",
		"                    set breakpoint at line <lino> in function <name>",
		*/
	}
	
	void clearDirective(Frame currentFrame, String[] args) throws NumberFormatException {
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
		//breakpoint.setId(uid++);
		breakpoints.add(breakpoint);
	}
	
	private void printBreakPoints(PrintWriter stdout){
		if(breakpoints.isEmpty()){
			stdout.println("No breakpoints");
			return;
		}
		stdout.println("Id\tEnabled\tDetails");
		for(BreakPoint breakpoint : breakpoints){
			breakpoint.println(stdout);
		}
	}
	
	boolean matchOnObserve(Frame frame){
		if(inConsoleMain(frame.src)){
			return false;
		}
		if(firstBreak || stepMode){
			firstBreak = false;
			if(autoList){
				listingDirective(frame, defaultListingDelta);
			}
			return true;
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
		if(inConsoleMain(frame.src)){
			return false;
		}
		if(firstBreak || stepMode){
			firstBreak = false;
			return true;
		}
		for(BreakPoint bp : breakpoints){
			if(bp.matchOnEnter(frame)){
				return true;
			}	
		}
		return false;
	}
	
	boolean matchOnLeave(Frame frame){
		if(inConsoleMain(frame.src)){
			return false;
		}
		if(firstBreak || stepMode){
			firstBreak = false;
			return true;
		}
		for(BreakPoint bp : breakpoints){
			if(bp.matchOnLeave(frame)){
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
