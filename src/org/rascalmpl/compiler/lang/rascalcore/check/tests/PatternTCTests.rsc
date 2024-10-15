@bootstrapParser
module lang::rascalcore::check::tests::PatternTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool matchNestedList() = checkOK("[[1]] := [];");

test bool matchNestedSet() = checkOK("{{1}} := {};");

data Bool = and(Bool, Bool) | t();
data Prop = or(Prop, Prop) | f();

test bool cannotMatchListStr1() = cannotMatch("[1] := \"a\";");

test bool unguardedMatchNoEscape1() = undeclaredVariable("int n = 3; int m := n; m == n;");

test bool recursiveDataTypeNoPossibleMatchHorizontal1() = 
	cannotMatch("Prop p := and(t(),t());", 
               initialDecls=["data Bool = and(Bool, Bool) | t();",
									  "data Prop = or(Prop, Prop) | f();"]);
 	
test bool matchListError1() = redeclaredVariable("list[int] x = [1,2,3]; [1, *int L, 2, *int L] := x;"); 
 	
test bool matchListErrorRedeclaredSpliceVar1() = redeclaredVariable("list[int] x = [1,2,3];[1, * int L, * int L] := x;"); 
  
test bool matchListError22() = checkOK("list[int] l = [1,2,3]; [1, list[str] L, 2] := l; ");
  
test bool matchBoolIntError1() = cannotMatch("true !:= 1;"); 

test bool matchBoolIntError2() = cannotMatch("!(1 := true);"); 

test bool noMatchBoolIntError1() = cannotMatch("true !:= 1;"); 
  
test bool noMatchBoolIntError2() = cannotMatch("1 !:= true;");  	
  
test bool matchStringBoolError11() = cannotMatch("\"abc\" := true;");  
 
test bool matchStringBoolError21() = cannotMatch("true := \"abc\";");  
  	
test bool noMatchStringBoolError11() = cannotMatch("\"abc\"  !:= true;");  

test bool noMatchStringBoolError21() = cannotMatch("true !:= \"abc\";");  

test bool matchStringIntError11() = cannotMatch("\"abc\" !:= 1;");  

test bool matchStringIntError2() = cannotMatch("1 !:= \"abc\";");  
  	
test bool noMatchStringIntError1() = cannotMatch("\"abc\"!:=1;");  
  	
test bool noMatchStringIntError2() = cannotMatch("1 !:= \"abc\";");   
 
test bool matchStringRealError1() = cannotMatch("\"abc\" := 1.5;");  
  	
test bool matchStringRealError2() = cannotMatch("1.5 !:= \"abc\";");  
  	
test bool noMatchStringRealError1() = cannotMatch("\"abc\"  !:= 1.5;"); 
  	
test bool noMatchStringRealError2() = cannotMatch("1.5 !:= \"abc\";");  
  	
test bool matchIntRealError1() = cannotMatch("!(2 := 1.5);");  
 
test bool matchIntRealError2() = cannotMatch("!(1.5 := 2);");  
  	
test bool noMatchIntRealError1() = cannotMatch("2  !:= 1.5;"); 
  	
test bool noMatchIntRealError2() = cannotMatch("1.5 !:= 2;"); 
 	
test bool errorRedclaredVariable1() = redeclaredVariable("{1, *int L, 2, *int L} := {1,2,3};"); 
  	
test bool matchSetWrongElemError1() = checkOK("{1, \"a\", 2, *set[int] L} := {1,2,3};");
  		
test bool matchSetWrongElemError2() = checkOK("{1, set[str] L, 2} := {1,2,3};");

test bool matchSetWrongElemError3() = checkOK("{1, str S, 2} := {1,2,3};");
 
test bool matchSetWrongElemError4() = cannotMatch("set[str] S = {\"a\"}; {1, S, 2} := {1,2,3};"); 

test bool matchSetErrorRedeclaredSpliceVar() = redeclaredVariable("set[int] x = {1,2,3}; {1, * int L, * int L} := x;"); 

test bool UndeclaredTypeError1() = undeclaredType( "STRANGE X := 123;");
 
test bool antiPatternDoesNotDeclare1() = undeclaredVariable("![1,int X,3] := [1,2,4] && X == 2;" );

test bool matchADTStringError11() =                                                              
	cannotMatch("f(1) := \"abc\";", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]);
	  	
test bool matchADTStringError21() = 
	cannotMatch("\"abc\" := f(1);", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]);  	
 
test bool noMatchADTStringError11() =                                                            
	cannotMatch("f(1) !:= \"abc\";", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]); 
	 	
