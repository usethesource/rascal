package org.meta_environment.rascal.interpreter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.tools.ToolProvider;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
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
				catch (FactTypeError e) {
					console.printString("bug: " + e.getMessage() + "\n");
				    printStacktrace(console, e);
				}
				catch (RascalTypeError e) {
					console.printString("error: " + e.getMessage() + "\n");
					if (e.hasCause()) {
						console.printString("caused by: " + e.getCause().getMessage() + "\n");
					}
					printStacktrace(console, e);
				}
				catch (RascalBug e) {
					console.printString("bug: " + e.getMessage() + "\n");
					if (e.hasCause()) {
						console.printString("caused by: " + e.getCause().getMessage() + "\n");
					}
					printStacktrace(console, e);
				}
			}
		}
		catch (FailureException e) {
			return;
		}
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
		INode tree = parser.parse(new ByteArrayInputStream(statement.toString().getBytes()));

		if (tree.getTreeNodeType() == Factory.ParseTree_Summary) {
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
				case ')': brackets--; break;
				case '{': curlies++; break;
				case '}': curlies--; break;
				case '[': braces++; break;
				case ']': braces--; break;
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
		System.err.println(ToolProvider.getSystemToolClassLoader());
		System.err.println(ToolProvider.getSystemJavaCompiler());
		try {
			new RascalShell().run();
			System.err.println("Que le Rascal soit avec vous!");
		} catch (IOException e) {
			System.err.println("unexpected error: " + e.getMessage());
			System.exit(1);
		} 
	}
}
