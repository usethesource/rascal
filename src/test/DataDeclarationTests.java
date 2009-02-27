package test;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.NoSuchFieldError;
import org.meta_environment.rascal.interpreter.errors.TypeError;

public class DataDeclarationTests extends TestFramework {

	@Test
	public void bool() {

		prepare("data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");

		assertTrue(runTestInSameEvaluator("{Bool b = btrue; b == Bool::btrue;}"));
		assertTrue(runTestInSameEvaluator("{Bool b = bfalse; b == Bool::bfalse;}"));
		assertTrue(runTestInSameEvaluator("{Bool b = band(btrue,bfalse);  b == Bool::band(Bool::btrue,Bool::bfalse);}"));
		assertTrue(runTestInSameEvaluator("{Bool b = bor(btrue,bfalse); b == bor(btrue,bfalse);}"));
		assertTrue(runTestInSameEvaluator("band(btrue,bfalse).left == btrue;"));
		assertTrue(runTestInSameEvaluator("band(btrue,bfalse).right == bfalse;"));
		assertTrue(runTestInSameEvaluator("bor(btrue,bfalse).left == btrue;"));
		assertTrue(runTestInSameEvaluator("bor(btrue,bfalse).right == bfalse;"));
		assertTrue(runTestInSameEvaluator("{Bool b = band(btrue,bfalse).left; b == btrue;}"));
		assertTrue(runTestInSameEvaluator("{Bool b = band(btrue,bfalse).right; b == bfalse;}"));
	}

	@Test
	public void let1() {
		prepare("data Exp = let(str name, Exp exp1, Exp exp2) | var(str name) | \\int(int intVal);");
		
		assertTrue(runTestInSameEvaluator("{Exp e = \\int(1); e == \\int(1);}"));
		assertTrue(runTestInSameEvaluator("{Exp e = var(\"a\"); e == var(\"a\");}"));
		assertTrue(runTestInSameEvaluator("{Exp e = let(\"a\",\\int(1),var(\"a\")); e ==  let(\"a\",\\int(1),var(\"a\"));}"));
	}

	@Test
	public void let2() {
		prepare("alias Var2 = str;");
		prepareMore("data Exp2 = let(Var2 var, Exp2 exp1, Exp2 exp2) | var(Var2 var) | \\int(int intVal);");

		assertTrue(runTestInSameEvaluator("{Exp2 e = \\int(1); e == \\int(1);}"));
		assertTrue(runTestInSameEvaluator("{Exp2 e = var(\"a\"); e == var(\"a\");}"));
		assertTrue(runTestInSameEvaluator("{Exp2 e = let(\"a\",\\int(1),var(\"a\")); e ==  let(\"a\",\\int(1),var(\"a\"));}"));
		assertTrue(runTestInSameEvaluator("Var2 var := \"a\";"));
		assertTrue(runTestInSameEvaluator("Var2 var !:= let(\"a\",\\int(1),var(\"a\"));"));
	}
	

	@Test(expected=NoSuchFieldError.class)
	public void boolError() throws NoSuchFieldError {
		prepare("data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		assertTrue(runTestInSameEvaluator("{Bool b = btrue; b.left == btrue;}"));
	}
	
	public void exactDoubleFieldIsAllowed() throws TypeError {
		runTest("data D = d | d;");
		assertTrue(true);
	}
	
	@Test(expected=TypeError.class)
	public void doubleFieldError2() throws TypeError {
		runTest("data D = d(int n) | d(value v);");
	}
	
	@Test(expected=TypeError.class)
	public void doubleFieldError3() throws TypeError {
		runTest("data D = d(int n) | d(int v);");
	}
	
	@Test(expected=TypeError.class)
	public void doubleFieldError4() throws TypeError {
		prepare("alias INTEGER = int;");
		runTest("data D = d(int n) | d(INTEGER v);");
	}
	
	public void exactDoubleDataDeclarationIsAllowed() throws TypeError {
		prepare("data D = d(int n) | e;");
		runTestInSameEvaluator("data D = d(int n);");
		assertTrue(true);
	}
	
	@Test(expected=TypeError.class)
	public void undeclaredTypeError1() throws NoSuchFieldError {
		runTest("data D = anE(E e);");
	}
}