test bool noMatchADTStringError21() = 
	cannotMatch("\"abc\" !:= f(1);", initialDecls=["data Bool = and(Bool, Bool) | t();",
														   "data Prop = or(Prop, Prop) | f(int n);"]); 

test bool matchTupleStringError() = cannotMatch("\<1\> := \"a\";");
 	
test bool matchTupleArityError() = cannotMatch("!(\<1,2\> := \<1\>);");

test bool noMatchTupleArityError() = cannotMatch("\<1\> !:= \<1,2\>;");  	
 
test bool matchSetStringError() = cannotMatch("{1} := \"a\";");  
  
test bool matchListError1() = checkOK("list[int] x = [1,2,3]; [1, *list[int] L, 2, list[int] M] !:= x;");   // DISCUSS, was: cannotMatch	
	
test bool matchListError2() = unexpectedDeclaration("!([1, list[int] L, 2, list[int] L] := [1,2,3]);");  
  	
test bool matchListError3() = checkOK("!([1, list[str] L, 2] := [1,2,3]);");  // DISCUSS, was: cannotMatch
 
test bool matchListError4() = cannotMatch("str S = \"a\";  [1, S, 2] !:= [1,2,3];");  
   	
test bool matchListError5() = checkOK("list[int] x = [1,2,3] ; [1, str S, 2] := x;");  // DISCUSS, was: cannotMatch
  	
test bool matchListError6() = cannotMatch("str S = \"a\"; [1, S, 2] !:= [1,2,3];");  
  	
test bool matchListError7() = cannotMatch("str S = \"a\"; list[int] x = [1,2,3]; [1, S, 2] := x;");  
  	
test bool matchListError8() = cannotMatch("list[str] S = [\"a\"];  [1, S, 2] !:= [1,2,3];");  
  	
test bool matchListError9() = cannotMatch("list[str] S = [\"a\"]; list[int] x = [1,2,3]; [1, S, 2] := x;");  
  	
//test bool recursiveDataTypeNoPossibleHiddenRecursion() = 
//	cannotMatch("p = or(t,t); and(t,t) := p;", initialDecls=["data Prop = f();", "data Bool = and(list[Prop], list[Prop]) | t();"]);  
  

test bool NoDataDecl() = 
	cannotMatch("f(1) := 1;", initialDecls=["data Prop = f();", "data Bool = and(list[Prop], list[Prop]) | t();"]);  

@ignore{The following test requires deeper analysis of the data signature}
test bool descendantWrongType() = 
	undeclaredVariable("/true := f(g(1),f(g(2),g(3)));", initialDecls=["data F = f(F left, F right) | g(int N);"]);  

test bool recursiveDataTypeNoPossibleMatchVertical() = 
	undeclaredVariable("T := and(T,T);", initialDecls=["data Bool = and(Bool, Bool) | t();"]);  
  
test bool typedVariableBecomesWrongType() = cannotMatch("str N : 3 := 3;");  
  	
test bool redeclaredTypedVariableBecomesShadowsAnother() = redeclaredVariable("int N = 5; int N : 3 := 3 && N == 3;");  
  	
test bool doubleTypedVariableBecomes() = redeclaredVariable("[int N : 3, int N : 4] := [3,4] && N == 3;");  
  	
test bool matchListExternalVar1() = checkOK("list[int] S; [1, *S, 2] !:= [1,2,3] && S != [3];");

test bool listExpressions1() = unexpectedType("value n = 1; list[int] l = [ *[n, n] ];");  

test bool listExpressions2() = unexpectedType("value n = 1; list[int] l = [ 1, *[n, n], 2 ];");  

test bool setExpressions1() = unexpectedType("value n = 1; set[int] l = { *[n, n] };");  

test bool setExpressions2() = unexpectedType("value n = 1; set[int] l = { 1, *[n, n], 2 };");  		
  	
 test bool unsupportedSplicePatternList1(){
    return unsupported("[*[1,2]] := [1,2];");
 }
 
 test bool unsupportedSplicePatternList2(){
    return unsupported("[*[_]] := [1];");
 }
 
  test bool unsupportedSplicePatternSet1(){
    return unsupported("{*{1,2}} := {1,2};");
 }
 
 test bool unsupportedSplicePatternSet2(){
    return unsupported("{*{_}} := {1};");
 }
 
 list[str] ovlConstructors =
    [ "syntax A = conditional: A;",
      "data B = conditional(B symbol);",
      "data C = conditional(C,C);"
    ];

test bool overloadedConstructorAmbiguous()
    = unexpectedType("B removeConditionals(B sym) = visit(sym) {
                     'case conditional(s) =\> s
                     '};",
                     initialDecls = ovlConstructors);
                     
