package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;

public class RascalTests {
	
	private boolean runTest(String statement) throws IOException {
		Parser parser = Parser.getInstance();
	    ASTBuilder builder = new ASTBuilder(new ASTFactory());
		Evaluator evaluator = new Evaluator(ValueFactory.getInstance());
		INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));

	//	if (tree.getTreeNodeType() == builder.ParseTree_Summary) {
	//		return false;
	//	}
	//	else {
			Statement stat = builder.buildStatement(tree);
			IValue value = evaluator.eval(stat);
			
			if (value == null || ! value.getType().isBoolType())
				return false;
			return value.equals(ValueFactory.getInstance().bool(true)) ? true : false;
	//	}
	}
	
	private void testAddition() throws IOException 
	{
		runTest("assert \"1\": 1 + 1 == 2");
	}
}
