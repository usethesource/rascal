package test;

import junit.framework.TestCase;
import java.io.IOException;


public class ErrorTests extends TestCase{
	
	private TestFramework tf = new TestFramework();
	
	public void testErrors() throws IOException{
		assertTrue(tf.runTest("int i = true;", "declared type integer incompatible with initialization type bool"));
		assertTrue(tf.runTest("assert \"a\": 3.5;", "expression in assertion should be bool instead of double"));
		assertTrue(tf.runTest("{int n = 3; n = true;}", "cannot assign value of type bool"));
		assertTrue(tf.runTest("[1,2][5];", "Subscript out of bounds"));
		assertTrue(tf.runTest("x.a;", "has no field named a"));
		assertTrue(tf.runTest("if(3){n = 4;}", "has type integer but should be bool"));
		assertTrue(tf.runTest("while(3){n = 4;}", "has type integer but should be bool"));
		assertTrue(tf.runTest("do {n = 4;} while(3);", "has type integer but should be bool"));
		assertTrue(tf.runTest("n;", "Uninitialized variable"));
		assertTrue(tf.runTest("3 + true;", "Operands of + have illegal types"));
		assertTrue(tf.runTest("3 - true;", "Operands of - have illegal types"));
		assertTrue(tf.runTest("- true;", "Operand of unary - should be"));
		assertTrue(tf.runTest("3 * true;", "Operands of * have illegal types"));
		assertTrue(tf.runTest("3 / true;", "Operands of / have illegal types"));
		assertTrue(tf.runTest("3 % true;", "Operands of % have illegal types"));
		assertTrue(tf.runTest("3 || true;", "Operands of || should be boolean instead of"));
		assertTrue(tf.runTest("3 && true;", "Operands of && should be boolean instead of"));
		assertTrue(tf.runTest("3 ==> true;", "Operands of ==> should be boolean instead of"));
		assertTrue(tf.runTest("3 == true;", "Operands of == should have equal types instead of"));
		assertTrue(tf.runTest("3 < true;", "Operands of comparison have unequal types"));
		assertTrue(tf.runTest("3 <= true;", "Operands of comparison have unequal types"));
		assertTrue(tf.runTest("3 > true;", "Operands of comparison have unequal types"));
		assertTrue(tf.runTest("3 >= true;", "Operands of comparison have unequal types"));
		assertTrue(tf.runTest("1 ? 2 : 3;", "but should be bool"));
		assertTrue(tf.runTest("1 in 3;", "Operands of in have wrong types"));
		assertTrue(tf.runTest("1 o 3;", "Operands of o have wrong types"));
		assertTrue(tf.runTest("1*;", "Operand of + or * closure has wrong type"));
		assertTrue(tf.runTest("1+;", "Operand of + or * closure has wrong type"));
		assertTrue(tf.runTest("{x | x : 3};", "expression in generator should be of type list/set"));
		assertTrue(tf.runTest("{x | 5};", "Expression as generator should have type bool"));
		assertTrue(tf.runTest("exists(x : [1,2,3] | \"abc\");", "expression in exists should yield bool"));
		assertTrue(tf.runTest("forall(x : [1,2,3] | \"abc\");", "expression in forall should yield bool"));
	}
}
