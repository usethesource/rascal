package test;

import java.io.IOException;

import junit.framework.TestCase;

public class RuleTests extends TestCase {
	private static TestFramework tf;
	
	public void testBool() throws IOException {
		
		tf = new TestFramework("import \\Bool-abstract-rules;");
		
		assertTrue(tf.runTestInSameEvaluator("btrue == btrue;"));
		assertTrue(tf.runTestInSameEvaluator("bfalse == bfalse;"));
		
		assertFalse(tf.runTestInSameEvaluator("btrue == bfalse;"));
		
		assertTrue(tf.runTestInSameEvaluator("band(btrue,bfalse) == bfalse;"));	
		assertTrue(tf.runTestInSameEvaluator("band(band(btrue,btrue),band(btrue, bfalse)) == bfalse;"));
		
		assertTrue(tf.runTestInSameEvaluator("bor(btrue,bfalse) == btrue;"));
		
		assertTrue(tf.runTestInSameEvaluator("bor(bor(btrue,btrue),bor(btrue, bfalse)) == btrue;"));
		assertTrue(tf.runTestInSameEvaluator("bor(bor(bfalse,bfalse),bor(bfalse, bfalse)) == bfalse;"));
		
		assertTrue(tf.runTestInSameEvaluator("bor(band(btrue,btrue),band(btrue, bfalse)) == btrue;"));
		
		assertTrue(tf.runTestInSameEvaluator("band(bor(btrue,btrue),band(btrue, bfalse)) == bfalse;"));
		
	}
	
	public void testInteger() throws IOException {
		
		tf = new TestFramework("import \\Integer-abstract-rules;");
		
		assertTrue(tf.runTestInSameEvaluator("add(s(s(z)), s(s(s(z)))) == s(s(s(s(s(z)))));"));
		assertTrue(tf.runTestInSameEvaluator("mul(s(s(z)), s(s(s(z)))) == s(s(s(s(s(s(z))))));"));
		assertTrue(tf.runTestInSameEvaluator("exp(s(s(z)), s(s(s(z)))) == s(s(s(s(s(s(s(s(z))))))));"));
		
		assertTrue(tf.runTestInSameEvaluator("eq(s(s(z)), s(s(s(z)))) == bfalse;"));
		assertTrue(tf.runTestInSameEvaluator("eq(s(s(s(z))), s(s(s(z)))) == btrue;"));
		
	}
}
	
