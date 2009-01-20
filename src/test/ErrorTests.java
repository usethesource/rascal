package test;

import junit.framework.TestCase;
import java.io.IOException;


public class ErrorTests extends TestCase{
	
	private static TestFramework tf = new TestFramework();
	
	public void testErrors() throws IOException{
		assertTrue(tf.runWithError("int i = true;", "declared type integer incompatible with initialization type bool"));
		assertTrue(tf.runWithError("assert \"a\": 3.5;", "expression in assertion should be bool instead of double"));
		assertTrue(tf.runWithError("{int n = 3; n = true;}", "cannot assign value of type bool"));
		assertTrue(tf.runWithError("[1,2][5];", "Subscript out of bounds"));
		assertTrue(tf.runWithError("{tuple[int a, str b] T = <1, \"a\">;T.zip == 0;}", "no field exists"));
		assertTrue(tf.runWithError("if(3){n = 4;}", "has type integer but should be bool"));
		assertTrue(tf.runWithError("while(3){n = 4;}", "has type integer but should be bool"));
		assertTrue(tf.runWithError("do {n = 4;} while(3);", "has type integer but should be bool"));
		assertTrue(tf.runWithError("zzz;", "Uninitialized variable"));
		assertTrue(tf.runWithError("3 + true;", "Operands of + have illegal types"));
		assertTrue(tf.runWithError("3 - true;", "Operands of - have illegal types"));
		assertTrue(tf.runWithError("- true;", "Operand of unary - should be"));
		assertTrue(tf.runWithError("3 * true;", "Operands of * have illegal types"));
		assertTrue(tf.runWithError("3 / true;", "Operands of / have illegal types"));
		assertTrue(tf.runWithError("3 % true;", "Operands of % have illegal types"));
		assertTrue(tf.runWithError("3 || true;", "Operand of boolean operator should be of type bool"));
		assertTrue(tf.runWithError("3 && true;", "Operand of boolean operator should be of type bool"));
		assertTrue(tf.runWithError("3 ==> true;", "Operand of boolean operator should be of type bool"));
		assertTrue(tf.runWithError("1 ? 2 : 3;", "but should be bool"));
		assertTrue(tf.runWithError("1 in 3;", "Operands of in have wrong types"));
		assertTrue(tf.runWithError("1 o 3;", "Operands of o have wrong types"));
		assertTrue(tf.runWithError("1*;", "Operand of + or * closure has wrong type"));
		assertTrue(tf.runWithError("1+;", "Operand of + or * closure has wrong type"));
//		assertTrue(tf.runWithError("{x | x : 3};", "expression in generator should be of type list/set"));
		assertTrue(tf.runWithError("{x | 5};", "Expression as generator should have type bool"));
		assertTrue(tf.runWithError("any(x : [1,2,3], \"abc\");", "Expression as generator should have type bool"));
		assertTrue(tf.runWithError("all(x : [1,2,3], \"abc\");", "Expression as generator should have type bool"));
	}
}
