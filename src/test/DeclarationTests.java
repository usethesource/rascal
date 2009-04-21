package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.*;

public class DeclarationTests extends TestFramework {
	@Test
	public void localTypeInferenceBottomScope() {
		runTest("x = 1;");
		runTestInSameEvaluator("x == 1;");
	}
	
	@Test
	public void localTypeInferenceNestedScope() {
		runTest("{ x = 1; x == 1; }");
	}
	
	public void localTypeInferenceNoEscape() {
		runTest("{ x = 1; x == 1; }");
		runTestInSameEvaluator("{ x = \"1\"; x == \"1\";}");
	}
	
	@Test(expected=StaticError.class)
	public void localTypeInferenceNoEscape2() {
		runTest("{ x = 1; x == 1; }");
		runTestInSameEvaluator("x;");
	}
	
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
	
	@Test(expected=RedeclaredVariableError.class)
	public void doubleDeclaration4(){
		assertTrue(runTest("{int N = 3; int N := 3;}"));
	}
	
	
	
	
}
