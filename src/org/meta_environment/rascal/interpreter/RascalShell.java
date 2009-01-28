package org.meta_environment.rascal.interpreter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.interpreter.exceptions.FailureException;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;
import org.meta_environment.rascal.interpreter.exceptions.RascalException;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;


public class RascalShell {
	private final static String PROMPT = ">";
	private final static String CONTINUE_PROMPT = "?";
	private final static String TERMINATOR = ";";
	
	private final Parser parser = Parser.getInstance();
	private final ASTFactory factory = new ASTFactory();
	private final ASTBuilder builder = new ASTBuilder(factory);
	private final ConsoleReader console;
	private final Evaluator evaluator;
	
	public RascalShell() throws IOException {
		console = new ConsoleReader();
		evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));
	}
	
	private void run() throws IOException {
		CommandEvaluator commander = new CommandEvaluator(evaluator, console);
		
		StringBuffer input = new StringBuffer();
		String line;
		
		try {
			while (true) {
				try {
					input.delete(0, input.length());

					do {
						line = prompt(console, input);
						input.append(line + "\n");
					} while (!completeStatement(input));

					String output = handleInput(commander, input);
					console.printString(output);
					console.printNewline();
				}
				catch (FailureException e) {
					break;
				}
				catch (FactTypeError e) {
					console.printString("FactTypeError: " + e.getMessage() + "\n");
				    printStacktrace(console, e);
				}
				catch (RascalTypeError e) {
					console.printString("RascalTypeError: " + e.getMessage() + "\n");
					if (e.hasCause()) {
						console.printString("caused by: " + e.getCause().getMessage() + "\n");
					}
					printStacktrace(console, e);
				}
				catch (RascalBug e) {
					console.printString("RascalBug: " + e.getMessage() + "\n");
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
		catch (FailureException e) {
			return;
		}
	}
	
	private int run(String module, String[] args) throws IOException {
		loadModule(module);
		return callMainFunction(module, args);
	}

	private int callMainFunction(String module, String[] args) throws IOException {
		String callMainStatement = module + "::main(" + mainArguments(args) + ");";
		IConstructor tree = parser.parse(new ByteArrayInputStream(callMainStatement.getBytes()));
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
		IConstructor tree = parser.parse(new ByteArrayInputStream(importCommand.getBytes()));
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
		IConstructor tree = parser.parse(new ByteArrayInputStream(statement.toString().getBytes()));

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

	private String prompt(ConsoleReader console, StringBuffer statement) throws IOException {
		if (statement.length() == 0) {
			return console.readLine(PROMPT);
		}
		else {
		  return console.readLine(CONTINUE_PROMPT);
		}
	}
	
	private boolean completeStatement(StringBuffer statement) {
		if (statement.toString().trim().startsWith(":")) {
			return true;
		}
		else {
			int brackets = 0, curlies = 0, angular = 0, braces = 0, semies = 0;
			boolean multiline = false;
			
			for (byte ch : statement.toString().getBytes()) {
				switch (ch) {
				case '(': brackets++; break;
				case ')': if (brackets > 0) brackets--; break;
				case '{': curlies++; break;
				case '}': if(curlies > 0) curlies--; break;
				case '[': braces++; break;
				case ']': if (braces > 0) braces--; break;
				case ';': semies++; break;
				case '\n': multiline = true; break;
				}
			}
			
			if (multiline) {
			  return brackets == 0 && curlies == 0 && angular == 0 && braces == 0;
			}
			else {
				int lastTerminator = statement.lastIndexOf(TERMINATOR);
				  return (lastTerminator != -1 && statement.substring(lastTerminator).trim().equals(TERMINATOR));	
			}
		}
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
