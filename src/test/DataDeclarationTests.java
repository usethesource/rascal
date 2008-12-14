package test;

import junit.framework.TestCase;
import java.io.IOException;

public class DataDeclarationTests extends TestCase{
	private TestFramework tf = new TestFramework();
	
	public void testBool() throws IOException {
		
		tf.prepare("data Bool btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		
		assertTrue(tf.runTestInSameEvaluator("{Bool b = btrue; b == btrue;}"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = bfalse; b == bfalse;}"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = band(btrue,bfalse);  b == band(btrue,bfalse);}"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = bor(btrue,bfalse); b == bor(btrue,bfalse);}"));
		assertTrue(tf.runTestInSameEvaluator("band(btrue,bfalse).left == btrue;"));
		assertTrue(tf.runTestInSameEvaluator("band(btrue,bfalse).right == bfalse;"));
		assertTrue(tf.runTestInSameEvaluator("bor(btrue,bfalse).left == btrue;"));
		assertTrue(tf.runTestInSameEvaluator("bor(btrue,bfalse).right == bfalse;"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = band(btrue,bfalse).left; b.left == btrue;}"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = band(btrue,bfalse).right; b.left == bfalse;}"));
	}
}
