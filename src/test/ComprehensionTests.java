package test;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class ComprehensionTests extends TestCase {
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
    private ASTBuilder builder = new ASTBuilder(factory);
	private Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory);
	
	private boolean runTest(String statement) throws IOException {
		INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));

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
	
	public void testSetComprehension() throws IOException {
		assertTrue(runTest("{ X | int X : {} } == {};"));
		assertTrue(runTest("{ X | int X : {1}} == {1};"));
		assertTrue(runTest("{ X | int X : {1, 2}} == {1,2};"));
		assertTrue(runTest("{ X | int X : {1, 1, 1}} == {1};"));
		assertTrue(runTest("{ 1 | int X : {1,2,3}} == {1};"));
		assertTrue(runTest("{ 1 | int X : {1,2,3}, true } == {1};"));
		assertTrue(runTest("{ 1 | int X : {1,2,3}, false} 	== {};"));
		assertTrue(runTest("{ X | int X : {1,2,3}} == {1,2,3};"));
		assertTrue(runTest("{  X | int X : {1,2,3}, true} == {1,2,3};"));
		assertTrue(runTest("{  X | int X : {1,2,3}, false} 	== {};"));
		assertTrue(runTest("{  X | int X : {1,2,3}, X >= 2, X < 3} == {2};"));
//		assertTrue(runTest("{  {} | int X : {1,2,3}} 	== {{}}};"));
//		assertTrue(runTest("{  {} | int X : {1,2,3}, true} == {{}};"));
//		assertTrue(runTest("{  {} | int X : {1,2,3}, false} == {};"));
		assertTrue(runTest("{ <1,2,3> | int X : {1,2,3}} 	== {<1,2,3>};"));
		assertTrue(runTest("{ <1,2,3> | int X : {1,2,3}, true} 	== {<1,2,3>};"));
		assertTrue(runTest("{ <1,2,3> | int X : {1,2,3}, true, true} == {<1,2,3>};"));
		assertTrue(runTest("{ <1,2,3> | int X : {1,2,3}, false}	== {} ;"));
		assertTrue(runTest("{ Y | set[int] Y : {{1,2,3},{10,20,30},{100,200,300}} } == { {1,2,3},{10,20,30},{100,200,300}};"));
		assertTrue(runTest("{1 | 3 > 2} == {1} ;"));
		assertTrue(runTest("{1 | 2 > 3} == {} ;"));
		assertTrue(runTest("{X | int X := 3} == {3} ;"));
		
		assertTrue(runTest("{ X | int X : {Y | int Y : {1,2,3}}} == {1, 2, 3};"));
		assertTrue(runTest("{ X | set[int] Y := {Z | int Z : {1,2,3}}} == {{3,2,1}} ;"));
	}
	
	public void testRelationComprehension() throws IOException {
		assertTrue(runTest("{<X,Y> | <int X, int Y> : {}} == {} ;"));
		assertTrue(runTest("{<X,Y> | int X : {}, int Y : {}} == {};"));
		assertTrue(runTest("{<X,Y> | int X : {1,1,1}, int Y : {2,2,2}} == {<1,2>};"));
		assertTrue(runTest("{<1,2> | int X : {1,2,3}} == {<1,2>};"));
		assertTrue(runTest("{<X,Y> | int X : {1,2,3}, int Y : {2,3,4}} ==  {<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		assertTrue(runTest("{<X,Y> | int X : {1,2,3}, int Y : {2,3,4}, true} ==	{<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		assertTrue(runTest("{<X,Y> | int X : {1,2,3}, int Y : {2,3,4}, false} == {};"));
		assertTrue(runTest("{<X,Y> | int X : {1,2,3}, int Y : {2,3,4}, X >= Y} =={<2, 2>, <3, 2>, <3, 3>};"));
		assertTrue(runTest("{<X,Y> | int X : {1,2,3}, <X, int Y> : {<1,10>, <7,70>, <3,30>,<5,50>}} =={<1, 10>, <3, 30>};"));
		assertTrue(runTest("{<X,Y> | int X : {1,2,3}, <X, str Y> : {<1,\"a\">, <7,\"b\">, <3,\"c\">,<5,\"d\">}} == {<1, \"a\">, <3, \"c\">};"));
		assertTrue(runTest("{<X,Y> | int X <- 3, int Y <- 4} == {<3,4>};"));
		assertTrue(runTest("{<X,Y> | <int X, int Y> <- <3, 4>} == {<3,4>};"));
	}
	
}
