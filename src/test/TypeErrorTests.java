package test;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.AssignmentError;
import org.meta_environment.rascal.interpreter.errors.NoSuchFieldError;
import org.meta_environment.rascal.interpreter.errors.TypeError;
import org.meta_environment.rascal.interpreter.errors.UndefinedValueError;

public class TypeErrorTests extends TestFramework {
	
	@Test(expected=TypeError.class)
	public void testAssignTrueToInt() {
		runTest("int i = true;");
	}
	
	@Test(expected=TypeError.class)
	public void testAssert() {
		runTest("assert \"a\": 3.5;");
	}
	
	@Test(expected=AssignmentError.class)
	public void testAssignment() {
		runTest("{int n = 3; n = true;}");
	}
	
	@Test(expected=NoSuchFieldError.class)
	public void testField1() {
		runTest("{tuple[int a, str b] T = <1, \"a\">;T.zip == 0;}");
	}
	
	@Test(expected=TypeError.class)
	public void testIf1() {
		runTest("if(3){n = 4;}");
	}
	
	@Test(expected=TypeError.class)
	public void testWhile11() {
		runTest("while(3){n = 4;}");
	}
	
	@Test(expected=TypeError.class)
	public void testDo1() {
		runTest("do {n = 4;} while(3);");
	}
	
	@Test(expected=UndefinedValueError.class)
	public void testUninit() {
		runTest("zzz;");
	}
	
	@Test(expected=TypeError.class)
	public void testAdd1() {
		runTest("3 + true;");
	}
	
	@Test(expected=TypeError.class)
	public void testSub1() {
		runTest("3 - true;");
	}
	
	@Test(expected=TypeError.class)
	public void testUMinus1() {
		runTest("- true;");
	}
	
	@Test(expected=TypeError.class)
	public void testTimes1() {
		runTest("3 * true;");
	}
	
	@Test(expected=TypeError.class)
	public void testDiv1() {
		runTest("3 / true;");
	}
	
	@Test(expected=TypeError.class)
	public void testMod1() {
		runTest("3 % true;");
	}
	
	@Test(expected=TypeError.class)
	public void testOr1() {
		runTest("3 || true;");
	}
	
	@Test(expected=TypeError.class)
	public void testAnd1() {
		runTest("3 && true;");
	}
	
	@Test(expected=TypeError.class)
	public void testImp1() {
		runTest("3 ==> true;");
	}
	
	@Test(expected=TypeError.class)
	public void testCondExp1() {
		runTest("1 ? 2 : 3;");
	}
	
	@Test(expected=TypeError.class)
	public void testIn1() {
		runTest("1 in 3;");
	}
	
	@Test(expected=TypeError.class)
	public void testComp1() {
		runTest("1 o 3;");
	}
	
	@Test(expected=TypeError.class)
	public void testClos1() {
		runTest("1*;");
	}
	
	@Test(expected=TypeError.class)
	public void testClos2() {
		runTest("1+;");
	}
	
	@Test(expected=TypeError.class)
	public void testGen1() {
		runTest("{x | 5};");
	}
	
	@Test(expected=TypeError.class)
	public void testAny() {
		runTest("any(x : [1,2,3], \"abc\");");
	}
	
	@Test(expected=TypeError.class)
	public void testAll() {
		runTest("all(x : [1,2,3], \"abc\");");
	}


//		assertTrue(runWithError("{x | x : 3};", "expression in generator should be of type list/set"));
		
	
}
