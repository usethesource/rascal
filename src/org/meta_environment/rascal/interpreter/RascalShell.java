package org.meta_environment.rascal.interpreter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.INode;
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
						input.append(line);
					}
				} while (!completeStatement(input));

				String output = handleInput(input);
				console.printString(output);
			} 
			catch (FactTypeError e) {
				System.err.println("bug: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private String handleInput(StringBuffer statement) throws IOException {
		Parser parser = Parser.getInstance();
		ASTBuilder builder = new ASTBuilder(new ASTFactory());
		StringBuilder result = new StringBuilder();
		INode tree = parser.parse(new ByteArrayInputStream(statement.toString().getBytes()));

		if (tree.getTreeNodeType() == Factory.ParseTree_Summary) {
			result.append(tree + "\n");
		}
		else {
			result.append("parse: " + tree + "\n");
			Statement stat = builder.buildStatement(tree);
			result.append("stat: " + stat + "\n");
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
		  int lastTerminator = statement.lastIndexOf(TERMINATOR);
		  return lastTerminator != -1 && statement.substring(lastTerminator).trim().equals(TERMINATOR);
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