test bool overloadedConstructorOk1()
    = checkOK("B removeConditionals(B sym) = visit(sym) {
              'case conditional(A s) =\> s
              '};",
              initialDecls = ovlConstructors);
                     
test bool overloadedConstructorOk2()
    = checkOK("B removeConditionals(B sym) = visit(sym) {
              'case conditional(B s) =\> s
              '};",
              initialDecls = ovlConstructors);
                  
test bool overloadedConstructorOk3()
    = checkOK("B removeConditionals(B sym) = visit(sym) {
              'case conditional(s,_) =\> s
              '};",
              initialDecls = ovlConstructors);
////////////////////////////

test bool P1() = checkOK("1 := 1;");
test bool P2() = unexpectedType("1 := true;");

test bool P3() = checkOK("\"a\" := \"b\";");
test bool P4() = checkOK("1.5 := 1.6;");
test bool P5() = checkOK("2r3 := 2r4;");

test bool Lst1() = checkOK("[] := [1];");
test bool Lst2() = checkOK("[1] := [1];");
test bool Lst3() = checkOK("[1,2] := [1];");
test bool Lst4() = checkOK("[1,2] := [1, 1.5];");
test bool Lst5() = checkOK("[1,2.5] := [1, 2];");
test bool Lst6() = unexpectedType("[1] := [\"a\"];");
test bool Lst7() = checkOK("[x] := [\"a\"];");
test bool Lst8() = unexpectedType("m && x == \"a\";"); 
test bool Lst9() = unexpectedType("{ x = 1; [x] := [\"a\"] && x == \"a\";};");
test bool Lst10() = checkOK("{ x = \"a\"; [x] := [\"a\"] && x == \"a\";};"); 

test bool Set1() = checkOK("{} := {1};");
test bool Set2() = checkOK("{1} := {1};");
test bool Set3() = checkOK("{1,2} := {1};");
test bool Set4() = checkOK("{1,2} := {1, 1.5};");
test bool Set5() = checkOK("{1,2.5} := {1, 2};");
test bool Set6() = unexpectedType("{1} := {\"a\"};");
test bool Set7() = checkOK("{x} := {\"a\"};");
test bool Set8() = checkOK("{x} := {\"a\"} && x == \"a\";");
test bool Set9() = unexpectedType("{ x = 1; {x} := {\"a\"} && x == \"a\";};");
test bool Set10() = checkOK("{ x = \"a\"; {x} := {\"a\"} && x == \"a\";};"); 

test bool Tup1() = checkOK("\<1\> := \<1\>;");
test bool Tup2() = unexpectedType("\<1\> := \<\"a\"\>;");
test bool Tup3() = checkOK("\<1, \"a\"\> := \<2, \"b\"\>;");
test bool Tup4() = cannotMatch("\<1\> := \<2, \"b\"\>;");
test bool Tup5() = cannotMatch("\<1\> := \<\"a\"\>;");
test bool Tup6() = checkOK("\<x\> := \<\"a\"\>;");
test bool Tup7() = checkOK("\<x\> := \<\"a\"\> && x == \"a\";");
test bool Tup8() = cannotMatch("{ x = 1; \<x\> := \<\"a\"\> && x == \"a\";};");
test bool Tup9() = checkOK("{ x = \"a\"; \<x\> := \<\"a\"\> && x == \"a\";};"); 

test bool Var1() = checkOK("int x := 1;");
test bool Var2() = checkOK("int x := 1 && x == 1;");
test bool Var3() = unexpectedType("int x := 1 && x == \"a\";");

test bool If1() = checkOK("{ if(int x := 1) x + 1; };");
test bool If2() = unexpectedType("{ if(int x := 1) x + 1; else x + 2; };");
test bool If3() = checkOK("{ if(int x := 1 && x == 1 ) x + 1; };");
test bool If4() = unexpectedDeclaration("{ if(int x := 1 && int x := 2 && x == 1 ) x + 1; };");

test bool If5() = checkOK("{ if(int x := 1 && int y := 2 && x == 1 ) x + y; };");
test bool If5() = unexpectedType("{ if(int x := 1 && int y := 2 && x == 1 ) x + y; else y;};");

test bool IfU1() = checkOK("{ if(x := 1) x + 1; };");
test bool IfU2() = unexpectedType("{ if(x := 1) x + 1; else x + 2;};");
test bool IfU3() = checkOK("{ if(x := 1 && x == 1 ) x + 1; };");

test bool ADT1() = checkOK("{ if(d1(x) := d1(10)) x + 1; };", initialDecls=["data D = d1(int n);"]);
test bool ADT2() = checkOK("{ if(d1(x) := d1(10) && d1(y) := d1(11)) x + y; };", initialDecls=["data D = d1(int n);"]);

