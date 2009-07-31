package test;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.StaticError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFunctionError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UninitializedVariableError;

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
		
		prepare("data Exception = divide_by_zero();");
		
		assertTrue(runTestInSameEvaluator("{" + divide + "divide(3, 2) == 1;}"));
		assertTrue(runTestInSameEvaluator("{" + divide + "safeDivide(3, 2) == 1;}"));
		assertTrue(runTestInSameEvaluator("{" + divide + "safeDivide(3, 0) == 0;}"));
	}

	@Test
	public void emptyListException(){
		String fun =
			
		"bool fun() {" +
		"  try {" +
		"     head([]);" +
		"  } catch EmptyList():" +
		"      return true;" +
		"  return false;" +
		"}";
		
		prepare("import Exception;");
		prepareMore("import List;");
	
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Test
	public void emptyMapException(){
		String fun =
			
		"bool fun() {" +
		"  try {" +
		"     getOneFrom(());" +
		"  } catch EmptyMap():" +
		"      return true;" +
		"  return false;" +
		"}";
		
		prepare("import Exception;");
		prepareMore("import Map;");
		
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Test
	public void emptySetException(){
		String fun =
			
		"bool fun() {" +
		"  try {" +
		"     getOneFrom({});" +
		"  } catch EmptySet:" +
		"      return true;" +
		"  return false;" +
		"}";
		
		prepareMore("import Exception;");
		prepare("import Set;");
		
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Test
	public void IndexOutOfBoundsException(){
		String fun =
			
		"bool fun() {" +
		"  try {" +
		"     [0,1,2][3];" +
		"  } catch IndexOutOfBounds(int i):" +
		"      return true;" +
		"  return false;" +
		"}";
	
		prepare("import Exception;");
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Test(expected=StaticError.class)
	public void NoSuchAnnotationException(){
		String fun =
			
		"bool fun() {" +
		"     1@pos;" +
		"}";
	
		prepare("import Exception;");
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Test
	public void NoSuchFileException(){
		String fun =
			
		"bool fun() {" +
		"  try {" +
		"      S = readFile(\"DoesNotExist\");" +
		"  } catch FileNotFound(str name):" +
		"      return true;" +
		"  return false;" +
		"}";
	
		prepare("import Exception;");
		prepareMore("import IO;");
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Ignore @Test(expected=SyntaxError.class)
	public void SubscriptException(){
		String fun =
			
		"bool fun() {" +
		"  try {" +
		"      [1,2,3][1,2];" +
		"  } catch SubscriptException(str e):" +
		"      return true;" +
		"  return false;" +
		"}";
	
		prepare("import Exception;");
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Test(expected=UndeclaredFunctionError.class)
	public void UndefinedValueException(){
		String fun =
			
		"bool fun() {" +
		"      X + 3;" +
		"}";
	
		prepare("import Exception;");
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedvariableException(){
		String fun =
			
		"bool fun() {" +
		"      X[2] = 3;" +
		"}";
	
		prepare("import Exception;");
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Test(expected=StaticError.class)
	public void UnknownExceptionError1(){
		String fun =
			
		"bool fun() {" +
		"  try {" +
		"      X[2] = 3;" +
		"  } catch StrangeException e:" +
		"      return true;" +
		"  return false;" +
		"}";
	
		prepare("import Exception;");
		assertTrue(runTestInSameEvaluator("{" + fun + "fun();}"));
	}
	
	@Ignore @Test(expected=StaticError.class)
	public void UnknownExceptionError2(){
		String fun =
			
		"bool fun() {" +
		"  try {" +
		"      X[2] = 3;" +
		"  } catch StrangeException(str e):" +
		"      return true;" +
		"  return false;" +
		"}";
	
		prepare("import Exception;");
		runTestInSameEvaluator("{" + fun + "fun();}");
	}
}
