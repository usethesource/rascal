package org.meta_environment.rascal.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.errors.ErrorAdapter;
import org.meta_environment.rascal.errors.SubjectAdapter;
import org.meta_environment.rascal.errors.SummaryAdapter;
import org.meta_environment.rascal.interpreter.control_exceptions.FailureControlException;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationError;
import org.meta_environment.rascal.interpreter.exceptions.RascalException;
import org.meta_environment.rascal.interpreter.exceptions.TypeError;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;


public class RascalShell {
	private final static String PROMPT = ">";
	private final static String CONTINUE_PROMPT = "?";
	private final static int MAX_CONSOLE_LINE = 100;
	
	private final Parser parser = Parser.getInstance();
	private final ASTFactory factory = new ASTFactory();
	private final ASTBuilder builder = new ASTBuilder(factory);
	private final ConsoleReader console;
	private final Evaluator evaluator;
	
	public RascalShell() throws IOException {
		console = new ConsoleReader();
		ModuleEnvironment root = new ModuleEnvironment("***shell***");
		evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err), root);
	}
	
	public RascalShell(InputStream inputStream,
			Writer out) throws IOException {
		console = new ConsoleReader(inputStream, out);
		ModuleEnvironment root = new ModuleEnvironment("***shell***");
		evaluator = new Evaluator(ValueFactory.getInstance(), factory, out, root);
	}

	public void setInputStream(InputStream in) {
		console.setInput(in);
	}
	
	public void run() throws IOException {
		CommandEvaluator commander = new CommandEvaluator(evaluator, console);
		
		StringBuffer input = new StringBuffer();
		String line;
		
		try {
			next:while (true) {
				try {
					input.delete(0, input.length());
					String prompt = PROMPT;

					do {
						line = console.readLine(prompt);
						if (line.trim().isEmpty()) {
							console.printString("cancelled\n");
							continue next;
						}
						else {
							input.append(line);
							prompt = CONTINUE_PROMPT;
						}
					} while (!completeStatement(input));

					String output = handleInput(commander, input);
					if(output.length() > MAX_CONSOLE_LINE)
						output = output.substring(0, MAX_CONSOLE_LINE) + " ...";
					console.printString(output);
					console.printNewline();
				}
				catch (FailureControlException e) {
					break;
				}
				catch (FactTypeError e) {
					console.printString("FactTypeError: " + e.getMessage() + "\n");
				    printStacktrace(console, e);
				}
				catch (TypeError e) {
					console.printString("TypeError: " + e.getMessage() + "\n");
					if (e.hasCause()) {
						console.printString("caused by: " + e.getCause().getMessage() + "\n");
					}
				}
				catch (ImplementationError e) {
					console.printString("ImplementationError: " + e.getMessage() + "\n");
					if (e.hasCause()) {
						console.printString("caused by: " + e.getCause().getMessage() + "\n");
					}
					printStacktrace(console, e);
				}
				catch (RascalException e) {
					console.printString("RascalException: " + e.getMessage() + "\n");
				}
				catch (Throwable e) {
					console.printString("Throwable: " + e.getMessage() + "\n");
					printStacktrace(console, e);
				}
			}
		}
		catch (FailureControlException e) {
			return;
		}
	}
	
	private int run(String module, String[] args) throws IOException {
		loadModule(module);
		return callMainFunction(module, args);
	}

	private int callMainFunction(String module, String[] args) throws IOException {
		String callMainStatement = module + "::main(" + mainArguments(args) + ");";
		IConstructor tree = parser.parseFromString(callMainStatement);
		Command callStat = builder.buildCommand(tree);
		IValue result = callStat.accept(new CommandEvaluator(evaluator));
		
		if (!result.getType().isIntegerType()) {
			System.err.println("Main function should return an integer");
		}
		
		return ((IInteger) result).getValue();
	}

	private String mainArguments(String[] args) {
		StringBuilder b = new StringBuilder();
		
		b.append("[");
		for (int i = 1; i < args.length; i++) {
			b.append("\"" + args[i].replaceAll("\"", "\\\"") + "\"");	
		}
		b.append("]");
		
		return b.toString();
	}

	private void loadModule(String module) throws IOException {
		String importCommand = "import " + module + ";";
		IConstructor tree = parser.parseFromString(importCommand);
		Command importDecl = builder.buildCommand(tree);
		importDecl.accept(new CommandEvaluator(evaluator));
	}
	
	private void printStacktrace(ConsoleReader console, Throwable e) throws IOException {
		console.printString("stacktrace: " + e.getMessage() + "\n");
		for (StackTraceElement elem : e.getStackTrace()) {
			console.printString("\t" + elem.getClassName() + "." + elem.getMethodName() + ":" + elem.getLineNumber() + "\n");
		}
		Throwable cause = e.getCause();
		if (cause != null) {
			console.printString("caused by:\n");
			printStacktrace(console, cause);
		}
	}

	private String handleInput(final CommandEvaluator command, StringBuffer statement) throws IOException {
		StringBuilder result = new StringBuilder();
		IConstructor tree = parser.parseFromString(statement.toString());

		if (tree.getConstructorType() == Factory.ParseTree_Summary) {
			result.append(tree + "\n");
		}
		else {
			Command stat = builder.buildCommand(tree);
			
			IValue value = command.eval(stat);
			
			if (value == null) {
				return "done.";
			}
			else {
			  return value.getType() + ": " + value.toString();
			}
		}
		
		return result.toString();
	}

	private ISourceRange getErrorRange(SummaryAdapter summaryAdapter) {
		for (ErrorAdapter error : summaryAdapter) {
			for (SubjectAdapter subject : error) {
				if (subject.isLocalized()) {
					return subject.getRange();
				}
			}
		}
		
		return null;
	}
	
	private boolean completeStatement(StringBuffer statement) throws FactTypeError, IOException {
		String command = statement.toString();
		IConstructor tree = parser.parseFromString(command);

		if (tree.getConstructorType() == Factory.ParseTree_Summary) {
			ISourceRange range = getErrorRange(new SummaryAdapter(tree));
			String[] commandLines = command.split("\n");
			int lastLine = commandLines.length;
			int lastColumn = commandLines[lastLine - 1].length();
			
			if (range.getEndLine() == lastLine && lastColumn <= range.getEndColumn()) { 
				return false;
			}
			else {
				return true;
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
		else if (args.length > 0) {
			try {
				int result = new RascalShell().run(args[0], args);
				System.exit(result);
			} catch (IOException e) {
				System.err.println("unexpected error: " + e.getMessage());
				System.exit(1);
			} 
		}
	}
}
