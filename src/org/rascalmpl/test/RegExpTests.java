/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;

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
		assertTrue(runTest("(/if<tst:.*>then<th:.*>fi/ := \"if a \\> b then c fi\") " +
				           "&& (tst == \" a \\> b \") && (th == \" c \");"));

		assertTrue(runTest("(/<l:.*>[Rr][Aa][Ss][Cc][Aa][Ll]<r:.*>/ := \"RASCAL is marvelous\")" +
				            "&& (l == \"\") && (r == \" is marvelous\");"));
		
		assertTrue(runTest("{str x = \"abc\"; /<x>/ := \"abc\";}"));
		assertTrue(runTest("{str x = \"abc\"; int n = 3; /<x><n>/ := \"abc3\";}"));
		
		assertTrue(runTest("(/<x:[a-z]+>-<x>/ := \"abc-abc\") && (x == \"abc\");"));
		assertTrue(runTest("(/<x:[a-z]+>-<x>-<x>/ := \"abc-abc-abc\") && (x == \"abc\");"));
		assertFalse(runTest("(/<x:[a-z]+>-<x>/ := \"abc-def\");"));

		assertTrue(runTest("/\\// := \"/\";"));
		assertTrue(runTest("/<x:\\/>/ := \"/\";"));
		assertTrue(runTest("/<x:\\/>/ := \"/\" && x == \"/\";"));
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
		assertTrue(runTest("{ str x = \"123\"; (/<x:[a-z]+>/ !:= \"abc\" || x == \"123\");}"));
		assertTrue(runTest("{ str x = \"123\"; (/<x:[a-z]+>/ !:= \"abc\");  (x == \"123\");}"));
	}
	
	@Test
	public void repeatedInterpolation() {
		assertTrue(runTest("{r = out:for (i <- [1,2,3]) for (/<i>/ := \"332211\") append out:i; r == [1,1,2,2,3,3]; }"));
	}
	
	@Test 
	public void interpolatingAndEscaped() {
		assertFalse(runTest("{ x = \".\"; /<x>/ := \"a\";}"));
		assertTrue(runTest("{ x = \".\"; /<x>/ := \".\";}"));
		assertTrue(runTest("{ /.<x:bc>/ := \"abc\" && x == \"bc\";}"));
		assertTrue(runTest("{ /^(a|b)*$/ := \"ababab\"; }"));
		assertTrue(runTest("{ /(a|b)<x:cd>/ := \"acd\" && x == \"cd\"; }"));
		assertFalse(runTest("{ /^(a|b)*$/ := \"abacbab\"; }"));
		assertTrue(runTest("{ /(.)<x:bc>/ := \"abc\" && x == \"bc\";}"));
		assertFalse(runTest("{ x = \"(\"; y = \")\"; /<x>.<y>/ := \"a\";}"));
		assertTrue(runTest("{ x = \"(\"; y = \")\"; /<x>.<y>/ := \"(a)\";}"));
	}
	
	@Test 
	public void lotsofbrackets() {
		assertTrue(runTest("/(<x:[a-z]+>)/ := \"abc\" && x == \"abc\""));
		assertTrue(runTest("/((<x:[a-z]+>))/ := \"abc\" && x == \"abc\""));
		assertTrue(runTest("/(<x:([a-z]+)>)/ := \"abc\" && x == \"abc\""));
		assertTrue(runTest("/(<x:(([a-z])+)>)/ := \"abc\" && x == \"abc\""));
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
		assertTrue(runTest("{ int n = 3; (/<x:\\>>/ := \"\\>\" && x == \"\\>\");}"));

		assertTrue(runTest("{ int n = 3; (/<x:\\<>/ := \"\\<\" && x == \"\\<\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:\\< <n>>/ := \"\\< 3\" && x == \"\\< 3\");}"));
		assertTrue(runTest("{ int n = 3; (/<x:\\< <n>\\>>/ := \"\\< 3\\>\" && x == \"\\< 3\\>\");}"));
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
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ !:= \"pqr\") || (x == \"abc\");"));
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
