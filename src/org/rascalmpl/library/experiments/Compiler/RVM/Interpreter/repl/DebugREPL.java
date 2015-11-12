package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import jline.Terminal;

public class DebugREPL extends BaseREPL{

	private PrintWriter stdout;
	private PrintWriter stderr;
	private String currentPrompt;
	private Frame currentFrame;
	private final Frame startFrame;
	private final String listingIndent = "\t";
	private final boolean autoList = true;
	private final int listWindow = 5;
	
	private boolean firstPrompt = true;
	private final BreakPointManager breakPointManager;

	public DebugREPL(Frame frame, BreakPointManager breakPointManager, InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File file, Terminal terminal) throws IOException{
		super(stdin, stdout, prettyPrompt, allowColors, new File(file.getAbsolutePath() + "-debug"), terminal);
		this.currentFrame = frame;
		this.startFrame = frame;
		setPrompt();
		this.breakPointManager = breakPointManager;
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
//		if(firstPrompt && autoList){
//			printListing(listWindow);
//		}
//		firstPrompt = false;
		
		String[] words = line.split(" ");
		switch(words[0]){
		
		case "h": case "help":
			printHelp(); 
			break;
			
		case "w":  case "where":
			stdout.println(currentFrame.getWhere());
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
			printListing(listWindow);
			break;
			
		case "s": case "stack":
			printStack();
			break;
			
		case "v": case "vars":
			currentFrame.printVars(stdout);
			break;
			
		case "n": case "next":
			stop();
			throw new InterruptedException();
			
		case "": 
			printListing(listWindow);
			break;
			
		case "b": case "break":
			if(words.length == 1){
				breakPointManager.printBreakPoints(stdout);
			}
			break;
			
		default:
			stderr.println("'" + line + "' not recognized");
			break;
		}
	}
	
	private void printHelp(){
		String[] lines = {
			"h(elp)    this help text",
			"u(p)      move up to newer call frame",
			"d(own)    move down to older call frame",
			"v(ars)    show values of local variables",
			"w(here)   show current location",
			"n(ext)    execute until next break point",
			"s(tack)   print stack trace",
			"r(eturn)  execute until the current function’s return is encountered",
			"l(isting) (or empty line) print lines around current breakpoint",
			"b(reak)   manage break points:",
			"          b         list current break points",
			"          b <lino>  set breakpoint at line <lino> in current module",
			"          b <name>  set breakpoint at start of function <name>",
			"          b <name> <lino>",
			"                    set breakpoint at line <lino> in function <name>",
			"c(lear) <bpno>",
			"          clear breakpoint with index <bpno>"
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
	
	private void printListing(int delta){
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
								stdout.println(prefix + before + PRETTY_PROMPT_PREFIX + middle + PRETTY_PROMPT_POSTFIX + after);
							} else {
								String after = line.substring(src.getBeginColumn());
								stdout.println(prefix + before + PRETTY_PROMPT_PREFIX + after + PRETTY_PROMPT_POSTFIX);
							}
						} else if(srcEnd == lino){
							String before = line.substring(0, src.getEndColumn());
							String after = line.substring(src.getEndColumn());
							stdout.println(prefix + PRETTY_PROMPT_PREFIX + before + PRETTY_PROMPT_POSTFIX + after);
						} else
						if(lino >= fileBegin && lino <= fileEnd){
							if(lino >= srcBegin && lino <= srcEnd){
								stdout.println(prefix + PRETTY_PROMPT_PREFIX + line + PRETTY_PROMPT_POSTFIX);
							} else {
								stdout.println(prefix + line);
							}
						}
						lino++;
					}
				}
			}
			catch(Exception e){
				stderr.println("Cannot read source file");
			}
		} catch (URISyntaxException e){
			stderr.println("Cannot create URI for source file");
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
