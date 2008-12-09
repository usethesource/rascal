package test;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

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
import org.meta_environment.uptr.Factory;

public class CallTests extends TestCase{
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
	
	public void testFac() throws IOException {
		String fac = "int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}";
		
		assertTrue(runTest("{" + fac + " fac(0) == 1;}"));
		assertTrue(runTest("{" +  fac + " fac(1) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	public void testHigherOrder() throws IOException  {
		String add = "int add(int a, int b) { return a + b; }";
		String sub = "int sub(int a, int b) { return a - b; }";
		String doSomething = "int doSomething(int (int a, int b) F) { return #F(1,2); }";

		assertTrue(runTest("{" + add + " " + doSomething + " " + "doSomething(#add) == 3;}"));
		assertTrue(runTest("{" + add + " " + sub + " " + doSomething + " " + "doSomething(#sub) == -1;}"));
	}
	
	public void testSideEffect() throws IOException {
		String one = "void One() { called = called + 1; return; }";
		
		assertTrue(runTest("{ int called = 0; " + one + " One(); One(); One(); called == 3;}"));
	}
	
	public void testMax1() throws IOException {
		String maxInt = "int max(int a, int b) { return a > b ? a : b; }";
		String maxDouble = "double max(double a, double b) { return a > b ? a : b; }";
		assertTrue(runTest("{" + maxInt + " max(3,4) == 4;}"));
		assertTrue(runTest("{" + maxInt + maxDouble + " (max(3,4) == 4) && (max(3.0,4.0) == 4.0);}"));
	}
	
	public void testMax2() throws IOException {
		String max = "&T max(&T a, &T b) { return a > b ? a : b; }";
		assertTrue(runTest("{" + max + " max(3,4) == 4;}"));
		assertTrue(runTest("{" + max + " max(3.0,4.0) == 4.0;}"));
		assertTrue(runTest("{" + max + " max(\"abc\",\"def\") == \"def\";}"));
	}
	
	public void testId() throws IOException {
		String Id = "&T id(&T x){ return x; }";
		assertTrue(runTest("{" + Id + " id(true) == true;}"));
		assertTrue(runTest("{" + Id + " id(4) == 4;}"));
		assertTrue(runTest("{" + Id + " id(4.5) == 4.5;}"));
		assertTrue(runTest("{" + Id + " id(\"abc\") == \"abc\";}"));
		assertTrue(runTest("{" + Id + " id(f(1)) == f(1);}"));
		assertTrue(runTest("{" + Id + " id([1,2,3]) == [1,2,3];}"));
		assertTrue(runTest("{" + Id + " id({1,2,3}) == {1,2,3};}"));
		assertTrue(runTest("{" + Id + " id((1:10,2:20,3:30)) == (1:10,2:20,3:30);}"));
	}
	
	public void testMap() throws IOException {
		String put = "map[&K,&V] put(map[&K,&V] m, &K k, &V v) { m[k] = v; return m; }";
		
		assertTrue(runTest("{" + put + " put((),1,\"1\") == (1:\"1\"); }"));
	}
}

