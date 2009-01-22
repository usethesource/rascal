package test;

import java.io.IOException;

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
	}
	
	public void testLet1() throws IOException {
		tf = new TestFramework();
		tf.prepare("data Exp = let(str name, Exp exp1, Exp exp2) | varExp(str name) | intExp(int intVal);");
		assertTrue(tf.runTestInSameEvaluator("{Exp e = intExp(1); e == intExp(1);}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = varExp(\"a\"); e == varExp(\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp e = let(\"a\",intExp(1),varExp(\"a\")); e ==  let(\"a\",intExp(1),varExp(\"a\"));}"));
	}
	
	public void testLet2() throws IOException {
		tf = new TestFramework();
		tf.prepare("alias Var2 = str;");
		tf.prepareMore("data Exp2 = let(Var2 var, Exp2 exp1, Exp2 exp2) | Var2 var | intExp(int intVal);");
		
		assertTrue(tf.runTestInSameEvaluator("{Exp2 e = intExp(1); e == intExp(1);}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp2 e = \"a\"; e == \"a\";}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp2 e = let(\"a\",intExp(1),\"a\"); e ==  let(\"a\",intExp(1),\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("Var2 var := \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("Var2 var !:= let(\"a\",intExp(1),\"a\");"));
		
	}
	
	public void testLet3() throws IOException {
		tf = new TestFramework();
		tf.prepare("data Exp3 = let(str name, Exp3 exp1, Exp3 exp2) | varExp(str name)  | int intVal;");
		
		assertTrue(tf.runTestInSameEvaluator("{Exp3 e = 1; e == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp3 e = varExp(\"a\"); e == varExp(\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp3 e = let(\"a\",1,varExp(\"a\")); e ==  let(\"a\",1,varExp(\"a\"));}"));
	}
	
	public void testLet4() throws IOException {
		tf = new TestFramework();
		tf.prepare("alias Var4 = str;");
		tf.prepareMore("alias intCon4 = int;");
		tf.prepareMore("data Exp4 = let(Var4 var, Exp4 exp1, Exp4 exp2) | var(Var4 var) | intCon4 intVal;");
		
		assertTrue(tf.runTestInSameEvaluator("{Exp4 e = 1; e == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp4 e = var(\"a\"); e == var(\"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Exp4 e = let(\"a\",1,var(\"a\")); e ==  let(\"a\",1,var(\"a\"));}"));
	}
}
