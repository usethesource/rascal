package test;


import static org.junit.Assert.*;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UninitializedVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;


public class ScopeTests extends TestFramework {
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError1(){
		runTest("{int n; int n;}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError2(){
		runTest("{int n = 1; int n;}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError3(){
		runTest("{int n = 1; int n = 2;}");
	}
	
	@Test
	public void localShadowing(){
		runTest("{int n = 2; int n := 3;}");
	}
	
	@Test
	public void localRedeclarationInt1(){
		assertTrue(runTest("{int n ; int n := 3 && n == 3;}"));
	}
	
	@Test
	public void localRedeclarationInt2(){
		assertTrue(runTest("{int n; [int n] := [3] && n == 3;}"));
	}
	
	@Test
	public void localShadowing2(){
		runTest("{int n; [list[int] n] := [1,2,3] && n == [1,2,3];}");
	}
	
	@Test
	public void localShadowingListMatch(){
		runTest("{list[int] n = [10,20]; [list[int] n] := [1,2,3] && n == [1,2,3];}");
	}
	
	@Test
	public void localRedeclarationList(){
		assertTrue(runTest("{list[int] n; [list[int] n] := [1,2,3] && n == [1,2,3];}"));
	}
	
	@Test(expected=UnexpectedTypeError.class)
	public void localRedeclarationError9(){
		runTest("{int n; /<n:[0-9]*>/ := \"123\";}");
	}
	
	@Test
	public void localComprehensionShadowing(){
		runTest("{int n = 5; L = [n | int n <- [1 .. 10]];}");
	}
	
	@Test
	public void localRedeclarationError10(){
		assertTrue(runTest("{int n; L = [n | int n <- [1 .. 10]]; L == [1 .. 10];}"));
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void moduleRedeclarationError1(){
		prepareModule("module XX public int n = 1; public int n = 2;");
		runTestInSameEvaluator("import XX;");
		assertTrue(runTestInSameEvaluator("n == 1;"));
	}
	
	@Test
	public void moduleAndLocalVarDeclaration(){
		prepareModule("module XX public int n = 1;");
		prepare("import XX;");
		assertTrue(runTestInSameEvaluator("{int n = 2; n == 2;}"));
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void ifNoLeak1(){
		runTest("{if(int n := 3){n == 3;}else{n != 3;} n == 3;}");
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void ifNoLeak2(){
		runTest("{if(int n <- [1 .. 3], n>=3){n == 3;}else{n != 3;} n == 3;}");
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void blockNoLeak1(){
		runTest("{int n = 1; {int m = 2;}; n == 1 && m == 2;}");
	}
	
	@Test
	public void RedeclaredLocal(){
		assertTrue(runTest("{int n = 1; {int m = 2;}; int m = 3; n == 1 && m == 3;}"));
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void innerImplicitlyDeclared(){
		assertTrue(runTest("{int n = 1; {m = 2;}; n == 1 && m == 2;}"));
	}

}
