package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.RascalBug;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class TestFramework {
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
	private ASTBuilder builder = new ASTBuilder(factory);
	private Evaluator evaluator;
	
	boolean runTest(String command) throws IOException {
		evaluator = new Evaluator(ValueFactory.getInstance(), factory,
				new PrintWriter(System.err));
		return execute(command);
	}
	
	boolean runTestInSameEvaluator(String command) throws IOException {
		return execute(command);
	}

	boolean runTest(String command1, String command2) throws IOException {
		evaluator = new Evaluator(ValueFactory.getInstance(), factory,
				new PrintWriter(System.err));
		execute(command1);
		return execute(command2);
	}
	
	boolean runWithError(String command, String msg){
		evaluator = new Evaluator(ValueFactory.getInstance(), factory,
				new PrintWriter(System.err));
		try {
			execute(command);
		} catch (Exception e){
			return e.toString().indexOf(msg) >= 0;			
		}
		return false;
	}
	
	void prepare(String command) throws IOException{
		evaluator = new Evaluator(ValueFactory.getInstance(), factory,
				new PrintWriter(System.err));
		execute(command);
	}

	private boolean execute(String command) throws IOException {
		INode tree = parser.parse(new ByteArrayInputStream(command.getBytes()));

		if (tree.getTreeNodeType() == Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		} else {
			Command cmd = builder.buildCommand(tree);

			if (cmd.isStatement()) {
				IValue value = evaluator.eval(cmd.getStatement());
				if (value == null || !value.getType().isBoolType())
					return false;
				return value.equals(ValueFactory.getInstance().bool(true)) ? true
						: false;
			} else if (cmd.isImport()) {
				IValue value = evaluator.eval(cmd.getImported());
				return true;
			} else if (cmd.isDeclaration()) {
				evaluator.eval(cmd.getDeclaration());
				return true;
			} else {
				throw new RascalBug("unexpected case in eval: " + cmd);
			}
		}
	}
}
