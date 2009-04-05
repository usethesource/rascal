package test;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;


import static org.junit.Assert.*;

public class RegExpTests extends TestFramework{

	@Test
	public void match() {
		assertTrue(runTest("/abc/ := \"abc\";"));
		assertFalse(runTest("/def/ := \"abc\";"));
		assertTrue(runTest("/def/ !:= \"abc\";"));
		assertTrue(runTest("/[a-z]+/ := \"abc\";"));
		assertTrue(runTest("/.*is.*/ := \"Rascal is marvelous\";"));
		assertTrue(runTest("/@.*@/ := \"@ abc @\";"));
		
		assertTrue(runTest("(/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");"));
		assertTrue(runTest("(/if<tst:.*>then<th:.*>fi/ := \"if a > b then c fi\") " +
				           "&& (tst == \" a > b \") && (th == \" c \");"));

		assertTrue(runTest("(/<l:.*>[Rr][Aa][Ss][Cc][Aa][Ll]<r:.*>/ := \"RASCAL is marvelous\")" +
				            "&& (l == \"\") && (r == \" is marvelous\");"));

	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void matchNonLinearError(){
		assertTrue(runTest("(/<x:[a-z]+>-<x:[a-z]+>/ := \"abc-abc\") && (x == \"abc\");"));
		//assertTrue(runTest("/<x:[a-z]+>-<x:[a-z]+>/ !:= \"abc-ab\";"));
	}
	
	@Test 
	public void matchWithLocalVariables(){
		//TODO: RedeclaredError?
		assertTrue(runTest("{ str x = \"abc\"; (/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");}"));
		assertTrue(runTest("{ str x = \"ab\"; (/<x:[a-z]+>/ !:= \"abc\") && (x == \"ab\");}"));
	}
	
	@Test 
	public void matchWithExternalModuleVariables(){
		prepare("module XX str x = \"abc\";");
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");"));
		assertTrue(runTest("(/<x:[a-z]+>/ !:= \"ab\") && (x == \"abc\");"));
	}
	
	@Test(expected=SyntaxError.class)
	public void RegExpError1(){
		runTest("/[a-/ := \"abc\";");
	}
	
	public void modifiers() {
		assertTrue(runTest("/abc/i := \"ABC\";"));
		assertTrue(runTest("/abc/i := \"ABC\";"));
		
	//TODO:	assertTrue(runTest("/ab.*c/m := \"ab\nc\";"));
	}
}
