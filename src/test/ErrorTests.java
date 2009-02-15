package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.TypeError;

public class ErrorTests extends TestFramework {
	
	@Test(expected=TypeError.class)
	public void testAssignTrueToInt() {
		runTest("int i = true;");
	}
	
	public void testErrors()  {
		assertTrue(runWithError("int i = true;", "int i incompatible with initialization type bool"));
		assertTrue(runWithError("assert \"a\": 3.5;", "Expression in assertion should be bool instead of double"));
		assertTrue(runWithError("{int n = 3; n = true;}", "cannot assign value of type bool"));
		assertTrue(runWithError("[1,2][5];", "Subscript out of bounds"));
		assertTrue(runWithError("{tuple[int a, str b] T = <1, \"a\">;T.zip == 0;}", "no field exists"));
		assertTrue(runWithError("if(3){n = 4;}", "has type int but should be bool"));
		assertTrue(runWithError("while(3){n = 4;}", "has type int but should be bool"));
		assertTrue(runWithError("do {n = 4;} while(3);", "has type int but should be bool"));
		assertTrue(runWithError("zzz;", "Uninitialized variable"));
		assertTrue(runWithError("3 + true;", "Operands of + have illegal types"));
		assertTrue(runWithError("3 - true;", "Operands of - have illegal types"));
		assertTrue(runWithError("- true;", "Operand of unary - should be"));
		assertTrue(runWithError("3 * true;", "Operands of * have illegal types"));
		assertTrue(runWithError("3 / true;", "Operands of / have illegal types"));
		assertTrue(runWithError("3 % true;", "Operands of % have illegal types"));
		assertTrue(runWithError("3 || true;", "Operand of boolean operator should be of type bool"));
		assertTrue(runWithError("3 && true;", "Operand of boolean operator should be of type bool"));
		assertTrue(runWithError("3 ==> true;", "Operand of boolean operator should be of type bool"));
		assertTrue(runWithError("1 ? 2 : 3;", "but should be bool"));
		assertTrue(runWithError("1 in 3;", "Operands of in have wrong types"));
		assertTrue(runWithError("1 o 3;", "Operands of o have wrong types"));
		assertTrue(runWithError("1*;", "Operand of + or * closure has wrong type"));
		assertTrue(runWithError("1+;", "Operand of + or * closure has wrong type"));
//		assertTrue(runWithError("{x | x : 3};", "expression in generator should be of type list/set"));
		assertTrue(runWithError("{x | 5};", "Expression as generator should have type bool"));
		assertTrue(runWithError("any(x : [1,2,3], \"abc\");", "Expression as generator should have type bool"));
		assertTrue(runWithError("all(x : [1,2,3], \"abc\");", "Expression as generator should have type bool"));
	}
	
}
