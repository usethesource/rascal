package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;

public class RascalTests extends TestCase{
	
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
	
	public void testBool() throws IOException
	{
		assertTrue(runTest("true == true;"));
		assertFalse(runTest("true == false;"));
		assertTrue(runTest("true != false;"));	
		
		assertTrue(runTest("(!true) == false;"));
		assertTrue(runTest("(!false) == true;"));
		
		assertTrue(runTest("true && true == true;"));	
		assertTrue(runTest("true && false == false;"));	
		assertTrue(runTest("false && true == false;"));	
		assertTrue(runTest("false && false == false;"));	
		
		assertTrue(runTest("true || true == true;"));	
		assertTrue(runTest("true || false == true;"));	
		assertTrue(runTest("false || true == true;"));	
		assertTrue(runTest("false || false == false;"));	
		

	}
	
	public void testInt() throws IOException 
	{
		assertTrue(runTest("1 == 1;"));
		assertTrue(runTest("1 != 2;"));
		
		assertTrue(runTest("-1 == -1;"));
		assertTrue(runTest("-1 != 1;"));
		
		assertTrue(runTest("1 + 1 == 2;"));
//		assertTrue(runTest("-1 + 2 == 1;"));
		assertTrue(runTest("1 + -12 == -1;"));
		
		assertTrue(runTest("1 <= 2;"));
		assertTrue(runTest("2 <= 2;"));
		assertFalse(runTest("2 <= 1;"));
		
		assertTrue(runTest("1 < 2;"));
		assertFalse(runTest("2 < 2;"));
		
		assertTrue(runTest("2 >= 1;"));
		assertTrue(runTest("2 >= 2;"));
		assertFalse(runTest("1 >= 2;"));
		
		assertTrue(runTest("2 > 1;"));
		assertFalse(runTest("2 > 2;"));
		assertFalse(runTest("1 > 2;"));
		
		
		
	}
	
	public void testDouble() throws IOException 
	{
		assertTrue(runTest("1.5 == 1.5;"));
		assertFalse(runTest("1.5 == 2.5;"));
	}
	
	public void testList() throws IOException 
	{
		assertTrue(runTest("[1] == [1];"));
		assertFalse(runTest("[1] == [2];"));
		
		assertTrue(runTest("[1, 2] == [1, 2];"));
		assertFalse(runTest("[1, 2] == [2, 1];"));
		assertTrue(runTest("[1, 2] + [3, 4, 5] == [1, 2, 3, 4, 5];"));	
		
	}
	
}
