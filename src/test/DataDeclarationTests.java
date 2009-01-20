package test;

import junit.framework.TestCase;
import java.io.IOException;

public class DataDeclarationTests extends TestCase{
	
	private static TestFramework tf = new TestFramework();
	
	
	public void testBool() throws IOException {
		
		tf.prepare("data Bool btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		
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
	}
	
	public void testLet1() throws IOException {
		tf = new TestFramework();
		tf.prepare("data Exp let(str name, Exp exp1, Exp exp2) | varExp(str name) | intExp(int intVal);");
		assertTrue(tf.runTestInSameEvaluator("{Exp e = intExp(1); e == intExp(1);}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = varExp(\"a\"); e == varExp(\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = let(\"a\",intExp(1),varExp(\"a\")); e ==  let(\"a\",intExp(1),varExp(\"a\"));}"));
	}
	
	public void testLet2() throws IOException {
		tf = new TestFramework();
		tf.prepare("type str Var;");
		tf.prepareMore("data Exp let(Var var, Exp exp1, Exp exp2) | Var var | intExp(int intVal);");
		
		assertTrue(tf.runTestInSameEvaluator("{Exp e = intExp(1); e == intExp(1);}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = \"a\"; e == \"a\";}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = let(\"a\",intExp(1),\"a\"); e ==  let(\"a\",intExp(1),\"a\");}"));
	}
	
	public void testLet3() throws IOException {
		tf = new TestFramework();
		tf.prepare("data Exp let(str name, Exp exp1, Exp exp2) | varExp(str name)  | int intVal;");
		
		assertTrue(tf.runTestInSameEvaluator("{Exp e = 1; e == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = varExp(\"a\"); e == varExp(\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = let(\"a\",1,varExp(\"a\")); e ==  let(\"a\",1,varExp(\"a\"));}"));
	}
	
	public void testLet4() throws IOException {
		tf = new TestFramework();
		tf.prepare("type str Var;");
		tf.prepareMore("type int intCon;");
		tf.prepareMore("data Exp let(Var var, Exp exp1, Exp exp2) | var(Var var) | intCon intVal;");
		
		assertTrue(tf.runTestInSameEvaluator("{Exp e = 1; e == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = var(\"a\"); e == var(\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = let(\"a\",1,var(\"a\")); e ==  let(\"a\",1,var(\"a\"));}"));
	}
	
}
