package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CommandExecutor;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.repl.ILanguageProtocol;

import io.usethesource.vallang.IValue;

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

public class DebugREPL implements ILanguageProtocol {

	private PrintWriter stdout;
	private PrintWriter stderr;
	private String currentPrompt;
	private Frame currentFrame;
	private final Frame startFrame;
	private String previousCommand;
	private final RVMCore rvm;

	private final BreakPointManager breakPointManager;

	public DebugREPL(RVMCore rvm2, Frame frame, BreakPointManager breakPointManager) throws IOException, URISyntaxException{
		this.rvm = rvm2;
		this.currentFrame = frame;
		this.startFrame = frame;
		setPrompt();
		this.breakPointManager = breakPointManager;
		this.breakPointManager.setStdOut(this.stdout);
		previousCommand = null;
	}
	
	@Override
	public void initialize(Writer stdout, Writer stderr) {
		 this.stdout = new PrintWriter(stdout);
         this.stderr = new PrintWriter(stderr);
	}

	@Override
	public String getPrompt() {
		return currentPrompt;
	}
	
	private void setPrompt(){
		currentPrompt = "at " + currentFrame.getWhere() + ">";
	}

	@Override
	public void handleInput(String line, Map<String,String> output, Map<String,String> metadata) throws InterruptedException {
		setPrompt();
		StringWriter out = new StringWriter();
		String[] words = line.split(" ");
		switch(words[0]){
		
		case "h": case "help":
			printHelp(out); 
			break;
			
		case "d": case "down":
			if(currentFrame.previousCallFrame != null){
				currentFrame = currentFrame.previousCallFrame;
			} else {
				 this.stderr.println("Cannot go down");
			}
			printStack(out);
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
			printStack(out);
			break;
			
		case "l": case "listing":
			breakPointManager.listingDirective(currentFrame, words);
			break;
			
		case "w": case "where":
			printStack(out);
			break;
			
		case "v": case "vars":
			currentFrame.printVars(out);
			break;
			
		case "s": case "step":
			breakPointManager.setStepMode(currentFrame);
			stop();
			throw new InterruptedException();			
			
		case "n": case "next":
			breakPointManager.setNextMode(currentFrame);
			stop();
			throw new InterruptedException();
			
		case "q": case "quit":
			breakPointManager.requestQuit();
			stop();
			throw new InterruptedException("quit");
			
		case "": 
			if(previousCommand != null){
				handleInput(previousCommand, output, metadata);
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
			breakPointManager.returnDirective(currentFrame);
			stop();
			throw new InterruptedException();
			
		case "c": case "cont": case "continue":
			breakPointManager.setBreakMode(currentFrame);
			stop();
			throw new InterruptedException();
		
		case "p": case "print":
                IValue eval = EvalExpr.eval(words[1], rvm, currentFrame);
                if (eval != null) {
                    stdout.println(RascalPrimitive.$value2string(eval));
                }
                else {
                    stdout.println(words[1] + " is undefined.");
                }
			break;
		
		case "i": case "ignore":
			breakPointManager.ignoreDirective(words);
			break;
			
		case "enable":
			breakPointManager.enableDirective(words);
			break;
			
		case "disable":
			breakPointManager.disableDirective(words);
			break;
			
		case "e": case "edit":
		    breakPointManager.edit(currentFrame.src);
		    break;
			
		default:
			IValue v = EvalExpr.eval(words[0], rvm, currentFrame);
			if(v != null){
				out.write(v.toString());
			} else {
			    stderr.println("'" + line + "' not recognized (or variable has undefined value)");
			}
			return;
		}
		if(!line.isEmpty()){
			previousCommand = line;
		}
		
		output.put("text/plain", out.toString());
	}
	
	private void printHelp(StringWriter out){
		String[] lines = {
			"h(elp)           This help text",
			"u(p)             Move up to newer call frame",
			"d(own)           Move down to older call frame",
			"v(ars)           Show values of local variables",
			"w(here)          Print stack trace",
			"n(ext)           Execute until next break point",
			"s(tep)           Execute but stop at the first possible occasion",
			"r(eturn)         Execute until the current function’s return is encountered",
			"l(isting)        Print lines around current breakpoint",
			"e(dit)           Edit current module",
			"b(reak)          Manage break points:",
			"                 b          List current break points",
			"                 b <lino>   Set breakpoint at line <lino> in current module",
			"                 b <module> <lino>",
			"                            Set breakpoint at line <lino> in <module>",
			"                 b <name>   Set breakpoint at start of function <name>",
			"c(ontinue)       Continue execution until a breakpoint is encountered",
			"cl(ear) <bpnos>  Clear breakpoints <bpnos> (empty list clears all)",
			"i(gnore) <bpno> <count>",
			"                 Ignore breakpoint <bpno> for <count> times",
			"<empty line>     Repeat previous command",
			"p(rint) <expr>   Print value of <expr>",
			"<expr>           Print value of <expr>",
			"                 (use p <expr> for variables that overlap with one of the above commands)",
			"enable <bnpos>   Enable breakpoints <bpnos> (empty list enables all)",
			"disable <bpnos>  Disable breakpoints <bpnos> (empty list disables all)"
		};
		for(String line : lines){
			out.write(line + "\n");
		}
	}
	
	private void printStack(StringWriter out){
		for(Frame f = currentFrame; 
		    (f != null && !f.src.getPath().equals(CommandExecutor.consoleInputPath)); 
		    f = f.previousCallFrame) {
			out.write("\t" + f.toString() + "\n");
		}
	}

	@Override
	public void handleReset(Map<String,String> output, Map<String,String> metadata) throws InterruptedException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean supportsCompletion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean printSpaceAfterFullCompletion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CompletionResult completeFragment(String line, int cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelRunningCommandRequested() {
	    stop();
	}

	@Override
	public void terminateRequested() {
	    stop();
	}

	@Override
	public void stackTraceRequested() {
		// TODO Auto-generated method stub
	}

    @Override
    public void stop() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isStatementComplete(String command) {
        // TODO Auto-generated method stub
        return true;
    }

}
