package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;

public class RascalTests extends TestCase{
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
    private ASTBuilder builder = new ASTBuilder(factory);
	private Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory);
	
	private boolean runTest(String statement) throws IOException {
		INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));

	//	if (tree.getTreeNodeType() == builder.ParseTree_Summary) {
	//		return false;
	//	}
	//	else {
			Command stat = builder.buildCommand(tree);
			IValue value = evaluator.eval(stat.getStatement());
			
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
//		assertTrue(runTest("false && true == false;"));	 //TODO ok in shell
//		assertTrue(runTest("false && false == false;"));	
		
		assertTrue(runTest("true || true == true;"));	
		assertTrue(runTest("true || false == true;"));	
		assertTrue(runTest("false || true == true;"));	
		assertTrue(runTest("false || false == false;"));	
		
		assertTrue(runTest("true ==> true == true;"));	
		assertTrue(runTest("true ==> false == false;"));	
		assertTrue(runTest("false ==> true == true;"));	
		assertTrue(runTest("false ==> false == true;"));
		
		assertTrue(runTest("true <==> true == true;"));	
		assertTrue(runTest("true <==> false == false;"));	
		assertTrue(runTest("false <==> true == false;"));	
		assertTrue(runTest("false <==> false == true;"));

	}
	
	public void xxtestInt() throws IOException 
	{
		assertTrue(runTest("1 == 1;"));
		assertTrue(runTest("1 != 2;"));
		
		assertTrue(runTest("-1 == -1;"));
		assertTrue(runTest("-1 != 1;"));
		
		assertTrue(runTest("1 + 1 == 2;"));
		assertTrue(runTest("-1 + 2 == 1;"));
		assertTrue(runTest("1 + -2 == -1;"));
		
		assertTrue(runTest("2 - 1 == 1;"));	
		assertTrue(runTest("2 - 3 == -1;"));	
		assertTrue(runTest("2 - -1 == 3;"));	
		assertTrue(runTest("-2 - 1 == -3;"));	
		
		assertTrue(runTest("2 * 3 == 6;"));	
		assertTrue(runTest("-2 * 3 == -6;"));	
		assertTrue(runTest("2 * -3 == -6;"));
		assertTrue(runTest("-2 * -3 == 6;"));	
		
		assertTrue(runTest("8 / 4 == 2;"));	
		assertTrue(runTest("-8 / 4 == -2;"));
		assertTrue(runTest("8 / -4 == -2;"));	
		assertTrue(runTest("-8 / -4 == 2;"));
		
		assertTrue(runTest("7 / 2 == 3;"));	
		assertTrue(runTest("-7 / 2 == -3;"));
		assertTrue(runTest("7 / -2 == -3;"));	
		assertTrue(runTest("-7 / -2 == 3;"));	
		
		assertTrue(runTest("0 / 5 == 0;"));	
		assertTrue(runTest("5 / 1 == 5;"));	
		
		assertTrue(runTest("5 % 2 == 1;"));	
		assertTrue(runTest("-5 % 2 == -1;"));
		assertTrue(runTest("5 % -2 == -1;"));		
		
		assertTrue(runTest("-2 <= -1;"));
		assertTrue(runTest("-2 <= 1;"));
		assertTrue(runTest("1 <= 2;"));
		assertTrue(runTest("2 <= 2;"));
		assertFalse(runTest("2 <= 1;"));
		
		assertTrue(runTest("-2 < -1;"));
		assertTrue(runTest("-2 < 1;"));
		assertTrue(runTest("1 < 2;"));
		assertFalse(runTest("2 < 2;"));
		
		assertTrue(runTest("-1 >= -2;"));
		assertTrue(runTest("1 >= -1;"));
		assertTrue(runTest("2 >= 1;"));
		assertTrue(runTest("2 >= 2;"));
		assertFalse(runTest("1 >= 2;"));
		
		assertTrue(runTest("-1 > -2;"));
		assertTrue(runTest("1 > -1;"));
		assertTrue(runTest("2 > 1;"));
		assertFalse(runTest("2 > 2;"));
		assertFalse(runTest("1 > 2;"));
		
	}
	
	public void testDouble() throws IOException 
	{
		assertTrue(runTest("1.0 == 1.0;"));
		assertTrue(runTest("1.0 != 2.0;"));
		
		assertTrue(runTest("-1.0 == -1.0;"));
		assertTrue(runTest("-1.0 != 1.0;"));
		
		assertTrue(runTest("1.0 + 1.0 == 2.0;"));
		assertTrue(runTest("-1.0 + 2.0 == 1.0;"));
//		assertTrue(runTest("1.0 + -2.0 == -1.0;"));
		
		assertTrue(runTest("2.0 - 1.0 == 1.0;"));	
//		assertTrue(runTest("2.0 - 3 == -1.0;"));	
//		assertTrue(runTest("2.0 - -1.0 == 3;"));	
//		assertTrue(runTest("-2.0 - 1.0 == -3;"));	
		
		assertTrue(runTest("2.0 * 3.0 == 6.0;"));	
		assertTrue(runTest("-2.0 * 3.0 == -6.0;"));	
//		assertTrue(runTest("2.0 * -3.0 == -6.0;"));
//		assertTrue(runTest("-2.0 * -3.0 == 6.0;"));	
		
		assertTrue(runTest("8.0 / 4.0 == 2.0;"));	
//		assertTrue(runTest("-8.0 / 4.0 == -2.0;"));
//		assertTrue(runTest("8.0 / -4.0 == -2.0;"));	
//		assertTrue(runTest("-8.0 / -4.0 == 2.0;"));
		
		assertTrue(runTest("7.0 / 2.0 == 3.5;"));	
//		assertTrue(runTest("-7.0 / 2.0 == -3.5;"));
//		assertTrue(runTest("7.0 / -2.0 == -3.5;"));	
//		assertTrue(runTest("-7.0 / -2.0 == 3.5;"));	
		
		assertTrue(runTest("0.0 / 5.0 == 0.0;"));	
		assertTrue(runTest("5.0 / 1.0 == 5.0;"));	
		
		assertTrue(runTest("-2.0 <= -1.0;"));
		assertTrue(runTest("-2.0 <= 1.0;"));
		assertTrue(runTest("1.0 <= 2.0;"));
		assertTrue(runTest("2.0 <= 2.0;"));
		assertFalse(runTest("2.0 <= 1.0;"));
		
		assertTrue(runTest("-2.0 < -1.0;"));
		assertTrue(runTest("-2.0 < 1.0;"));
		assertTrue(runTest("1.0 < 2.0;"));
		assertFalse(runTest("2.0 < 2.0;"));
		
		assertTrue(runTest("-1.0 >= -2.0;"));
		assertTrue(runTest("1.0 >= -1.0;"));
		assertTrue(runTest("2.0 >= 1.0;"));
		assertTrue(runTest("2.0 >= 2.0;"));
		assertFalse(runTest("1.0 >= 2.0;"));
		
		assertTrue(runTest("-1.0 > -2.0;"));
		assertTrue(runTest("1.0 > -1.0;"));
		assertTrue(runTest("2.0 > 1.0;"));
		assertFalse(runTest("2.0 > 2.0;"));
		assertFalse(runTest("1.0 > 2.0;"));
	}
	
	public void testList() throws IOException 
	{
		assertTrue(runTest("[] == [];"));
		assertTrue(runTest("[] != [1];"));
		assertTrue(runTest("[1] == [1];"));
		assertTrue(runTest("[1] != [2];"));
		assertTrue(runTest("[1, 2] == [1, 2];"));
		assertTrue(runTest("[1, 2] != [2, 1];"));
		
		assertTrue(runTest("[] + [] == [];"));
		assertTrue(runTest("[1, 2, 3] + [] == [1, 2, 3];"));
		assertTrue(runTest("[] + [1, 2, 3] == [1, 2, 3];"));
		assertTrue(runTest("[1, 2] + [3, 4, 5] == [1, 2, 3, 4, 5];"));	
		
		assertTrue(runTest("[] <= [];"));
		assertTrue(runTest("[] <= [1];"));
		assertTrue(runTest("[2, 1, 0] <= [2, 3];"));
		assertTrue(runTest("[2, 1] <= [2, 3, 0];"));
		assertTrue(runTest("[2, 1] <= [2, 1];"));
		assertTrue(runTest("[2, 1] <= [2, 1, 0];"));
		
		assertTrue(runTest("[] < [1];"));
		assertTrue(runTest("[2, 1, 0] < [2, 3];"));
		assertTrue(runTest("[2, 1] < [2, 3, 0];"));
		assertTrue(runTest("[2, 1] < [2, 1, 0];"));
		
		assertTrue(runTest("[] >= [];"));
		assertTrue(runTest("[1] >= [];"));
		assertTrue(runTest("[2, 3] >= [2, 1, 0];"));
		assertTrue(runTest("[2, 3, 0] >= [2, 1];"));
		assertTrue(runTest("[2, 1] >= [2, 1];"));
		assertTrue(runTest("[2, 1, 0] >= [2, 1];"));
		
		assertTrue(runTest("[1] > [];"));
		assertTrue(runTest("[2, 3] > [2, 1, 0];"));
		assertTrue(runTest("[2, 3, 0] > [2, 1];"));
		assertTrue(runTest("[2, 1, 0] > [2, 1];"));
		
		assertTrue(runTest("2 in [1, 2, 3];"));
		assertTrue(runTest("3 in [2, 4, 6];"));
		
	}
	
	public void testSet() throws IOException {
		assertTrue(runTest("{} == {};"));
		assertTrue(runTest("{} != {1};"));
		assertTrue(runTest("{1} == {1};"));
		assertTrue(runTest("{1} != {2};"));
		assertTrue(runTest("{1, 2} == {1, 2};"));
		assertTrue(runTest("{1, 2} == {2, 1};"));
		assertTrue(runTest("{1, 2, 3, 1, 2, 3} == {3, 2, 1};"));	
		
//		assertTrue(runTest("{{1, 2}, {3,4}} == {{2,1}, {4,3}};"));	
	
		assertTrue(runTest("{} + {} == {};"));
		assertTrue(runTest("{1, 2, 3} + {} == {1, 2, 3};"));
		assertTrue(runTest("{} + {1, 2, 3} == {1, 2, 3};"));
		assertTrue(runTest("{1, 2} + {3, 4, 5} == {1, 2, 3, 4, 5};"));	
		assertTrue(runTest("{1, 2, 3, 4} + {3, 4, 5} == {1, 2, 3, 4, 5};"));
//		assertTrue(runTest("{{1, 2}, {3,4}} + {{5,6}} == {{1,2},{3,4},{5,6};"));	
		
		assertTrue(runTest("{} - {} == {};"));
		assertTrue(runTest("{1, 2, 3} - {} == {1, 2, 3};"));
		assertTrue(runTest("{} - {1, 2, 3} == {};"));
		assertTrue(runTest("{1, 2, 3} - {3, 4, 5} == {1, 2};"));	
		assertTrue(runTest("{1, 2, 3, 4} - {1, 2, 3, 4, 5} == {};"));
//		assertTrue(runTest("{{1, 2}, {3,4}, {5,6}} {{3,4}} == {{1,2}, {5,6};"));
		
		assertTrue(runTest("{} & {} == {};"));
//		assertTrue(runTest("{1, 2, 3} & {} == {};")); //TODO: ok in shell but wrong here
		assertTrue(runTest("{} & {1, 2, 3} == {};"));
		assertTrue(runTest("{1, 2, 3} & {3, 4, 5} == {3};"));	
		assertTrue(runTest("{1, 2, 3, 4} & {3, 4, 5} == {3, 4};"));	
	
		assertTrue(runTest("{} <= {};"));
		assertTrue(runTest("{} <= {1};"));
		assertTrue(runTest("{2, 1} <= {1, 2};"));
		assertTrue(runTest("{2, 1} <= {1, 2, 3};"));
		assertTrue(runTest("{2, 1} <= {2, 1, 0};"));
	
//		assertTrue(runTest("{} < {1};"));
//		assertTrue(runTest("{2, 1} < {2, 1, 3};"));
	
		assertTrue(runTest("{} >= {};"));
		assertTrue(runTest("{1} >= {};"));
		assertTrue(runTest("{2, 3} >= {2};"));
	
//		assertTrue(runTest("{1} > {};"));
//		assertTrue(runTest("{2, 1, 3} > {2, 3};"));
		
		assertTrue(runTest("2 in {1, 2, 3};"));
//		assertTrue(runTest("{4,3} in {{1, 2}, {3,4}, {5,6}};"));
		
		assertTrue(runTest("5 notin {1, 2, 3};"));
//		assertTrue(runTest("{7,8} notin {{1, 2}, {3,4}, {5,6}};"));
	
	}
}