////////////////////////////

test bool U1() = checkOK("[_] := [1];");
test bool U2() = checkOK("[_*] := [1];");
test bool U3() = checkOK("[*_] := [1];");

test bool TU1() = checkOK("[int _] := [1];");
test bool TU2() = checkOK("[*int _] := [1];");

test bool TN1() = checkOK("[int X] := [1];");
test bool TN2() = checkOK("[*int X] := [1];");

test bool U1() = checkOK("[X] := [1];");
test bool U2() = checkOK("[X*] := [1];");
test bool U3() = checkOK("[*X] := [1];");

test bool S1() = checkOK("[*X,*_] := [1];");

// https://github.com/cwi-swat/rascal/issues/458

test bool Issue458a() =
	checkOK("\"f1\"(1, M=10) := \"f1\"(1, M=10);");

test bool Issue458b() = checkModuleOK("
   module  Issue458b  
      data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);
	   bool main() = f1(1, M=X) := f1(1, B=false, M=20) && X == 20;
   ");
		
test bool Issue458c() = checkModuleOK("
   module Issue458c
      data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);
      bool main() = \"f1\"(1, M=X) := \"f1\"(1, B=false, M=20) && X == 20;
   ");

// https://github.com/cwi-swat/rascal/issues/471

test bool Issue471a() = checkModuleOK("
   module Issue471a
      data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
	   bool main() = ([A1, f([A1, b(), DATA X8])] := [a(), f([a(),b(),c()])]) && (A1 == a());
   ");
					
test bool Issue471b() = checkModuleOK("
   module Issue471b
      data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
      bool main() = ([f([A1, b(), DATA X8]), A1] := [f([a(),b(),c()]), a()]) && (A1 == a());
   ");
					
test bool Issue471c() = checkModuleOK("
   module Issue471c
      data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
	   bool main() = ([DATA A2, f([A2, b(), *DATA SX1]), *SX1] := [a(), f([a(),b(),c()]), c()]) && (A2 == a()) && (SX1 ==[c()]); 
	");		

test bool Issue471d() = checkModuleOK("
   module Issue471d
      data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
	   bool main() = ([DATA A3, f([A3, b(), *DATA SX2]), *SX2] !:= [d(), f([a(),b(),c()]), a()]);
   ");
					

test bool Issue471e() = checkModuleOK("
   module Issue471e
      data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);

	   bool main() = ([DATA A4, f([A4, b(), *DATA SX3]), *SX3] !:= [c(), f([a(),b(),c()]), d()]);
   ");
					
test bool Issue471f() = checkModuleOK("
   module Issue471f
      data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);
      bool main() = f(_) := f(1);
   ");
					
test bool Issue471g() = checkModuleOK("
   module Issue471g
      data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);
      bool main() = f(_,_):= f(1,2);
   ");
					
test bool Issue471h() = checkModuleOK("
   module Issue471h
      data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);
      bool main() = f(_,_,_):= f(1,2.5,true);
   ");
					
 test bool Issue471i() = checkModuleOK("
   module Issue471i
      data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);
      bool main() = (f(n5) := f(1)) && (n5 == 1);
   ");
					
 test bool Issue471j() = checkModuleOK("
   module Issue471j
      data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
      bool main() = ({e(X3), g(X3), h(X3)} := {e(3), h(3), g(3)}) && (X3 == 3);
   "); 

// https://github.com/cwi-swat/rascal/issues/472

test bool Issue472a() = checkModuleOK("
   module Issue472a    
      data F = f(F left, F right) | g(int N);
      bool main() = [1, /f(/g(2), _), 3] := [1, f(g(1),f(g(2),g(3))), 3];
   ");

test bool Issue472b() = checkModuleOK("
   module Issue472b
      data F = f(F left, F right) | g(int N);
      bool main() = [1, F outer: /f(/F inner: g(2), _), 3] := [1, f(g(1),f(g(2),g(3))), 3] && outer == f(g(1),f(g(2),g(3))) && inner == g(2);
   ");

// https://github.com/cwi-swat/rascal/issues/473

test bool Issue473() =
 	unexpectedType("[ \<s,r,L\> | list[int] L:[*str s, *str r] \<- [ [1,2], [\"3\",\"4\"] ]];");

// https://github.com/cwi-swat/rascal/issues/478

test bool Issue478() = checkModuleOK("
   module Issue478
	   data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);
 		public value my_main() = f1(1, M=10)  := f1(1);
   ");

test bool Issue886() = unexpectedType("[\<[\<19,0,*_\>],false,_\>] := [\<[\<19,0,1\>], true, 1\>];");