package org.rascalmpl.interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.util.List;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Insert;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.RascalURIResolver;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
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
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(SHELL_MODULE, heap));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap);
		running = true;
	}
	
	public RascalShell(InputStream stdin, PrintWriter stderr, PrintWriter stdout) throws IOException {
		console = new ConsoleReader(stdin, new PrintWriter(stdout));
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(SHELL_MODULE, heap));
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap);
		running = true;
	}
	
	public RascalShell(InputStream stdin, PrintWriter stderr, PrintWriter stdout, List<ClassLoader> classLoaders, RascalURIResolver uriResolver) throws IOException {
		console = new ConsoleReader(stdin, new PrintWriter(stdout));
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(SHELL_MODULE, heap));
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap, classLoaders, uriResolver);
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
				if(loc != null) console.printString("Parse error in command from <"+loc.getBeginLine()+","+loc.getBeginColumn()+"> to <"+loc.getEndLine()+","+loc.getEndColumn()+">\n");
				else console.printString("Parse error in command\n");
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
				console.printString(evaluator.getStackTrace());
				printStacktrace(console, e);
			}
		}
	}
	
	public synchronized void stop(){
		running = false;
		evaluator.interrupt();
	}
	
	public Evaluator getEvaluator() {
		return evaluator;
	}
	
	private void printStacktrace(ConsoleReader console, Throwable e) throws IOException {
		String message = e.getMessage();
		console.printString("stacktrace: " + (message != null ? message : "" )+ "\n");
		StackTraceElement[] stackTrace = e.getStackTrace();
		if (stackTrace != null) {
			for (StackTraceElement elem : stackTrace) {
				console.printString("\tat " + elem.getClassName() + "." + elem.getMethodName() + "(" + elem.getFileName() + ":" + elem.getLineNumber() + ")\n");
			}
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
	
	public static void main(String[] args) throws IOException {
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
		if (args[0].equals("-latex")) {
			toLatex(args[1]);
		}
	}

	private static void toLatex(String fileName) throws IOException {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(SHELL_MODULE, heap));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		Evaluator evaluator = new Evaluator(vf, stderr, stdout, root, heap);
		evaluator.doImport("lang::rascal::doc::ToLatex");
		File file = new File(fileName);
		String name = file.getName();
		int pos = name.lastIndexOf('.');
		if (pos < 0) {
			System.err.println("No extension in file " + fileName);
			System.exit(1);
		}
		String ext = name.substring(pos + 1);
		
		if (ext.equals("ltx")) {
			System.err.println("Using output extension ltx, but source file has the same extension");
			System.exit(1);
		}
		final String destExt = ".ltx";
		File dest = new File(file.getParent(), name.substring(0, pos) + destExt); 
		
		System.err.println("Formatting Rascal snippets in " + file + "; outputting to " + dest + "...");
		System.err.flush();
		ISourceLocation loc = vf.sourceLocation(file.getAbsolutePath());
		IString str = (IString) evaluator.call("rascalDoc2Latex", loc);
		FileWriter writer = new FileWriter(dest);
		writer.write(str.getValue());
		writer.close();
		System.err.println("Done.");
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

