package test;

import java.io.IOException;

import junit.framework.TestCase;

import org.meta_environment.rascal.interpreter.errors.RascalUndefinedValueError;

public class ImportTests extends TestCase {
	
	public void testFun() throws IOException{
		TestFramework tf = new TestFramework();
		
		tf.prepareModule("module M" +
				         " public int f(int n) {return 2 * n;}" +
				         " private int g(int n) { return 2 * n;}");
		
		assertTrue(tf.runTestInSameEvaluator("import M;"));
		assertTrue(tf.runTestInSameEvaluator("M::f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("f(3) == 6;"));
 // since g() is private, you can not call it from the shell...
		//		assertFalse(tf.runTestInSameEvaluator("g(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("{ int f(int n) {return 3 * n;} f(3) == 9;}"));
	}
	
	public void testVar() throws IOException{
		TestFramework tf = new TestFramework();
		
		tf.prepareModule("module M\n" +
				         "public int n = 3;\n" +
				         "private int m = 3;");
		assertTrue(tf.runTestInSameEvaluator("import M;"));
		assertTrue(tf.runTestInSameEvaluator("M::n == 3;"));
		assertTrue(tf.runTestInSameEvaluator("n == 3;"));
		try {
		  tf.runTestInSameEvaluator("m != 3;");
		  fail("should throw undefined value");
		}
		catch (RascalUndefinedValueError e) {
			// this should happen
		}
		assertTrue(tf.runTestInSameEvaluator("{ int n = 4; n == 4;}"));
	}
	
	public void testMbase1() throws IOException{
		TestFramework tf = new TestFramework();
		
		tf.prepare("import Mbase;");
		
		assertTrue(tf.runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("Mbase::f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("{ int n = 3; n == 3;}"));
	}
	
	public void testMbase2() throws IOException{
		TestFramework tf = new TestFramework();
		
		tf.prepareModule("module M " +
						 "import Mbase; " +
						 "public int m = n;" +
						 "public int f() { return n; }"	 +
						 "public int g() { return m; } "
		);
		
		tf.prepareMore("import M;");
		assertTrue(tf.runTestInSameEvaluator("M::m == 2;"));
		assertTrue(tf.runTestInSameEvaluator("M::f() == 2;"));
		assertTrue(tf.runTestInSameEvaluator("M::g() == 2;"));
	}
	
	public void testMbase3() throws IOException{
		TestFramework tf = new TestFramework();
		
		tf.prepareModule("module M " +
						 "import Mbase;" +
						 "public int g(int n) {return 3 * n;}" +
						 "public int h(int n) {return f(n);}" +
						 "public int m = 3;"
		);
		
		tf.prepareMore("import M;");
		tf.prepareMore("import Mbase;");
		assertTrue(tf.runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("Mbase::f(3) == 6;"));
		
		assertTrue(tf.runTestInSameEvaluator("M::m == 3;"));
		assertTrue(tf.runTestInSameEvaluator("M::g(3) == 9;"));
		
		assertTrue(tf.runTestInSameEvaluator("M::h(3) == 6;"));
	}
	
	public void testSize()  throws IOException{
		TestFramework tf = new TestFramework();
		
		tf.prepareModule("module Msize \n" +
				         "import Set;\n" +
						 "public set[int] Procs = {1, 2, 3};\n" +
						 "public int f() {int nProcs = Set::size(Procs); return nProcs;}\n" +
						 "public int g() {int nProcs = size(Procs); return nProcs;}\n"
		);
		
		tf.runTestInSameEvaluator("import Msize;");
		assertTrue(tf.runTestInSameEvaluator("f() == 3;"));
		assertTrue(tf.runTestInSameEvaluator("g() == 3;"));
	}
	
	
}
