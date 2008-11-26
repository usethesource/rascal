package org.meta_environment.rascal.interpreter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;


public class RascalShell {
	private final static String PROMPT = ">";
	private final static String CONTINUE_PROMPT = "?";
	private final static String TERMINATOR = ";";
	private final static String QUIT = "quit";
	
	private Parser parser = Parser.getInstance();
	private ASTBuilder builder = new ASTBuilder(new ASTFactory());
	private Evaluator evaluator = new Evaluator(ValueFactory.getInstance());
	
	private void run() throws IOException, FactTypeError {
		ConsoleReader console = new ConsoleReader();
		
		StringBuffer input = new StringBuffer();
		String line;
		
		quit: while (true) {
			try {
				input.delete(0, input.length());

				do {
					line = prompt(console, input);
					
					if (line == null || line.equals(QUIT)) {
						break quit;
					}
					else {
						input.append(line + "\n");
					}
				} while (!completeStatement(input));

				String output = handleInput(input);
				console.printString(output);
				console.printNewline();
			} 
			catch (FactTypeError e) {
				System.err.println("bug: " + e.getMessage());
				e.printStackTrace();
				console.printNewline();
			}
			catch (RascalTypeError e) {
				System.err.println("error: " + e.getMessage());
				console.printNewline();
				e.printStackTrace();
			}
		}
	}

	private String handleInput(StringBuffer statement) throws IOException {
		StringBuilder result = new StringBuilder();
		INode tree = parser.parse(new ByteArrayInputStream(statement.toString().getBytes()));

		if (tree.getTreeNodeType() == Factory.ParseTree_Summary) {
			result.append(tree + "\n");
		}
		else {
			Statement stat = builder.buildStatement(tree);
			IValue value = evaluator.eval(stat);
			
			if (value == null) {
				return "no result.";
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
		if (statement.toString().trim().equals(QUIT)) {
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
				case '<': angular++; break;
				case '>': angular--; break;
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
		RascalShell r = new RascalShell();
		try {
			r.run();
			System.err.println("Que le Rascal soit avec vous!");
		} catch (IOException e) {
			System.err.println("unexpected error: " + e.getMessage());
			System.exit(1);
		} 
	}
}
