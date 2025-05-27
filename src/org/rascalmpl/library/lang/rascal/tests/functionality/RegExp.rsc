@license{
  Copyright (c) 2009-2020 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::tests::functionality::RegExp

import Exception;

//	match 

test bool match1() = /abc/ := "abc";
test bool match2() = /def/ !:= "abc";
test bool match3() = /def/ !:= "abc";
test bool match4() = /[a-z]+/ := "abc";
test bool match5() = /.*is.*/ := "Rascal is marvelous";
test bool match6() = /@.*@/ := "@ abc @";
		
test bool match7() = (/<x:[a-z]+>/ := "abc") && (x == "abc");
test bool match8() = (/if<tst:.*>then<th:.*>fi/ := "if a \> b then c fi") 
				     && (tst == " a \> b ") && (th == " c ");

test bool match9() = (/<l:.*>[Rr][Aa][Ss][Cc][Aa][Ll]<r:.*>/ := "RASCAL is marvelous")
				     && (l == "") && (r == " is marvelous");
		
test bool match10() {str x = "abc"; return /<x>/ := "abc";}
test bool match11() {str x = "abc"; int n = 3; return /<x><n>/ := "abc3";}
		
test bool match12() = (/<x:[a-z]+>-<x>/ := "abc-abc") && (x == "abc");
test bool match13() = (/<x:[a-z]+>-<x>-<x>/ := "abc-abc-abc") && (x == "abc");
test bool match14() = /<x:[a-z]+>-<x>/ !:= "abc-def";

test bool match15() = /\// := "/";

test bool match16() = /<x:\/>/ := "/" && x == "/";
test bool match17() = /<x:\/>/ := "/" && x == "/";

/* NOTE: we no longer allow local shadowing of variables
test bool matchWithLocalVariable() { str x;          return (/<x:[a-z]+>/ := "abc") && (x == "abc");}
test bool matchWithLocalVariable() { str x = "123"; return (/<x:[a-z]+>/ := "abc") && (x == "abc");}
test bool matchWithLocalVariable() { str x = "123"; return (/<x:[a-z]+>/ := "abc"); (x == "123");}
	

test bool matchWithLocalVariable() { int x;       return (/<x:[a-z]+>/ := "abc") && (x == "abc");}
test bool matchWithLocalVariable() { int x = 123; return (/<x:[a-z]+>/ := "abc") && (x == "abc");}
test bool matchWithLocalVariable() { int x = 123; return (/<x:[a-z]+>/ := "abc"); (x == 123);}

test bool nomatchWithLocalVariable() { str x = "123"; return (/<x:[a-z]+>/ !:= "abc" || x == "123");}
test bool nomatchWithLocalVariable() { str x = "123"; (/<x:[a-z]+>/ !:= "abc");  return (x == "123");}
*/
	
test bool repeatedInterpolation(){r = out:for (i <- [1,2,3]) for (/<i>/ := "332211") append out:i; return r == [1,1,2,2,3,3]; }
	
// interpolating

test bool interpolation1() {x = "."; return (/<x>/ !:= "a");}
test bool interpolation2() {x = "."; return /<x>/ := ".";}
test bool interpolation3() = /.<x:bc>/ := "abc" && x == "bc";
test bool interpolation4() = /^(a|b)*$/ := "ababab"; 
test bool interpolation5() = /(a|b)<x:cd>/ := "acd" && x == "cd";
test bool interpolation6() = /^(a|b)*$/ !:= "abacbab";
test bool interpolation7() = /(.)<x:bc>/ := "abc" && x == "bc";
test bool interpolation8() { x = "("; y = ")"; return /<x>.<y>/ !:= "a";}
test bool interpolation9() { x = "("; y = ")"; return /<x>.<y>/ := "(a)";}

// escape
test bool escape1() = /\\/ := "\\";
test bool escape2() = /a\\/ := "a\\";
test bool escape3() = /\\b/ := "\\b";
test bool escape4() = /a\\b/ := "a\\b";

test bool escape5() = /[\\]/ := "\\";
test bool escape6() = /[\\ \<]/ := "\\";
test bool escape7() = /[\\ \<]/ := "\<";

test bool escape8() = /a[\\ \<]/ := "a\\";
test bool escape9() = /a[\\ \<]/ := "a\<";
test bool escape10() = /[\\ \>]b/ := "\\b";
test bool escape11() = /[\\ \>]b/ := "\>b";
test bool escape12() = /a[\\ \>]b/ := "a\\b";
test bool escape13() = /a[\\ \>]b/ := "a\>b";

	
//	literalBracket

test bool literalBracket() = /\(/ := "(";
	
// lotsofbrackets

test bool lotsofBracket1() = /(<x:[a-z]+>)/ := "abc" && x == "abc";
test bool lotsofBracket2() = /((<x:[a-z]+>))/ := "abc" && x == "abc";
test bool lotsofBracket3() = /(<x:([a-z]+)>)/ := "abc" && x == "abc";
test bool lotsofBracket4() = /(<x:(([a-z])+)>)/ := "abc" && x == "abc";
	
//nogrouplookaheadandbehind

test bool nogrouplookaheadandbehind() = /(?s)a.c/ := "a\nc";
	
// InterpolateInPatternVarDecl

test bool interpolateIndPatternDeclSimple1(){ int n = 3; return (/<x:<n>>/ := "3" && x == "3");}
test bool interpolateIndPatternDeclSimple2(){ real n = 3.5; return (/<x:<n>>/ := "3.5" && x == "3.5");}
test bool interpolateIndPatternDeclSimple3(){ rat n = 2r3; return (/<x:<n>>/ := "2r3" && x == "2r3");}

test bool interpolateIndPatternDecl1(){ int n = 3; return (/<x:<n>><x>/ := "33" && x == "3");}
test bool interpolateIndPatternDecl2(){ int n = 3; return  (/<x:a<n>>/ := "a3" && x == "a3");}
test bool interpolateIndPatternDecl3(){ int n = 3; return  (/<x:<n>b>/ := "3b" && x == "3b");}
test bool interpolateIndPatternDecl4(){ int n = 3; return  (/<x:a<n>b>/ := "a3b" && x == "a3b");}
test bool interpolateIndPatternDecl5(){ int n = 3;  return (/<x:a<n>b>/ := "a3b" && x == "a3b");}
test bool interpolateIndPatternDecl6(){ int n = 3; return (/<x:a<n>b<n>c>/ := "a3b3c" && x == "a3b3c");}
test bool interpolateIndPatternDecl7(){ int n = 3; return (/<x:a<n>b<n>c><x>/ := "a3b3ca3b3c" && x == "a3b3c");}
test bool interpolateIndPatternDecl8(){ int n = 3; return (/<x:a{<n>}>/ := "aaa" && x == "aaa");}
test bool interpolateIndPatternDecl9(){ str a = "a"; int n = 3; return (/<x:<a>{<n>}>/ := "aaa" && x == "aaa");}
test bool interpolateIndPatternDecl10(){ str a = "abc"; int n = 3; return (/<x:(<a>){<n>}>/ := "abcabcabc" && x == "abcabcabc");}
	
test bool interpolateIndPatternDecl11(){ return (/<x:\\>/ := "\\" && x == "\\");}
test bool interpolateIndPatternDecl12(){ return (/<x:\>>/ := "\>" && x == "\>");}

test bool interpolateIndPatternDecl13(){ return (/<x:\<>/ := "\<" && x == "\<");}
test bool interpolateIndPatternDecl14(){ int n = 3;  return (/<x:\< <n>>/ := "\< 3" && x == "\< 3");}
test bool interpolateIndPatternDecl15(){ int n = 3;  return (/<x:\< <n>\>>/ := "\< 3\>" && x == "\< 3\>");}
	
//	multipleMatches

test bool multipleMatches1() = [<x, y> | /<x:[a-z]+?><y:[a-z]+?>/ := "abcd"] == [<"a", "b">, <"c", "d">];
test bool multipleMatches2() { int n = 3;  return [y | /abc<y:.{<n>}>/ := "abc111abc222abc333"] == ["111", "222", "333"];}
test bool multipleMatches3() = [s | /<s:.>/ := "abcdef"] ==  ["a","b","c","d","e","f"];
	
/*TODO: add interpreter tests here*/	
/* NOTE:  we no longer allow local shadowing of variables
@Test 
void matchWithExternalModuleVariable(){
prepareModule("XX", "module XX str x = "abc";");
runTestInSameEvaluator("import XX;");
assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ := "abc") && (x == "abc");
}

@Test 
void nomatchWithExternalModuleVariable(){
prepareModule("XX", "module XX str x = "abc";");
runTestInSameEvaluator("import XX;");
assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ !:= "pqr") || (x == "abc");
assertTrue(runTestInSameEvaluator("{(/<x:[a-z]+>/ !:= "pqr") ; (x == "abc");}
}

@Test 
void matchWithExternalModuleVariableOfWrongType(){
prepareModule("XX", "module XX int x = 123;");
assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ := "abc") && (x == "abc");
}
*/

@ignoreCompiler{Remove-after-transtion-to-compiler: Different exception}
@expected{SyntaxError}
test bool RegExpSyntaxError1() = /[a-/ := "abc";
	
@ignoreInterpreter{Different exception}
@expected{RegExpSyntaxError}
test bool RegExpSyntaxError1() = /[a-/ := "abc";

// modifiers

test bool modifiers1() = /abc/i := "ABC";
test bool modifiers2() = /abc/i := "ABC";
test bool modifiers3() = /ab.*c/s := "ab\\nc";
test bool modifiers4() = /ab.*c/si := "AB\\nc";
	    test bool modifiers5() = /^ab.*c$/smd := "ab\\r\\nc";

// wordCount

test bool wordCount1(){
	int cnt(str S){
		int count = 0;
		while (/^\W*\w+<rest:.*$>/ := S) {
			count = count + 1;
			S = rest;
		}
		return count;
	}
	return cnt("abc def ghi") == 3;
}

test bool wordCount2(){
	int cnt(str S){
		int count = 0;
		while (/^\W*\w+<rest:.*$>/ := S) {
			count = count + 1;
			S = rest;
		}
		return count;
	}
	return cnt("abc def ghi") == 3;
}

// RegExp nested in other pattern

test bool regExpInList1() = [/abc/] := ["abc"];
test bool regExpInList2() = [/abc/, /def/] := ["abc", "def"];

@ignore{Interpreter: "append statement without enclosing loop", compiler: "Parse error in concrete syntax"}
test bool regExpInSet1() = {/abc/} := {"abc"};
@ignore{Interpreter: "append statement without enclosing loop", compiler: "Parse error in concrete syntax"}
test bool regExpInSet2() = {/abc/, /def/} := {"abc", "def"};

test bool regExpInTuple1() = </abc/> := <"abc">;
test bool regExpInTuple2() = </abc/, /def/> := <"abc", "def">;

test bool regExpInNode1() = "f"(/abc/) := "f"("abc");
test bool regExpInNode2() = "f"(/abc/, /def/) := "f"("abc", "def");