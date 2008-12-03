package test;

import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class StatementTests extends TestCase {
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
    private ASTBuilder builder = new ASTBuilder(factory);
	
	
	private boolean runTest(String statement) throws IOException {
		INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));
		Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));

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
	
    public class All extends TestCase {
    	
	}
    
    public void testAssert() throws IOException {
    	assertTrue(runTest("assert \"1\": 3 > 2;"));
	}
	
	public void testAssignmnment() throws IOException {
		assertTrue(runTest("{int x = 3; assert \"a\": x == 3;};"));
		assertTrue(runTest("{int x = 3; x = 4; assert \"a\": x == 4;};"));
		assertTrue(runTest("{<x, y> = <3, 4>; assert \"a\": (x == 3) && (y == 4);};"));
		assertTrue(runTest("{<x, y, z> = <3, 4, 5>; assert \"a\": (x == 3) && (y == 4) && (z == 5);};"));
		assertTrue(runTest("{<x, y> = <3, 4>; x = 5; assert \"a\": (x == 5) && (y == 4);};"));
		
//		assertTrue(runTest("{int x = 3; x += 2; assert \"a\": x == 5;};"));	
//		assertTrue(runTest("{int x = 3; x -= 2; assert \"a\": x == 1;};"));	
//		assertTrue(runTest("{int x = 3; x *= 2; assert \"a\": x == 6;};"));	
//		assertTrue(runTest("{int x = 3; x /= 2; assert \"a\": x == 1;};"));	
//		assertTrue(runTest("{int x = 3; x %= 2; assert \"a\": x == 1;};"));	
		
		assertTrue(runTest("{list[int] x = [0,1,2]; assert \"a\": x == [0,1,2];};"));
		assertTrue(runTest("{list[int] x = [0,1,2]; assert \"a\": x[0] == 0;};"));
		assertTrue(runTest("{list[int] x = [0,1,2]; assert \"a\": x[1] == 1;};"));
		assertTrue(runTest("{list[int] x = [0,1,2]; assert \"a\": x[2] == 2;};"));
		assertTrue(runTest("{list[int] x = [0,1,2]; x[1] = 10; assert \"a\": (x[0] == 0) && (x[1] == 10) && (x[2] == 2);};"));
		
		assertTrue(runTest("{map[int,int] x = (0:0,1:10,2:20); assert \"a\": x == (0:0,1:10,2:20);};"));
//		assertTrue(runTest("{map[int,int] x = (0:0,1:10,2:20); x[1] = 15; assert \"a\": (x[0] == 0) && (x[1] == 15) && (x[2] == 20);};"));
		
		assertTrue(runTest("{set[int] x = {0,1,2}; assert \"a\": x == {0,1,2};};"));
		assertTrue(runTest("{set[int] x = {0,1,2}; x = x + {3,4}; assert \"a\": x == {0,1,2, 3,4};};"));
	}
	
	public void testBlock() throws IOException {
	}
	
	public void testBreak() throws IOException {
	}
	
	public void testContinue() throws IOException {
	}
	
	public void testDoWhile()throws IOException {
		assertTrue(runTest("{int n = 0; m = 2; do {m = m * m; n = n + 1;} while (n < 1); assert \"a\": m == 4;};"));
		assertTrue(runTest("{int n = 0; m = 2; do {m = m * m; n = n + 1;} while (n < 3); assert \"a\": m == 16;};"));
	}
	
	public void testFail() throws IOException {
	}
	
	public void testFirst() throws IOException {
	}
	public void testFor() throws IOException {
		assertTrue(runTest("int n == 0; for(int i:[1,2,3,4]){ n = n + i;}; assert \"a\": n == 10;};"));
		assertTrue(runTest("int n == 0; for(int i:[1,2,3,4], n <= 3){ n = n + i;}; assert \"a\": n == 6;};"));
	}
	public void testIfThen() throws IOException {
		assertTrue(runTest("int n == 10; if(n < 10){n = n - 4;}; assert \"a\": n == 6;};"));
	}
	
	public void testIfThenElse() throws IOException {
		assertTrue(runTest("int n == 10; if(n < 10){n = n - 4;} else { n = n + 4};; assert \"a\": n == 6;};"));
		assertTrue(runTest("int n == 12; if(n < 10){n = n - 4;} else { n = n + 4};; assert \"a\": n == 16;};"));
	}
	public void testInsert() throws IOException {
	}
	public void testReturn() throws IOException {
	}
	public void testSolve() throws IOException {
	}
	public void testSwitch() throws IOException {
		assertTrue(runTest("{int n = 0; switch(2){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} assert \"a\": n == 2;};"));
		assertTrue(runTest("{int n = 0; switch(4){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} assert \"a\": n == 4;};"));
		assertTrue(runTest("{int n = 0; switch(6){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} assert \"a\": n == 6;};"));
		assertTrue(runTest("{int n = 0; switch(8){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} assert \"a\": n == 10;};"));
		
	}
	public void testThrow() throws IOException {
	}
	public void testTryFinally() throws IOException {
	}
	public void testVisit() throws IOException {
	}
	public void testWhile() throws IOException {
		assertTrue(runTest("int n = 0; m = 2; while(n < 3){ m = m * m;}; assert \"a\": m == 8;};"));
	}
	
	public void testTry() throws IOException {
	}
}
