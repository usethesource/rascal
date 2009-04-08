package test;


import static org.junit.Assert.*;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UninitializedVariableError;


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
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError4(){
		runTest("{int n = 2; int n := 3;}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError5(){
		runTest("{int n ; int n := 3;}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError6(){
		runTest("{int n; [int n] := 3;}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError7(){
		runTest("{int n; [list[int] n] := 3;}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError8(){
		runTest("{int n; /<n:[0-9]*>/ := \"123\";}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void localRedeclarationError9(){
		runTest("{int n; L = [n | int n <- [1 .. 10]];}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void moduleRedeclarationError1(){
		prepareModule("module XX int n = 1; int n = 2;");
	}
	
	@Test
	public void moduleAndLocalVarDeclaration(){
		prepareModule("module XX public int n = 1;");
		assertTrue(runTestInSameEvaluator("import XX;"));
		assertTrue(runTestInSameEvaluator("int n = 2;"));
		
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void ifNoLeak1(){
		runTest("{if(int n := 3){n == 3;}else{n != 3;} n == 3;}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void ifNoLeak2(){
		runTest("{if(int n <- [1 .. 3], n>=3){n == 3;}else{n != 3;} n == 3;}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void blockNoLeak1(){
		runTest("{int n = 1; {int m = 2;}; n == 1 && m == 2;}");
	}
	
	@Test
	public void RedeclaredLocal(){
		assertTrue(runTest("{int n = 1; {int m = 2;}; int m = 3; n == 1 && m == 3;}"));
	}
	
	@Test
	public void innerImplicitlyDeclared(){
		assertTrue(runTest("{int n = 1; {m = 2;}; n == 1 && m == 2;}"));
	}

}
