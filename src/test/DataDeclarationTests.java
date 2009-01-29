package test;

import java.io.IOException;

import org.meta_environment.rascal.interpreter.exceptions.RascalException;

import junit.framework.TestCase;

public class DataDeclarationTests extends TestCase{
	
	private static TestFramework tf = new TestFramework();
	
	
	public void testBool() throws IOException {
		
		tf.prepare("data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		
		assertTrue(tf.runTestInSameEvaluator("{Bool b = btrue; b == Bool::btrue;}"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = bfalse; b == Bool::bfalse;}"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = band(btrue,bfalse);  b == Bool::band(Bool::btrue,Bool::bfalse);}"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = bor(btrue,bfalse); b == bor(btrue,bfalse);}"));
		assertTrue(tf.runTestInSameEvaluator("band(btrue,bfalse).left == btrue;"));
		assertTrue(tf.runTestInSameEvaluator("band(btrue,bfalse).right == bfalse;"));
		assertTrue(tf.runTestInSameEvaluator("bor(btrue,bfalse).left == btrue;"));
		assertTrue(tf.runTestInSameEvaluator("bor(btrue,bfalse).right == bfalse;"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = band(btrue,bfalse).left; b == btrue;}"));
		assertTrue(tf.runTestInSameEvaluator("{Bool b = band(btrue,bfalse).right; b == bfalse;}"));
		
		try {
			tf.runTestInSameEvaluator("{Bool b = btrue; b.left == btrue;}");
			fail("should have thrown an exception");
		}
		catch (RascalException e) {
			// should happen
		}
	}
	
	public void testLet1() throws IOException {
		tf = new TestFramework();
		tf.prepare("data Exp = let(str name, Exp exp1, Exp exp2) | var(str name) | \\int(int intVal);");
		assertTrue(tf.runTestInSameEvaluator("{Exp e = \\int(1); e == \\int(1);}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = var(\"a\"); e == var(\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = let(\"a\",\\int(1),var(\"a\")); e ==  let(\"a\",\\int(1),var(\"a\"));}"));
	}
	
	public void testLet2() throws IOException {
		tf = new TestFramework();
		tf.prepare("alias Var2 = str;");
		tf.prepareMore("data Exp2 = let(Var2 var, Exp2 exp1, Exp2 exp2) | var(Var2 var) | \\int(int intVal);");
		
		assertTrue(tf.runTestInSameEvaluator("{Exp2 e = \\int(1); e == \\int(1);}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp2 e = var(\"a\"); e == var(\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp2 e = let(\"a\",\\int(1),var(\"a\")); e ==  let(\"a\",\\int(1),var(\"a\"));}"));
		assertTrue(tf.runTestInSameEvaluator("Var2 var := \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("Var2 var !:= let(\"a\",\\int(1),var(\"a\"));"));
		
	}
}
