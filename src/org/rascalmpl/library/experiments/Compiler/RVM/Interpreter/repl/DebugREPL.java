package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.CompletionResult;

import jline.Terminal;

/*
 * Shopping list of ideas for the Rascal debugger.
 * 
 * - The result of ech command is now printed as 'type : value'; making the type optional (config option)
 * - Save the result of the previous command in a fixed variable like 'it' that can be used in the next command.
 * - User-addition to blacklisted files in which we never break.
 * - break in exceptions
 * - conditional breakpoints.
 * - give stack frames an id and allow up/down to a specific frame
 * - print more info per frame.
 * - resolve clean/clear
 * 
 */

public class DebugREPL extends BaseREPL{

	private PrintWriter stdout;
	private PrintWriter stderr;
	private String currentPrompt;
	private Frame currentFrame;
	private final Frame startFrame;
	private String previousCommand;

	private final BreakPointManager breakPointManager;

	public DebugREPL(Frame frame, BreakPointManager breakPointManager, InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File file, Terminal terminal) throws IOException{
		super(stdin, stdout, prettyPrompt, allowColors, new File(file.getAbsolutePath() + "-debug"), terminal);
		this.currentFrame = frame;
		this.startFrame = frame;
		setPrompt();
		this.breakPointManager = breakPointManager;
		this.breakPointManager.setStdOut(this.stdout);
		previousCommand = null;
	}
	
	@Override
	protected void initialize(Writer stdout, Writer stderr) {
		 this.stdout = new PrintWriter(stdout);
         this.stderr = new PrintWriter(stderr);
	}

	@Override
	protected String getPrompt() {
		return currentPrompt;
	}
	
	private void setPrompt(){
		currentPrompt = "at " + currentFrame.getWhere() + ">";
	}

	@Override
	protected void handleInput(String line) throws InterruptedException {
		setPrompt();
		
		String[] words = line.split(" ");
		switch(words[0]){
		
		case "h": case "help":
			printHelp(); 
			break;
			
		case "d": case "down":
			if(currentFrame.previousCallFrame != null){
				currentFrame = currentFrame.previousCallFrame;
			} else {
				 this.stderr.println("Cannot go down");
			}
			printStack();
			break;
			
		case "u": case "up":
			if(currentFrame != startFrame){
				for(Frame f = startFrame; f != null; f = f.previousCallFrame){
					if(f.previousCallFrame == currentFrame){
						currentFrame = f;
						break;
					}
				}
			} else {
				this.stderr.println("Cannot go up");
			}
			printStack();
			break;
			
		case "l": case "listing":
			breakPointManager.listingDirective(currentFrame, words);
			break;
			
		case "w": case "where":
			printStack();
			break;
			
		case "v": case "vars":
			currentFrame.printVars(stdout);
			break;
			
		case "s": case "step":
			breakPointManager.setStepMode(true);
			stop();
			throw new InterruptedException();
			
		case "n": case "next":
			breakPointManager.setNextMode(currentFrame);
			stop();
			throw new InterruptedException();
			
		case "": 
			if(previousCommand != null){
				handleInput(previousCommand);
			}
			break;
			
		case "b": case "break":
			try {
				breakPointManager.breakDirective(currentFrame, words);
			} catch(NumberFormatException e){
				stderr.println("break requires integer arguments");
			}
			break;
			
		case "cl": case "clear":
			try {
				breakPointManager.clearDirective(words);
			} catch(NumberFormatException e){
				stderr.println("clear requires integer arguments");
			}
			break;
			
		case "r": case "return":
			breakPointManager.returnDirective(currentFrame, words);
			stop();
			throw new InterruptedException();
			
		case "c": case "cont": case "continue":
			stop();
			throw new InterruptedException();
			
		default:
			stderr.println("'" + line + "' not recognized");
			return;
		}
		if(!line.isEmpty()){
			previousCommand = line;
		}
	}
	
	private void printHelp(){
		String[] lines = {
			"h(elp)    this help text",
			"u(p)      move up to newer call frame",
			"d(own)    move down to older call frame",
			"v(ars)    show values of local variables",
			"w(here)   print stack trace",
			"n(ext)    execute until next break point",
			"s(tep)    execute but stop at the first possible occasion",
			"r(eturn)  execute until the current function’s return is encountered",
			"l(isting) print lines around current breakpoint",
			"b(reak)   manage break points:",
			"          b         list current break points",
			"          b <lino>  set breakpoint at line <lino> in current module",
			"          b <name>  set breakpoint at start of function <name>",
			"          b <name> <lino>",
			"                    set breakpoint at line <lino> in function <name>",
			"c(ontinue) continue execution until a breakpoint is encountered",
			"cl(ear) <bpno>",
			"          clear breakpoint with index <bpno>",
			"          (empty line) repeat previous command"
		};
		for(String line : lines){
			stdout.println(line);
		}
	}
	
	private void printStack(){
		for(Frame f = currentFrame; f != null; f = f.previousCallFrame) {
			stdout.println("\t" + f.toString());
		}
	}

	@Override
	protected void handleReset() throws InterruptedException {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean supportsCompletion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean printSpaceAfterFullCompletion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected CompletionResult completeFragment(String line, int cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void cancelRunningCommandRequested() {
	    stop();
	}

	@Override
	protected void terminateRequested() {
	    stop();
	}

	@Override
	protected void stackTraceRequested() {
		// TODO Auto-generated method stub
	}

}
