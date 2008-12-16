package test;

import java.io.IOException;

import junit.framework.TestCase;

public class ImportTests extends TestCase {
	
	private TestFramework tf = new TestFramework();
	
	public void testFun() throws IOException{
		
		tf.prepareModule("module M" +
				         " public int f(int n) {return 2 * n;}" +
				         " private int g(int n) { return 2 * n;}");
		
		assertTrue(tf.runTestInSameEvaluator("import M;"));
		assertTrue(tf.runTestInSameEvaluator("M::f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("f(3) == 6;"));
		assertFalse(tf.runTestInSameEvaluator("g(3) == 6"));
		assertTrue(tf.runTestInSameEvaluator("{ int f(int n) {return 3 * n;} f(3) == 9;}"));
	}
	
	public void testVar() throws IOException{
		
		tf.prepareModule("module M\n" +
				         "public int n = 3;\n" +
				         "private int m = 3;");
		assertTrue(tf.runTestInSameEvaluator("import M;"));
		assertTrue(tf.runTestInSameEvaluator("M::n == 3;"));
		assertTrue(tf.runTestInSameEvaluator("n == 3;"));
		assertTrue(tf.runTestInSameEvaluator("m != 3;"));
		assertTrue(tf.runTestInSameEvaluator("{ int n = 4; n == 4;}"));
	}
	
	public void testMbase1() throws IOException{
		
		tf.prepare("import src/test/Mbase;");
		
		assertTrue(tf.runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("Mbase::f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("{ int n = 3; n == 3;}"));
	}
	
	public void testMbase2() throws IOException{
		
		tf.prepareModule("module M " +
						 "import src/test/Mbase;");
		
		assertTrue(tf.runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("Mbase::f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("{ int n = 3; n == 3;}"));
	}
	
	public void testMbase3() throws IOException{
		
		tf.prepareModule("module M " +
						 "import src/test/Mbase;" +
						 " int g(int n) {return 3 * n;}" +
						 "int m = 3;");
		
		assertTrue(tf.runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("n == 2;"));
		assertTrue(tf.runTestInSameEvaluator("Mbase::f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("f(3) == 6;"));
		assertTrue(tf.runTestInSameEvaluator("{ int n = 3; n == 3;}"));
		
		assertTrue(tf.runTestInSameEvaluator("M::m == 3;"));
		assertTrue(tf.runTestInSameEvaluator("m == 2;"));
		assertTrue(tf.runTestInSameEvaluator("M::g(3) == 9;"));
		assertTrue(tf.runTestInSameEvaluator("g(3) == 9;"));
		assertTrue(tf.runTestInSameEvaluator("{ int n = 3; n == 3;}"));
		assertTrue(tf.runTestInSameEvaluator("{ int m = 4; m == 3;}"));
	}
	
	
}
