package test;

import junit.framework.TestCase;
import java.io.IOException;

public class DataDeclarationTests extends TestCase{
	private TestFramework tf = new TestFramework();
	
	public void testBool() throws IOException {
		String boolDecl = "data Bool btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);";
		
		assertTrue(tf.runTest(boolDecl, "{Bool b = btrue; b == btrue;}"));
		assertTrue(tf.runTest(boolDecl, "{Bool b = bfalse; b == bfalse;}"));
		assertTrue(tf.runTest(boolDecl, "{Bool b = band(btrue,bfalse);  b == band(btrue,bfalse);}"));
		assertTrue(tf.runTest(boolDecl, "{Bool b = bor(btrue,bfalse); b == bor(btrue,bfalse);}"));
		assertTrue(tf.runTest(boolDecl, "band(btrue,bfalse).left == btrue;"));
		assertTrue(tf.runTest(boolDecl, "band(btrue,bfalse).right == bfalse;"));
		assertTrue(tf.runTest(boolDecl, "bor(btrue,bfalse).left == btrue;"));
		assertTrue(tf.runTest(boolDecl, "bor(btrue,bfalse).right == bfalse;"));
		assertTrue(tf.runTest(boolDecl, "{Bool b = band(btrue,bfalse).left; b.left == btrue;}"));
	}
}
