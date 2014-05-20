module tests::functionality::RegExpTests

//	match 

		public test bool match1()=/abc/ := "abc";
		public test bool match2()=/def/ !:= "abc";
		public test bool match3()=/def/ !:= "abc";
		public test bool match4()=/[a-z]+/ := "abc";
		public test bool match5()=/.*is.*/ := "Rascal is marvelous";
		public test bool match6()=/@.*@/ := "@ abc @";
		
		public test bool match7()=(/<x:[a-z]+>/ := "abc") && (x == "abc");
		public test bool match8()=(/if<tst:.*>then<th:.*>fi/ := "if a \> b then c fi") 
				           && (tst == " a \> b ") && (th == " c ");

		public test bool match9()=(/<l:.*>[Rr][Aa][Ss][Cc][Aa][Ll]<r:.*>/ := "RASCAL is marvelous")
				            && (l == "") && (r == " is marvelous");
		
		public test bool match10(){str x = "abc"; return /<x>/ := "abc";}
		public test bool match11() {str x = "abc"; int n = 3; return /<x><n>/ := "abc3";}
		
		public test bool match12()=(/<x:[a-z]+>-<x>/ := "abc-abc") && (x == "abc");
		public test bool match13()=(/<x:[a-z]+>-<x>-<x>/ := "abc-abc-abc") && (x == "abc");
		public test bool match14()=/<x:[a-z]+>-<x>/ !:= "abc-def";

		public test bool match15()=/\// := "/";
		public test bool match16()=/<x:\/>/ := "/";
		public test bool match17()=/<x:\/>/ := "/" && x == "/";
	
		/* NOTE: we no longer allow local shadowing of variables
		public test bool matchWithLocalVariable() { str x;          return (/<x:[a-z]+>/ := "abc") && (x == "abc");}
		public test bool matchWithLocalVariable() { str x = "123"; return (/<x:[a-z]+>/ := "abc") && (x == "abc");}
		public test bool matchWithLocalVariable() { str x = "123"; return (/<x:[a-z]+>/ := "abc"); (x == "123");}
	

		public test bool matchWithLocalVariable() { int x;       return (/<x:[a-z]+>/ := "abc") && (x == "abc");}
		public test bool matchWithLocalVariable() { int x = 123; return (/<x:[a-z]+>/ := "abc") && (x == "abc");}
		public test bool matchWithLocalVariable() { int x = 123; return (/<x:[a-z]+>/ := "abc"); (x == 123);}

		public test bool nomatchWithLocalVariable() { str x = "123"; return (/<x:[a-z]+>/ !:= "abc" || x == "123");}
		public test bool nomatchWithLocalVariable() { str x = "123"; (/<x:[a-z]+>/ !:= "abc");  return (x == "123");}
	*/
	
		public test bool repeatedInterpolation(){r = out:for (i <- [1,2,3]) for (/<i>/ := "332211") append out:i; return r == [1,1,2,2,3,3]; }
	
// interpolatingAndEscaped

		public test bool interpolationAndEscaped() {x = "."; return (/<x>/ !:= "a");}
		public test bool interpolationAndEscaped() {x = "."; return /<x>/ := ".";}
		public test bool interpolationAndEscaped() = /.<x:bc>/ := "abc" && x == "bc";
		public test bool interpolationAndEscaped() = /^(a|b)*$/ := "ababab"; 
		public test bool interpolationAndEscaped()= /(a|b)<x:cd>/ := "acd" && x == "cd";
		public test bool interpolationAndEscaped()= /^(a|b)*$/ !:= "abacbab";
	    public test bool interpolationAndEscaped()= /(.)<x:bc>/ := "abc" && x == "bc";
 	    public test bool interpolationAndEscaped() { x = "("; y = ")"; return /<x>.<y>/ !:= "a";}
		public test bool interpolationAndEscaped() { x = "("; y = ")"; return /<x>.<y>/ := "(a)";}
	
