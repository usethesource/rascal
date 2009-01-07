package test;

import java.io.IOException;

import junit.framework.TestCase;

public class TryCatchTests extends TestCase {
	private static TestFramework tf = new TestFramework();
	
	public void testClassify() throws IOException {
		String classify = 
		"int classify(value v){" +
		"	try {" +
		"		throw v;" +
		"	} catch (int x){" +
		"		return 1;" +
		"	}" +
		"	catch (tree x){" +
		"		return 2;" +
		"	}" +
		"	catch (str s){" +
		"		return 3;" +
		"	}" +
		"	catch {" +
		"		return 4;" +
		"	}" +
		"}";
		
		tf = new TestFramework("data NODE f(int N);");
		
		assertTrue(tf.runTestInSameEvaluator("{" + classify + "classify(3) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + classify + "classify(f(3)) == 2;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + classify + "classify(\"abc\") == 3;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + classify + "classify([1,2,3]) == 4;}"));
	}
	
	public void testDuplicate() throws IOException {
		String duplicate = 
		"	value duplicate(value v){" +
		"	try {" +
		"		throw v;" +
		"	} catch (int x){" +
		"		return x + x;" +
		"	}" +
		"	catch (tree x){" +
		"		return d(x,x);" +
		"	}" +
		"	catch (str s){" +
		"		return s + s;" +
		"	}" +
		"	catch {" +
		"		return v;" +
		"	}" +
		"}";
		
		tf = new TestFramework("data NODE f(int N) | d(NODE a, NODE b);");
		
		assertTrue(tf.runTestInSameEvaluator("{" + duplicate + "duplicate(3) == 6;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + duplicate + "duplicate(f(3)) == d(f(3),f(3));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + duplicate + "duplicate(\"abc\") == \"abcabc\";}"));
		assertTrue(tf.runTestInSameEvaluator("{" + duplicate + "duplicate(3.5) == 3.5;}"));
	}
	
	public void testDFin() throws IOException {
		String dfin = 
		"value dfin(value v){" +
		"    value res = 0;" +
		"	try {" +
		"		throw v;" +
		"	} catch (int x){" +
		"		res = x + x;" +
		"	}" +
		"	catch (tree x){" +
		"		res = d(x,x);" +
		"	}" +
		"	catch (str s){" +
		"		res = s + s;" +
		"	}" +
		"	catch {" +
		"		res = v;" +
		"	}" +
		"	finally {" +
		"		return fin(res);" +
		"	}" +
		"}";
		
		tf = new TestFramework("data NODE f(int N) | fin(value V) | d(NODE a) | d(NODE a, NODE b);");
		
		assertTrue(tf.runTestInSameEvaluator("{" + dfin + "dfin(3) == fin(6);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + dfin + "dfin(f(3)) == fin(d(f(3),f(3)));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + dfin + "dfin(\"abc\") == fin(\"abcabc\");}"));
		assertTrue(tf.runTestInSameEvaluator("{" + dfin + "dfin(3.5) == fin(3.5);}"));
	}
	
	public void testDivide () throws IOException {
		String divide = 
	
		"int divide(int x, int y)" +
		"throws divide_by_zero" +
		"{" +
		"	if(y == 0){" +
		"		throw divide_by_zero();" +
		"	} else {" +
		"		return x / y;" +
		"	}" +
		"}" +
	
		"int safeDivide(int x, int y){" +
		"	try" +
		"		return divide(x,y);" +
		"	catch " +
		"		return 0;" +
		"}";
		
		//TODO: divide_by_zero will become a built-in exception
		
		tf = new TestFramework("data Exception divide_by_zero;");
		
		assertTrue(tf.runTestInSameEvaluator("{" + divide + "divide(3, 2) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + divide + "safeDivide(3, 2) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + divide + "safeDivide(3, 0) == 0;}"));
	}
		
}
