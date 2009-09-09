package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;

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
		
		assertTrue(runTest("{str x = \"abc\"; /<x>/ := \"abc\";}"));
		assertTrue(runTest("{str x = \"abc\"; int n = 3; /<x><n>/ := \"abc3\";}"));
		
		assertTrue(runTest("(/<x:[a-z]+>-<x>/ := \"abc-abc\") && (x == \"abc\");"));
		assertTrue(runTest("(/<x:[a-z]+>-<x>-<x>/ := \"abc-abc-abc\") && (x == \"abc\");"));
		assertFalse(runTest("(/<x:[a-z]+>-<x>/ := \"abc-def\");"));

	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void RedeclaredError(){
		assertTrue(runTest("(/<x:[a-z]+>-<x:[a-z]+>/ := \"abc-abc\") && (x == \"abc\");"));
	}
	
	@Test
	public void matchWithLocalVariable(){
		assertTrue(runTest("{ str x;           (/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");}"));
		assertTrue(runTest("{ str x = \"123\"; (/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");}"));
		assertTrue(runTest("{ str x = \"123\"; (/<x:[a-z]+>/ := \"abc\"); (x == \"123\");}"));
	}
	
	@Test
	public void matchWithLocalVariableOfNonStringType(){
		assertTrue(runTest("{ int x;       (/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");}"));
		assertTrue(runTest("{ int x = 123; (/<x:[a-z]+>/ := \"abc\") && (x == \"abc\");}"));
		assertTrue(runTest("{ int x = 123; (/<x:[a-z]+>/ := \"abc\"); (x == 123);}"));
	}
	
	@Test
	public void nomatchWithLocalVariable(){
		assertTrue(runTest("{ str x = \"123\"; (/<x:[a-z]+>/ !:= \"abc\" && x == \"abc\");}"));
		assertTrue(runTest("{ str x = \"123\"; (/<x:[a-z]+>/ !:= \"abc\");  (x == \"123\");}"));
	}
	
	@Test
	public void InterpolateInPatternVarDecl(){
		
		assertTrue(runTest("{ int n = 3; (/<x:<n>>/ := \"3\" && x == \"3\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:<n>><x>/ := \"33\" && x == \"3\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:a<n>>/ := \"a3\" && x == \"a3\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:<n>b>/ := \"3b\" && x == \"3b\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:a<n>b>/ := \"a3b\" && x == \"a3b\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:a<n>b>/ := \"a3b\" && x == \"a3b\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:a<n>b<n>c>/ := \"a3b3c\" && x == \"a3b3c\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:a<n>b<n>c><x>/ := \"a3b3ca3b3c\" && x == \"a3b3c\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:a{<n>}>/ := \"aaa\" && x == \"aaa\");}"));
		assertTrue(runTest("{ str a = \"a\"; int n = 3; (/<x:<a>{<n>}>/ := \"aaa\" && x == \"aaa\");}"));
		assertTrue(runTest("{ str a = \"abc\"; int n = 3; (/<x:(<a>){<n>}>/ := \"abcabcabc\" && x == \"abcabcabc\");}"));
	
		assertTrue(runTest("{ int n = 3; (/<x:\\\\>/ := \"\\\\\" && x == \"\\\\\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:\\>>/ := \">\" && x == \">\");}"));
		
		// \< is not handled properly by the Rascal grammar ...
		assertTrue(runTest("{ int n = 3; (/<x:\\<>/ := \"<\" && x == \"<\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:\\<<n>>/ := \"<3\" && x == \"<3\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:\\<<n>\\>>/ := \"<3>\" && x == \"<3>\");}"));
	}
	
	@Test
	public void multipleMatches(){
		assertTrue(runTest("[<x, y> | /<x:[a-z]+?><y:[a-z]+?>/ := \"abcd\"] == [<\"a\", \"b\">, <\"c\", \"d\">];"));
		assertTrue(runTest("[y | /<x:abc><y:...>/ := \"abc111abc222abc333\"] == [\"111\", \"222\", \"333\"];"));
		assertTrue(runTest("{int n = 3; [y | /<x:abc><y:.{<n>}>/ := \"abc111abc222abc333\"] == [\"111\", \"222\", \"333\"];}"));
		assertTrue(runTest("[s | /<s:.>/ := \"abcdef\"] ==  [\"a\",\"b\",\"c\",\"d\",\"e\",\"f\"];"));
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
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ !:= \"pqr\") && (x == \"pqr\");"));
		assertTrue(runTestInSameEvaluator("{(/<x:[a-z]+>/ !:= \"pqr\") ; (x == \"abc\");}"));
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
	
	@Test
	public void modifiers() {
		assertTrue(runTest("/abc/i := \"ABC\";"));
		assertTrue(runTest("/abc/i := \"ABC\";"));
		assertTrue(runTest("/ab.*c/s := \"ab\\nc\";"));
		assertTrue(runTest("/ab.*c/si := \"AB\\nc\";"));
	    assertTrue(runTest("/^ab.*c$/smd := \"ab\\r\\nc\";"));
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
	
	@Test
	public void wordCount2(){
	
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
}
