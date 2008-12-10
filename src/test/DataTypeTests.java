package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class DataTypeTests extends TestCase{
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
    private ASTBuilder builder = new ASTBuilder(factory);
    private Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));
	
	private boolean runTest(String statement) throws IOException {
		INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));
		evaluator.clean();
		
		if (tree.getTreeNodeType() ==  Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		} else {
			Command stat = builder.buildCommand(tree);
			IValue value = evaluator.eval(stat.getStatement());
			
			if (value == null || ! value.getType().isBoolType())
				return false;
			return value.equals(ValueFactory.getInstance().bool(true)) ? true : false;
		}
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
		
		assertTrue(runTest("true ==> true == true;"));	
		assertTrue(runTest("true ==> false == false;"));	
		assertTrue(runTest("false ==> true == true;"));	
		assertTrue(runTest("false ==> false == true;"));
		
		assertTrue(runTest("true <==> true == true;"));	
		assertTrue(runTest("true <==> false == false;"));	
		assertTrue(runTest("false <==> true == false;"));	
		assertTrue(runTest("false <==> false == true;"));
	}
	
	public void testInt() throws IOException 
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
		assertTrue(runTest("5 % -2 == 1;"));		
		
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
		
		assertTrue(runTest("(3 > 2 ? 3 : 2) == 3;"));
		
	}
	
	public void testDouble() throws IOException 
	{
		assertTrue(runTest("1.0 == 1.0;"));
		assertTrue(runTest("1.0 != 2.0;"));
		
		assertTrue(runTest("-1.0 == -1.0;"));
		assertTrue(runTest("-1.0 != 1.0;"));
		
		assertTrue(runTest("1.0 + 1.0 == 2.0;"));
		assertTrue(runTest("-1.0 + 2.0 == 1.0;"));
		assertTrue(runTest("1.0 + -2.0 == -1.0;"));
		
		assertTrue(runTest("2.0 - 1.0 == 1.0;"));	
		assertTrue(runTest("2.0 - 3.0 == -1.0;"));	
		assertTrue(runTest("2.0 - -1.0 == 3.0;"));	
		assertTrue(runTest("-2.0 - 1.0 == -3.0;"));	
		
		assertTrue(runTest("2.0 * 3.0 == 6.0;"));	
		assertTrue(runTest("-2.0 * 3.0 == -6.0;"));	
		assertTrue(runTest("2.0 * -3.0 == -6.0;"));
		assertTrue(runTest("-2.0 * -3.0 == 6.0;"));	
		
		assertTrue(runTest("8.0 / 4.0 == 2.0;"));	
		assertTrue(runTest("-8.0 / 4.0 == -2.0;"));
		assertTrue(runTest("8.0 / -4.0 == -2.0;"));	
		assertTrue(runTest("-8.0 / -4.0 == 2.0;"));
		
		assertTrue(runTest("7.0 / 2.0 == 3.5;"));	
		assertTrue(runTest("-7.0 / 2.0 == -3.5;"));
		assertTrue(runTest("7.0 / -2.0 == -3.5;"));	
		assertTrue(runTest("-7.0 / -2.0 == 3.5;"));	
		
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
		
		assertTrue(runTest("3.5 > 2.5 ? 3.5 : 2.5 == 3.5;"));
	}
	
	public void testString() throws IOException {
		assertTrue(runTest("\"\" == \"\";"));
		assertTrue(runTest("\"abc\" != \"\";"));
		assertTrue(runTest("\"abc\" == \"abc\";"));
		assertTrue(runTest("\"abc\" != \"def\";"));
		
		assertTrue(runTest("\"abc\" + \"\" == \"abc\";"));
		assertTrue(runTest("\"abc\" + \"def\" == \"abcdef\";"));
		
		assertTrue(runTest("\"\" <= \"\";"));
		assertTrue(runTest("\"\" <= \"abc\";"));
		assertTrue(runTest("\"abc\" <= \"abc\";"));
		assertTrue(runTest("\"abc\" <= \"def\";"));
		
		assertFalse(runTest("\"\" < \"\";"));
		assertTrue(runTest("\"\" < \"abc\";"));
		assertFalse(runTest("\"abc\" < \"abc\";"));
		assertTrue(runTest("\"abc\" < \"def\";"));
		
		assertTrue(runTest("\"\" >= \"\";"));
		assertTrue(runTest("\"abc\" >= \"\";"));
		assertTrue(runTest("\"abc\" >= \"abc\";"));
		assertTrue(runTest("\"def\" >= \"abc\";"));
		
		assertFalse(runTest("\"\" > \"\";"));
		assertTrue(runTest("\"abc\" > \"\";"));
		assertFalse(runTest("\"abc\" > \"abc\";"));
		assertTrue(runTest("\"def\" > \"abc\";"));
	}
	
	public void testLocation() throws IOException {
		
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
		assertTrue(runTest("3 notin [2, 4, 6];"));
		
		assertTrue(runTest("2 > 3 ? [1,2] : [1,2,3] == [1,2,3];"));
	}
	
	public void testSet() throws IOException {
		assertTrue(runTest("{} == {};"));
		assertTrue(runTest("{} != {1};"));
		assertTrue(runTest("{1} == {1};"));
		assertTrue(runTest("{1} != {2};"));
		assertTrue(runTest("{1, 2} == {1, 2};"));
		assertTrue(runTest("{1, 2} == {2, 1};"));
		assertTrue(runTest("{1, 2, 3, 1, 2, 3} == {3, 2, 1};"));	
		
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};"));
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 2, 3, 4, 5, 6, 7, 8, 9, 1};"));
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 3, 4, 5, 6, 7, 8, 2, 1};"));
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 4, 5, 6, 3, 8, 2, 1};"));
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 6, 5, 4, 3, 8, 2, 1};"));
		
		assertTrue(runTest("{{1}, {2}} == {{2}, {1}};"));
		assertTrue(runTest("{{}} == {{}};"));
		assertTrue(runTest("{{}, {}} == {{}};"));
		assertTrue(runTest("{{}, {}, {}} == {{}};"));
		
		assertTrue(runTest("{{1, 2}, {3,4}} == {{2,1}, {4,3}};"));	
	
		assertTrue(runTest("{} + {} == {};"));
		assertTrue(runTest("{1, 2, 3} + {} == {1, 2, 3};"));
		assertTrue(runTest("{} + {1, 2, 3} == {1, 2, 3};"));
		assertTrue(runTest("{1, 2} + {3, 4, 5} == {1, 2, 3, 4, 5};"));	
		assertTrue(runTest("{1, 2, 3, 4} + {3, 4, 5} == {1, 2, 3, 4, 5};"));
		assertTrue(runTest("{{1, 2}, {3,4}} + {{5,6}} == {{1,2},{3,4},{5,6}};"));	
		
		assertTrue(runTest("{} - {} == {};"));
		assertTrue(runTest("{1, 2, 3} - {} == {1, 2, 3};"));
		assertTrue(runTest("{} - {1, 2, 3} == {};"));
		assertTrue(runTest("{1, 2, 3} - {3, 4, 5} == {1, 2};"));	
		assertTrue(runTest("{1, 2, 3, 4} - {1, 2, 3, 4, 5} == {};"));
		assertTrue(runTest("{{1, 2}, {3,4}, {5,6}} - {{3,4}} == {{1,2}, {5,6}};"));
		
		assertTrue(runTest("{} & {} == {};"));
		assertTrue(runTest("{1, 2, 3} & {} == {};"));
		assertTrue(runTest("{} & {1, 2, 3} == {};"));
		assertTrue(runTest("{1, 2, 3} & {3, 4, 5} == {3};"));	
		assertTrue(runTest("{1, 2, 3, 4} & {3, 4, 5} == {3, 4};"));	
		assertTrue(runTest("{{1,2},{3,4},{5,6}} & {{2,1}, {8,7}, {6,5}} == {{1,2},{5,6}};"));
		
		assertTrue(runTest("{} <= {};"));
		assertTrue(runTest("{} <= {1};"));
		assertTrue(runTest("{2, 1} <= {1, 2};"));
		assertTrue(runTest("{2, 1} <= {1, 2, 3};"));
		assertTrue(runTest("{2, 1} <= {2, 1, 0};"));
	
		assertTrue(runTest("{} < {1};"));
		assertTrue(runTest("{2, 1} < {2, 1, 3};"));
	
		assertTrue(runTest("{} >= {};"));
		assertTrue(runTest("{1} >= {};"));
		assertTrue(runTest("{2, 3} >= {2};"));
	
		assertTrue(runTest("{1} > {};"));
		assertTrue(runTest("{2, 1, 3} > {2, 3};"));
		
		assertTrue(runTest("2 in {1, 2, 3};"));
		assertTrue(runTest("{4,3} in {{1, 2}, {3,4}, {5,6}};"));
		
		assertTrue(runTest("5 notin {1, 2, 3};"));
		assertTrue(runTest("{7,8} notin {{1, 2}, {3,4}, {5,6}};"));
		
		assertTrue(runTest("3 > 2 ? {1,2} : {1,2,3} == {1,2};"));
		
		assertTrue(runTest("{<\"a\", [1,2]>, <\"b\", []>, <\"c\", [4,5,6]>} != {};"));
		
	}
	
	public void testMap() throws IOException {
		assertTrue(runTest("() == ();"));
		assertTrue(runTest("(1:10) != ();"));
		assertTrue(runTest("(1:10) == (1:10);"));
		assertTrue(runTest("(1:10) != (2:20);"));
		
		assertTrue(runTest("() + () == ();"));
		assertTrue(runTest("(1:10) + () == (1:10);"));
		assertTrue(runTest("(1:10) + (2:20) == (1:10, 2:20);"));
		assertTrue(runTest("(1:10, 2:20) + (2:25) == (1:10, 2:25);"));
		
		assertTrue(runTest("() - () == ();"));
		assertTrue(runTest("(1:10, 2:20) - () == (1:10,2:20);"));
		assertTrue(runTest("(1:10, 2:20) - (2:20) == (1:10);"));
		assertTrue(runTest("(1:10, 2:20) - (2:25) == (1:10);")); // This is current behaviour; is this ok?
	
		assertTrue(runTest("() & () == ();"));
		assertTrue(runTest("(1:10) & () == ();"));
		assertTrue(runTest("(1:10, 2:20, 3:30, 4:40) & (2:20, 4:40, 5:50) == (2:20, 4:40);"));
		assertTrue(runTest("(1:10, 2:20, 3:30, 4:40) & (5:50, 6:60) == ();"));
		
		assertTrue(runTest("() <= ();"));
		assertTrue(runTest("() <= (1:10);"));
		assertTrue(runTest("(1:10) <= (1:10);"));
		assertTrue(runTest("(1:10) <= (1:10, 2:20);"));
		
		assertFalse(runTest("() < ();"));
		assertTrue(runTest("() < (1:10);"));
		assertFalse(runTest("(1:10) < (1:10);"));
		assertTrue(runTest("(1:10) < (1:10, 2:20);"));
		
		assertTrue(runTest("() >= ();"));
		assertTrue(runTest("(1:10) >= ();"));
		assertTrue(runTest("(1:10) >= (1:10);"));
		assertTrue(runTest("(1:10, 2:20) >= (1:10);"));
		
		assertFalse(runTest("() > ();"));
		assertTrue(runTest("(1:10) > ();"));
		assertFalse(runTest("(1:10) > (1:10);"));
		assertTrue(runTest("(1:10, 2:20) > (1:10);"));
		
		
		assertTrue(runTest("20 in (1:10, 2:20);"));
		assertFalse(runTest("15 in (1:10, 2:20);"));
		
		assertTrue(runTest("15 notin (1:10, 2:20);"));
		assertFalse(runTest("20 notin (1:10, 2:20);"));
		
		assertTrue(runTest("{map[str,list[int]] m = (\"a\": [1,2], \"b\": [], \"c\": [4,5,6]); m[\"a\"] == [1,2];}"));
	}
	
	public void testTuple() throws IOException {
		assertTrue(runTest("<1, 2.5, true> == <1, 2.5, true>;"));
		assertTrue(runTest("<1, 2.5, true> != <0, 2.5, true>;"));
		assertTrue(runTest("<{1,2}, 3> == <{2,1}, 3>;"));
		assertTrue(runTest("<1, {2,3}> == <1, {3,2}>;"));
		assertTrue(runTest("<{1,2}, {3,4}> == <{2,1},{4,3}>;"));
		
	}
	
	public void testRelation() throws IOException {
		assertTrue(runTest("{} == {};"));
		assertTrue(runTest("{<1,10>} == {<1,10>};"));
		assertTrue(runTest("{<1,2,3>} == {<1,2,3>};"));
		assertTrue(runTest("{<1,10>, <2,20>} == {<1,10>, <2,20>};"));
		assertTrue(runTest("{<1,10>, <2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};"));
		assertTrue(runTest("{<1,2,3>, <4,5,6>} == {<4,5,6>, <1,2,3>};"));
		assertTrue(runTest("{<1,2,3,4>, <4,5,6,7>} == {<4,5,6,7>, <1,2,3,4>};"));
		
		assertTrue(runTest("{} != {<1,2>, <3,4>};"));
		assertFalse(runTest("{<1,2>, <3,4>} == {};"));
		
		assertTrue(runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<1, {1,2,3}>, <2, {2,3,4}>};"));
		assertTrue(runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {2,3,4}>, <1, {1,2,3}>};"));
		assertTrue(runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {4,3,2}>, <1, {2,1,3}>};"));
		
		assertTrue(runTest("{<1,10>} + {} == {<1,10>};"));
		assertTrue(runTest("{} + {<1,10>}  == {<1,10>};"));
		assertTrue(runTest("{<1,10>} + {<2,20>} == {<1,10>, <2,20>};"));
		assertTrue(runTest("{<1,10>, <2,20>} + {<3,30>} == {<1,10>, <2,20>, <3,30>};"));
		assertTrue(runTest("{<1,10>, <2,20>} + {<2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};"));
		
		assertTrue(runTest("{<1,10>} - {} == {<1,10>};"));
		assertTrue(runTest("{} - {<1,10>}  == {};"));
		assertTrue(runTest("{<1,10>, <2,20>} - {<2,20>, <3,30>} == {<1,10>};"));
		
		assertTrue(runTest("{<1,10>} & {} == {};"));
		assertTrue(runTest("{} & {<1,10>}  == {};"));
		assertTrue(runTest("{<1,10>, <2,20>} & {<2,20>, <3,30>} == {<2,20>};"));
		assertTrue(runTest("{<1,2,3,4>, <2,3,4,5>} & {<2,3,4,5>,<3,4,5,6>} == {<2,3,4,5>};"));
		
		assertTrue(runTest("<2,20> in {<1,10>, <2,20>, <3,30>};"));
		assertTrue(runTest("<1,2,3> in {<1,2,3>, <4,5,6>};"));
		
		assertTrue(runTest("<4,40> notin {<1,10>, <2,20>, <3,30>};"));
		assertTrue(runTest("<1,2,4> notin {<1,2,3>, <4,5,6>};"));
		
		assertTrue(runTest("{} o {} == {};"));
		assertTrue(runTest("{<1,10>,<2,20>} o {} == {};"));
		assertTrue(runTest("{} o {<10,100>, <20,200>} == {};"));
		assertTrue(runTest("{<1,10>,<2,20>} o {<10,100>, <20,200>} == {<1,100>, <2,200>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>} + == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>} * == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>, <1, 1>, <2, 2>, <3, 3>, <4, 4>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}+ ==	{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}* == {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>, <1, 1>, <5, 5>};"));
	}
	
	public void testTree() throws IOException {
		assertTrue(runTest("f() == f();"));
		assertTrue(runTest("f() != g();"));
		assertTrue(runTest("f(1) == f(1);"));
		assertTrue(runTest("f(1) != g(1);"));
		assertTrue(runTest("f(1,2) == f(1,2);"));
		assertTrue(runTest("f(1,2) != f(1,3);"));
		assertTrue(runTest("f(1,g(2,3)) == f(1,g(2,3));"));
		assertTrue(runTest("f(1,g(2,3)) != f(1,g(2,4));"));
		assertTrue(runTest("f(1,g(2,{3,4,5})) == f(1,g(2,{3,4,5}));"));
		assertTrue(runTest("f(1,g(2,{3,4,5})) != f(1,g(2,{3,4,5,6}));"));
		assertTrue(runTest("f(1,g(2,[3,4,5])) == f(1,g(2,[3,4,5]));"));
		assertTrue(runTest("f(1,g(2,[3,4,5])) != f(1,g(2,[3,4,5,6]));"));
		assertTrue(runTest("f(1,g(2,(3:30,4:40,5:50))) == f(1,g(2,(3:30,4:40,5:50)));"));
		assertTrue(runTest("f(1,g(2,(3:30,4:40,5:50))) != f(1,g(2,(3:30,4:40,5:55)));"));
	}
	
	public void testOther() throws IOException {
		assertTrue(runTest("1 =? 13 == 1;"));
		assertTrue(runTest("x =? 13 == 13;"));
		assertTrue(runTest("{ x = 3; x =? 13 == 3; }"));
	}
}
