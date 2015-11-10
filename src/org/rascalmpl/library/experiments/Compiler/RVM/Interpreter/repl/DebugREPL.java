package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
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
	private final String listingIndent = "    ";
	private final boolean autoList = true;
	private final int listWindow = 5;
	
	private boolean firstPrompt = true;

	public DebugREPL(Frame frame, InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File file, Terminal terminal) throws IOException{
		super(stdin, stdout, prettyPrompt, allowColors, file, terminal);
		this.currentFrame = frame;
		this.startFrame = frame;
		setPrompt();
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
		currentPrompt = currentFrame.getWhere() + ">";
	}

	@Override
	protected void handleInput(String line) throws InterruptedException {
		setPrompt();
//		if(firstPrompt && autoList){
//			printListing(listWindow);
//		}
//		firstPrompt = false;
		
		switch(line){
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
			
		case "": 
			stop();
			throw new InterruptedException();
			
		default:
			stderr.println("'" + line + "' not recognized");
			break;
		}
	}
	
	private void printHelp(){
		String[] lines = {
			"h(elp)   this help text",
			"u(p)     move up to previous call frame",
			"d(own)   move down to next call frame",
			"v(ars)   show values of local variables",
			"w(here)  show current location",
			"s(tack)  print stack trace",
			"         (empty line) continue execution",
			"b(break) manage break points:",
			"         break        list current break points",
			"         break <lino> set breakpoint at line <lino> in current module",
			"         break <name> set breakpoint at start of function <name>",
			"         break <name> <lino>",
			"                      set breakpoint at line <lino> in function <name>",
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
						if(srcBegin == lino){
							String before = line.substring(0, src.getBeginColumn());
							if(srcBegin == srcEnd){
								String middle = line.substring(src.getBeginColumn(), src.getEndColumn());
								String after = line.substring(src.getEndColumn());
								stdout.println(listingIndent + before + PRETTY_PROMPT_PREFIX + middle + PRETTY_PROMPT_POSTFIX + after);
							} else {
								String after = line.substring(src.getBeginColumn());
								stdout.println(listingIndent + before + PRETTY_PROMPT_PREFIX + after + PRETTY_PROMPT_POSTFIX);
							}
						} else if(srcEnd == lino){
							String before = line.substring(0, src.getEndColumn());
							String after = line.substring(src.getEndColumn());
							stdout.println(listingIndent + PRETTY_PROMPT_PREFIX + before + PRETTY_PROMPT_POSTFIX + after);
						} else
						if(lino >= fileBegin && lino <= fileEnd){
							if(lino >= srcBegin && lino <= srcEnd){
								stdout.println(listingIndent + PRETTY_PROMPT_PREFIX + line + PRETTY_PROMPT_POSTFIX);
							} else {
								stdout.println(listingIndent + line);
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
		// TODO Auto-generated method stub
	}

	@Override
	protected void terminateRequested() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void stackTraceRequested() {
		// TODO Auto-generated method stub
	}

}
