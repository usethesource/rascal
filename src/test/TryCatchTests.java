package test;

import org.junit.Test;
import static org.junit.Assert.*;

public class TryCatchTests extends TestFramework {
	
	@Test
	public void testClassify()  {
		String classify = 
		"int classify(value v){" +
		"	try {" +
		"		throw v;" +
		"	} catch int x:{" +
		"		return 1;" +
		"	}" +
		"	catch node x: {" +
		"		return 2;" +
		"	}" +
		"	catch str s: {" +
		"		return 3;" +
		"	}" +
		"	catch: {" +
		"		return 4;" +
		"	}" +
		"}";
		
		prepare("data NODEA = f(int N);");
		
		assertTrue(runTestInSameEvaluator("{" + classify + "classify(3) == 1;}"));
		assertTrue(runTestInSameEvaluator("{" + classify + "classify(f(3)) == 2;}"));
		assertTrue(runTestInSameEvaluator("{" + classify + "classify(\"abc\") == 3;}"));
		assertTrue(runTestInSameEvaluator("{" + classify + "classify([1,2,3]) == 4;}"));
	}
	
	@Test
	public void testDuplicate()  {
		String duplicate = 
		"	value duplicate(value v){" +
		"	try {" +
		"		throw v;" +
		"	} catch int x: {" +
		"		return x + x;" +
		"	}" +
		"	catch NODEB x: {" +
		"		return d(x,x);" +
		"	}" +
		"	catch str s: {" +
		"		return s + s;" +
		"	}" +
		"	catch: {" +
		"		return v;" +
		"	}" +
		"}";
		
		prepare("data NODEB = f(int N) | d(NODEB a, NODEB b);");
		
		assertTrue(runTestInSameEvaluator("{" + duplicate + "duplicate(3) == 6;}"));
		assertTrue(runTestInSameEvaluator("{" + duplicate + "duplicate(f(3)) == d(f(3),f(3));}"));
		assertTrue(runTestInSameEvaluator("{" + duplicate + "duplicate(\"abc\") == \"abcabc\";}"));
		assertTrue(runTestInSameEvaluator("{" + duplicate + "duplicate(3.5) == 3.5;}"));
	}
	
	@Test
	public void testDFin()  {
		String dfin = 
		"value dfin(value v){" +
		"    value res = 0;" +
		"	try {" +
		"		throw v;" +
		"	} catch int x: {" +
		"		res = x + x;" +
		"	}" +
		"	catch NODEC x: {" +
		"		res = d(x,x);" +
		"	}" +
		"	catch str s: {" +
		"		res = s + s;" +
		"	}" +
		"	catch: {" +
		"		res = v;" +
		"	}" +
		"	finally {" +
		"		return fin(res);" +
		"	}" +
		"}";
		
		prepare("data NODEC = f(int N) | fin(value V) | d(NODEC a) | d(NODEC a, NODEC b);");
		
		assertTrue(runTestInSameEvaluator("{" + dfin + "dfin(3) == fin(6);}"));
		assertTrue(runTestInSameEvaluator("{" + dfin + "dfin(f(3)) == fin(d(f(3),f(3)));}"));
		assertTrue(runTestInSameEvaluator("{" + dfin + "dfin(\"abc\") == fin(\"abcabc\");}"));
		assertTrue(runTestInSameEvaluator("{" + dfin + "dfin(3.5) == fin(3.5);}"));
	}
	
	@Test
	public void testDivide ()  {
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
		"	catch: " +
		"		return 0;" +
		"}";
		
		//TODO: divide_by_zero will become a built-in exception
		
		prepare("data Exception = divide_by_zero;");
		
		assertTrue(runTestInSameEvaluator("{" + divide + "divide(3, 2) == 1;}"));
		assertTrue(runTestInSameEvaluator("{" + divide + "safeDivide(3, 2) == 1;}"));
		assertTrue(runTestInSameEvaluator("{" + divide + "safeDivide(3, 0) == 0;}"));
	}
		
}
