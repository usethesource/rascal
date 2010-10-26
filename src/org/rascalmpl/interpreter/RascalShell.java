package org.rascalmpl.interpreter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Insert;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.library.IO;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class RascalShell {
	private final static String PROMPT = "rascal>";
	private final static String CONTINUE_PROMPT = ">>>>>>>";
	private final static int LINE_LIMIT = 200;
	private static final String SHELL_MODULE = "***shell***";
	
	private final ConsoleReader console;
	private final Evaluator evaluator;
	private volatile boolean running;
	
	
	// TODO: cleanup these constructors.
	public RascalShell() throws IOException {
		console = new ConsoleReader();
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(SHELL_MODULE));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap);
		running = true;
	}
	
	public RascalShell(InputStream stdin, PrintWriter stderr, PrintWriter stdout) throws IOException {
		console = new ConsoleReader(stdin, new PrintWriter(stdout));
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(SHELL_MODULE));
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap);
		Object ioInstance = evaluator.getJavaBridge().getJavaClassInstance(IO.class);
		((IO) ioInstance).setOutputStream(new WriterPrintStream(stdout));
		running = true;
	}
	
	public void run() throws IOException {
		StringBuffer input = new StringBuffer();
		String line;
		
		next:while (running) {
			try {
				input.delete(0, input.length());
				String prompt = PROMPT;

				do {
					line = console.readLine(prompt);
					
					if (line == null) {
						break next; // EOF
					}
					
					if (line.trim().length() == 0) {
						console.printString("cancelled\n");
						continue next;
					}
					
					input.append((input.length() > 0 ? "\n" : "") + line);
					prompt = CONTINUE_PROMPT;
				} while (!completeStatement(input));

				String output = handleInput(evaluator, input);
				console.printString(output);
				console.printNewline();
			}
			catch (SyntaxError e) {
				ISourceLocation loc = e.getLocation();
				console.printString("Parse error in command at line " + loc.getBeginLine() + ", column " + loc.getBeginColumn() + "\n");
			}
			catch (StaticError e) {
				console.printString("Static Error: " + e.getMessage() + "\n");
				e.printStackTrace(); // for debugging only
			}
			catch (Throw e) {
				console.printString("Uncaught Rascal Exception: " + e.getMessage() + "\n");
				String trace = e.getTrace();
				if (trace != null) {
					console.printString(trace);
				}
				else {
//					e.printStackTrace(); // for debugging only
				}
			}
			catch(Insert e){
				console.printString("Error: insert statement outside visit\n");
			}
			catch (Return e){
				console.printString("Error: return statement outside function body\n");
			}
			catch (ImplementationError e) {
				e.printStackTrace();
				console.printString("ImplementationError: " + e.getMessage() + "\n");
				printStacktrace(console, e);
			}
			catch (QuitException q) {
				break next;
			}
			catch (Throwable e) {
				console.printString("Unexpected exception (generic Throwable): " + e.getMessage() + "\n");
				printStacktrace(console, e);
			}
		}
	}
	
	public synchronized void stop(){
		running = false;
		evaluator.interrupt();
	}
	
	private void printStacktrace(ConsoleReader console, Throwable e) throws IOException {
		String message = e.getMessage();
		console.printString("stacktrace: " + (message != null ? message : "" )+ "\n");
		for (StackTraceElement elem : e.getStackTrace()) {
			console.printString("\tat " + elem.getClassName() + "." + elem.getMethodName() + "(" + elem.getFileName() + ":" + elem.getLineNumber() + ")\n");
		}
		Throwable cause = e.getCause();
		if (cause != null) {
			console.printString("caused by:\n");
			printStacktrace(console, cause);
		}
	}

	private String handleInput(final Evaluator command, StringBuffer statement){
		Result<IValue> value = evaluator.eval(statement.toString(), URI.create("prompt:///"));

		if (value.getValue() == null) {
			return "ok";
		}

		IValue v = value.getValue();
		Type type = value.getType();

		if (type.isAbstractDataType() && type.isSubtypeOf(Factory.Tree)) {
			return "`" + TreeAdapter.yield((IConstructor) v) + "`\n" + value.toString(LINE_LIMIT);
		}

		return ((v != null) ? value.toString(LINE_LIMIT) : null);
	}

	private boolean completeStatement(StringBuffer statement) throws FactTypeUseException {
		String command = statement.toString();
		
		try {
			evaluator.parseCommand(command, URI.create("prompt:///"));
		}
		catch (SyntaxError e) {
			ISourceLocation l = e.getLocation();
			
			String[] commandLines = command.split("\n");
			int lastLine = commandLines.length;
			int lastColumn = commandLines[lastLine - 1].length();
			
			if (l.getEndLine() == lastLine && lastColumn <= l.getEndColumn()) { 
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			// interactive mode
			try {
				new RascalShell().run();
				System.err.println("Que le Rascal soit avec vous!");
				System.exit(0);
			} catch (IOException e) {
				System.err.println("unexpected error: " + e.getMessage());
				System.exit(1);
			} 
		}
	}
}

/**  
 * Adapter for a Writer to behave like a PrintStream. 
 * This is needed RascalShell gets PrintWriters as argument but the
 * IO module needs a PrintStream.  
 * 
 * Bytes are converted to chars using the platform default encoding. 
 * If this encoding is not a single-byte encoding, some data may be lost. 
 */  

class WriterPrintStream extends PrintStream {  
   
    private final Writer writer;  
  
    public WriterPrintStream(Writer writer) throws FileNotFoundException { 
    	super("tmp");
        this.writer = writer;  
    }  
 
    public void write(int b) {  
        // It's tempting to use writer.write((char) b), but that may get the encoding wrong  
        // This is inefficient, but it works  
        write(new byte[] {(byte) b}, 0, 1);  
    }  
   
    public void write(byte b[], int off, int len) {  
        try {
			writer.write(new String(b, off, len));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }  
   
    public void flush() {  
        try {
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }  
   
    public void close() {  
        try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }  
}  

