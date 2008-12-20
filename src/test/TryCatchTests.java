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
		
		assertTrue(tf.runTest("{" + classify + "classify(3) == 1;}"));
		assertTrue(tf.runTest("{" + classify + "classify(f(3)) == 2;}"));
		assertTrue(tf.runTest("{" + classify + "classify(\"abc\") == 3;}"));
		assertTrue(tf.runTest("{" + classify + "classify([1,2,3]) == 4;}"));
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
		assertTrue(tf.runTest("{" + duplicate + "duplicate(3) == 6;}"));
		assertTrue(tf.runTest("{" + duplicate + "duplicate(f(3)) == d(f(3),f(3));}"));
		assertTrue(tf.runTest("{" + duplicate + "duplicate(\"abc\") == \"abcabc\";}"));
		assertTrue(tf.runTest("{" + duplicate + "duplicate(3.5) == 3.5;}"));
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
		
		assertTrue(tf.runTest("{" + dfin + "dfin(3) == fin(6);}"));
		assertTrue(tf.runTest("{" + dfin + "dfin(f(3)) == fin(d(f(3),f(3)));}"));
		assertTrue(tf.runTest("{" + dfin + "dfin(\"abc\") == fin(\"abcabc\");}"));
		assertTrue(tf.runTest("{" + dfin + "dfin(3.5) == fin(3.5);}"));
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
		
		assertTrue(tf.runTest("{" + divide + "divide(3, 2) == 1;}"));
		assertTrue(tf.runTest("{" + divide + "safeDivide(3, 2) == 1;}"));
		assertTrue(tf.runTest("{" + divide + "safeDivide(3, 0) == 0;}"));
	}
		
}
