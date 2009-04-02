package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.*;

public class DeclarationTests extends TestFramework {
	@Test(expected=UndeclaredTypeError.class)
	public void undeclaredType1(){
		runTest("X N;");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void doubleDeclaration1(){
		assertTrue(runTest("{int N = 1; {int N = 2;}; N == 1;}"));
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void doubleDeclaration2(){
		assertTrue(runTest("{N = 1; {int N = 2;}; N == 1;}"));
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void doubleDeclaration3(){
		assertTrue(runTest("{int f(int N){int N = 1; return N;} f(3) == 1;}"));
	}
	
	
}
