@bootstrapParser
module lang::rascalcore::check::tests::PatternTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

import ParseTree;

test bool matchNestedList() = cannotMatch("[[1]] := [];");

test bool matchNestedSet() = cannotMatch("{{1}} := {};");

data Bool = and(Bool, Bool) | t();
data Prop = or(Prop, Prop) | f();

test bool cannotMatchListStr1() = cannotMatch("[1] := \"a\";");

test bool unguardedMatchNoEscape1() = undeclaredVariable("int n = 3; int m := n; m == n;");

test bool recursiveDataTypeNoPossibleMatchHorizontal1() = 
	cannotMatch("Prop p := and(t(),t());", initialDecls=["data Bool = and(Bool, Bool) | t();",
														   "data Prop = or(Prop, Prop) | f();"]);
  	
test bool matchListError1() = redeclaredVariable("list[int] x = [1,2,3]; [1, *int L, 2, *int L] := x;"); 
  	
test bool matchListErrorRedeclaredSpliceVar1() = redeclaredVariable("list[int] x = [1,2,3];[1, * int L, * int L] := x;"); 
  
test bool matchListError22() = cannotMatch("list[int] l = [1,2,3]; [1, list[str] L, 2] := l; "); 
  
test bool matchBoolIntError1() = cannotMatch("true !:= 1;"); 

test bool matchBoolIntError1() = cannotMatch("!(1 := true);"); 

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
  	
test bool matchSetWrongElemError1() = cannotMatch("{1, \"a\", 2, *set[int] L} := {1,2,3};");
  		
test bool matchSetWrongElemError2() = cannotMatch("{1, set[str] L, 2} := {1,2,3};"); 

test bool matchSetWrongElemError3() = cannotMatch("{1, str S, 2} := {1,2,3};"); 
 
test bool matchSetWrongElemError4() = cannotMatch("set[str] S = {\"a\"}; {1, S, 2} := {1,2,3};"); 
  
test bool matchSetErrorRedeclaredSpliceVar() = redeclaredVariable("set[int] x = {1,2,3}; {1, * int L, * int L} := x;"); 
  
test bool UndeclaredTypeError1() = cannotMatch( "STRANGE X := 123;");  // rename to UndefinedType?
 
test bool antiPatternDoesNotDeclare1() = undeclaredVariable("![1,int X,3] := [1,2,4] && X == 2;" ); // TODO

test bool matchADTStringError11() =                                                              // TODO
	cannotMatch("f(1) := \"abc\";", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]);
	  	
test bool matchADTStringError21() = 
	cannotMatch("\"abc\" := f(1);", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]);  	
 
test bool noMatchADTStringError11() =                                                            // TODO
	cannotMatch("f(1) !:= \"abc\";", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]); 
	 	
test bool noMatchADTStringError21() = 
	cannotMatch("\"abc\" !:= f(1);", initialDecls=["data Bool = and(Bool, Bool) | t();",
														   "data Prop = or(Prop, Prop) | f(int n);"]); 

test bool matchTupleStringError() = cannotMatch("\<1\> := \"a\";");
 	
test bool matchTupleArityError() = cannotMatch("!(\<1,2\> := \<1\>);");

test bool noMatchTupleArityError() = cannotMatch("\<1\> !:= \<1,2\>;");  	
 
test bool matchSetStringError() = cannotMatch("{1} := \"a\";");  
  
test bool matchListError1() = cannotMatch("list[int] x = [1,2,3]; [1, *list[int] L, 2, list[int] M] !:= x;");   	
  	
test bool matchListError2() = cannotMatch("!([1, list[int] L, 2, list[int] L] := [1,2,3]);");  
  	
test bool matchListError4() = cannotMatch("!([1, list[str] L, 2] := [1,2,3]);");  
 
test bool matchListError5() = cannotMatch("str S = \"a\";  [1, S, 2] !:= [1,2,3];");  
   	
test bool matchListError3() = cannotMatch("list[int] x = [1,2,3] ; [1, str S, 2] := x;");  
  	
test bool matchListError5() = cannotMatch("str S = \"a\"; [1, S, 2] !:= [1,2,3];");  
  	
test bool matchListError42() = cannotMatch("str S = \"a\"; list[int] x = [1,2,3]; [1, S, 2] := x;");  
  	
test bool matchListError5() = cannotMatch("list[str] S = [\"a\"];  [1, S, 2] !:= [1,2,3];");  
  	
test bool matchListError55() = cannotMatch("list[str] S = [\"a\"]; list[int] x = [1,2,3]; [1, S, 2] := x;");  
  	
//test bool recursiveDataTypeNoPossibleHiddenRecursion() = 
//	cannotMatch("p = or(t,t); and(t,t) := p;", initialDecls=["data Prop = f();", "data Bool = and(list[Prop], list[Prop]) | t();"]);  
  

test bool NoDataDecl() = 
	undeclaredVariable("f(1) := 1;", initialDecls=["data Prop = f();", "data Bool = and(list[Prop], list[Prop]) | t();"]);  

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

test bool PicoQuoted1() = 
	checkOK("Program program := t1;",
			initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);

test bool PicoQuoted2() = 
	checkOK("(Program) `\<Program program\>` := t1;",
			initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);
  

test bool PicoQuoted2() = 
	checkOK(" (Program) `begin \<Declarations decls\> \<{Statement \";\"}* stats\> end` := t1; ",
			initialDecls = ["Tree t1 = (Program) `begin declare x: natural; x := 10 end`;"],
			importedModules = ["ParseTree",  "lang::pico::\\syntax::Main"]);
		
  	
  	
  		