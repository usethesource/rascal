package test;

import org.junit.Ignore;
import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;


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
	}
	
	@Test
	public void matchWithLocalVariableError(){
		assertFalse(runTest("{ str x = \"123\"; (/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");}"));
	}
	
	@Test
	public void matchWithLocalUnitializedVariable(){
		assertTrue(runTest("{ str x; (/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");}"));
	}
	
	@Test(expected=UnexpectedTypeError.class)
	public void matchWithLocalVariableOfWrongType(){
		assertTrue(runTest("{ int x; (/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");}"));
	}
	
	@Test
	public void nomatchWithLocalVariableError(){
		assertTrue(runTest("{ str x = \"123\"; (/<x:[a-z]+>/ !:= \"abc\" && x == \"123\");}"));
	}
	
	@Test 
	public void matchWithExternalModuleVariable(){
		prepareModule("XX", "module XX str x = \"abc\";");
		runTestInSameEvaluator("import XX;");
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");"));
	}
	
	@Test 
	public void nomatchWithExternalModuleVariable(){
		prepareModule("XX", "module XX public str x = \"abc\";");
		runTestInSameEvaluator("import XX;");
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ !:= \"pqr\") && (x == \"abc\");"));
	}
	
	@Test 
	public void matchWithExternalModuleVariableOfWrongType(){
		prepareModule("XX", "module XX int x = 123;");
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");"));
	}
	
	@Test(expected=SyntaxError.class)
	public void RegExpSyntaxError1(){
		runTest("/[a-/ := \"abc\";");
	}
	
	public void modifiers() {
		assertTrue(runTest("/abc/i := \"ABC\";"));
		assertTrue(runTest("/abc/i := \"ABC\";"));
		
	//TODO:	assertTrue(runTest("/ab.*c/m := \"ab\nc\";"));
	}
	
	@Test
	public void wordCount1(){
	
	String cnt = 
		      "int cnt(str S){" +
		      "  int count = 0;" +
		      "  while (/^\\W*<word:\\w+><rest:.*$>/ := S) { " +
		      "         count = count + 1;" +
		      "         S = rest;" +
		      "  }" +
		      "  return count;" +
		      "}";
		assertTrue(runTest("{" + cnt + "cnt(\"abc def ghi\") == 3;}"));
	}
	
	@Test @Ignore // ignored because the semantics of regular expressions needs to be discussed firsts
	public void wordCount2(){
	
	String cnt = 
		      "int cnt(str S){" +
		      "  int count = 0;" +
		      "  str word;" +
		      "  str rest;" +
		      "  while (/^\\W*<word:\\w+><rest:.*$>/ := S) { " +
		      "         count = count + 1;" +
		      "         S = rest;" +
		      "  }" +
		      "  return count;" +
		      "}";
		assertTrue(runTest("{" + cnt + "cnt(\"abc def ghi\") == 3;}"));
	}
}