//	literalBracket

		public test bool literalBracket()=/\(/ := "(";
	
// lotsofbrackets

		public test bool lotsofBracket()=/(<x:[a-z]+>)/ := "abc" && x == "abc";
		public test bool lotsofBracket()=/((<x:[a-z]+>))/ := "abc" && x == "abc";
		public test bool lotsofBracket()=/(<x:([a-z]+)>)/ := "abc" && x == "abc";
		public test bool lotsofBracket()=/(<x:(([a-z])+)>)/ := "abc" && x == "abc";
	
//	nogrouplookaheadandbehind

		public test bool nogrouplookaheadandbehind()=/(?s)a.c/ := "a\nc";
	
//	InterpolateInPatternVarDecl

		public test bool interpolateIndPatternDecl(){ int n = 3; return (/<x:<n>>/ := "3" && x == "3");}
		public test bool interpolateIndPatternDecl(){ int n = 3; return (/<x:<n>><x>/ := "33" && x == "3");}
		public test bool interpolateIndPatternDecl(){ int n = 3; return  (/<x:a<n>>/ := "a3" && x == "a3");}
		public test bool interpolateIndPatternDecl(){ int n = 3; return  (/<x:<n>b>/ := "3b" && x == "3b");}
		public test bool interpolateIndPatternDecl(){ int n = 3; return  (/<x:a<n>b>/ := "a3b" && x == "a3b");}
		public test bool interpolateIndPatternDecl(){ int n = 3;  return (/<x:a<n>b>/ := "a3b" && x == "a3b");}
		public test bool interpolateIndPatternDecl(){ int n = 3; return (/<x:a<n>b<n>c>/ := "a3b3c" && x == "a3b3c");}
		public test bool interpolateIndPatternDecl(){ int n = 3; return (/<x:a<n>b<n>c><x>/ := "a3b3ca3b3c" && x == "a3b3c");}
		public test bool interpolateIndPatternDecl(){ int n = 3; return (/<x:a{<n>}>/ := "aaa" && x == "aaa");}
		public test bool interpolateIndPatternDecl(){ str a = "a"; int n = 3; return (/<x:<a>{<n>}>/ := "aaa" && x == "aaa");}
		public test bool interpolateIndPatternDecl(){ str a = "abc"; int n = 3; return (/<x:(<a>){<n>}>/ := "abcabcabc" && x == "abcabcabc");}
	
		public test bool interpolateIndPatternDecl(){ int n = 3;  return (/<x:\\>/ := "\\" && x == "\\");}
		public test bool interpolateIndPatternDecl(){ int n = 3;  return (/<x:\>>/ := "\>" && x == "\>");}

		public test bool interpolateIndPatternDecl(){ int n = 3;  return (/<x:\<>/ := "\<" && x == "\<");}
		public test bool interpolateIndPatternDecl(){ int n = 3;  return (/<x:\< <n>>/ := "\< 3" && x == "\< 3");}
		public test bool interpolateIndPatternDecl(){ int n = 3;  return (/<x:\< <n>\>>/ := "\< 3\>" && x == "\< 3\>");}
	
//	multipleMatches

		public test bool multipleMatches()=[<x, y> | /<x:[a-z]+?><y:[a-z]+?>/ := "abcd"] == [<"a", "b">, <"c", "d">];
		public test bool multipleMatches() {int n = 3;  return [y | /<x:abc><y:.{<n>}>/ := "abc111abc222abc333"] == ["111", "222", "333"];}
		public test bool multipleMatches()=[s | /<s:.>/ := "abcdef"] ==  ["a","b","c","d","e","f"];
	
	
	/* NOTE:  we no longer allow local shadowing of variables
	@Test 
	public void matchWithExternalModuleVariable(){
		prepareModule("XX", "module XX str x = "abc";");
		runTestInSameEvaluator("import XX;");
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ := "abc") && (x == "abc");
	}
	
	@Test 
	public void nomatchWithExternalModuleVariable(){
		prepareModule("XX", "module XX public str x = "abc";");
		runTestInSameEvaluator("import XX;");
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ !:= "pqr") || (x == "abc");
		assertTrue(runTestInSameEvaluator("{(/<x:[a-z]+>/ !:= "pqr") ; (x == "abc");}
	}
	
	@Test 
	public void matchWithExternalModuleVariableOfWrongType(){
		prepareModule("XX", "module XX int x = 123;");
		assertTrue(runTestInSameEvaluator("(/<x:[a-z]+>/ := "abc") && (x == "abc");
	}
	*/

	@expected{RegExpSyntaxError}
	public test bool RegExpSyntaxError1() = /[a-/ := "abc";

// modifiers

		public test bool modifiers1() = /abc/i := "ABC";
		public test bool modifiers2() = /abc/i := "ABC";
		public test bool modifiers3() = /ab.*c/s := "ab\\nc";
		public test bool modifiers4() = /ab.*c/si := "AB\\nc";
	    public test bool modifiers5() = /^ab.*c$/smd := "ab\\r\\nc";

// wordCount

		public test bool wordCount1(){
		      int cnt(str S){
		        int count = 0;
		        while (/^\W*<word:\w+><rest:.*$>/ := S) {
		               count = count + 1;
		               S = rest;
		        }
		        return count;
		      }
			return cnt("abc def ghi") == 3;
		}

		public test bool wordCount2(){
		      int cnt(str S){
		        int count = 0;
		        while (/^\W*<word:\w+><rest:.*$>/ := S) {
		               count = count + 1;
		               S = rest;
		        }
		        return count;
		      }
			return cnt("abc def ghi") == 3;
		}

