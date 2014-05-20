module tests::functionality::PatternTCTests

import StaticTestingUtils;

public test bool matchNestedList2() = !([[1]] := []);

public test bool matchNestedSet2() = !({{1}} := {});

data Bool = and(Bool, Bool) | t();
data Prop = or(Prop, Prop) | f();

public test bool cannotMatchListStr1() = cannotMatch("[1] := \"a\";");

public test bool unguardedMatchNoEscape1() = undeclaredVariable("int n = 3; int m := n; m == n;");

public test bool recursiveDataTypeNoPossibleMatchHorizontal1() = 
	cannotMatch("Prop p := and(t(),t());", initialDecls=["data Bool = and(Bool, Bool) | t();",
														   "data Prop = or(Prop, Prop) | f();"]);
  	
public test bool matchListError1() = redeclaredVariable("list[int] x = [1,2,3]; [1, *int L, 2, *int L] := x;"); 
  	
public test bool matchListErrorRedeclaredSpliceVar1() = redeclaredVariable("list[int] x = [1,2,3];[1, * int L, * int L] := x;"); 
  
public test bool matchListError22() = cannotMatch("list[int] l = [1,2,3]; [1, list[str] L, 2] := l; "); 
  
public test bool matchBoolIntError1() = cannotMatch("true !:= 1;"); 

public test bool matchBoolIntError1() = cannotMatch("!(1 := true);"); 

public test bool noMatchBoolIntError1() = cannotMatch("true !:= 1;"); 
  
public test bool noMatchBoolIntError2() = cannotMatch("1 !:= true;");  	
  
public test bool matchStringBoolError11() = cannotMatch("\"abc\" := true;");  
 
public test bool matchStringBoolError21() = cannotMatch("true := \"abc\";");  
  	
public test bool noMatchStringBoolError11() = cannotMatch("\"abc\"  !:= true;");  

public test bool noMatchStringBoolError21() = cannotMatch("true !:= \"abc\";");  

public test bool matchStringIntError11() = cannotMatch("\"abc\" !:= 1;");  

public test bool matchStringIntError2() = cannotMatch("1 !:= \"abc\";");  
  	
public test bool noMatchStringIntError1() = cannotMatch("\"abc\"!:=1;");  
  	
public test bool noMatchStringIntError2() = cannotMatch("1 !:= \"abc\";");   
 
public test bool matchStringRealError1() = cannotMatch("\"abc\" := 1.5;");  
  	
public test bool matchStringRealError2() = cannotMatch("1.5 !:= \"abc\";");  
  	
public test bool noMatchStringRealError1() = cannotMatch("\"abc\"  !:= 1.5;"); 
  	
public test bool noMatchStringRealError2() = cannotMatch("1.5 !:= \"abc\";");  
  	
public test bool matchIntRealError1() = cannotMatch("!(2 := 1.5);");  
 
public test bool matchIntRealError2() = cannotMatch("!(1.5 := 2);");  
  	
public test bool noMatchIntRealError1() = cannotMatch("2  !:= 1.5;"); 
  	
public test bool noMatchIntRealError2() = cannotMatch("1.5 !:= 2;"); 
  	
public test bool errorRedclaredVariable1() = redeclaredVariable("{1, *int L, 2, *int L} := {1,2,3};"); 
  	
public test bool matchSetWrongElemError1() = cannotMatch("{1, \"a\", 2, *set[int] L} := {1,2,3};");
  		
public test bool matchSetWrongElemError2() = cannotMatch("{1, set[str] L, 2} := {1,2,3};"); 

public test bool matchSetWrongElemError3() = cannotMatch("{1, str S, 2} := {1,2,3};"); 
 
public test bool matchSetWrongElemError4() = cannotMatch("set[str] S = {\"a\"}; {1, S, 2} := {1,2,3};"); 
  
public test bool matchSetErrorRedeclaredSpliceVar() = redeclaredVariable("set[int] x = {1,2,3}; {1, * int L, * int L} := x;"); 
  
public test bool UndeclaredTypeError1() = cannotMatch( "STRANGE X := 123;");  // rename to UndefinedType?
 
public test bool antiPatternDoesNotDeclare1() = undeclaredVariable("![1,int X,3] := [1,2,4] && (X ? 10) == 10;" ); // TODO


public test bool matchADTStringError11() =                                                              // TODO
	cannotMatch("f(1) := \"abc\";", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]);
	  	
public test bool matchADTStringError21() = 
	cannotMatch("\"abc\" := f(1);", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]);  	
 
public test bool noMatchADTStringError11() =                                                            // TODO
	cannotMatch("f(1) !:= \"abc\";", initialDecls=["data Prop = or(Prop, Prop) | f(int n);"]); 
	 	
public test bool noMatchADTStringError21() = 
	cannotMatch("\"abc\" !:= f(1);", initialDecls=["data Bool = and(Bool, Bool) | t();",
														   "data Prop = or(Prop, Prop) | f(int n);"]); 

public test bool matchTupleStringError() = cannotMatch("\<1\> := \"a\";");
 	
public test bool matchTupleArityError() = cannotMatch("!(\<1,2\> := \<1\>);");

public test bool noMatchTupleArityError() = cannotMatch("\<1\> !:= \<1,2\>;");  	
 
public test bool matchSetStringError() = cannotMatch("{1} := \"a\";");  
  
public test bool matchListError1() = cannotMatch("list[int] x = [1,2,3]; [1, *list[int] L, 2, list[int] M] !:= x;");   	
  	
public test bool matchListError2() = cannotMatch("!([1, list[int] L, 2, list[int] L] := [1,2,3]);");  
  	
public test bool matchListError4() = cannotMatch("!([1, list[str] L, 2] := [1,2,3]);");  
 
public test bool matchListError5() = cannotMatch("str S = \"a\";  [1, S, 2] !:= [1,2,3];");  
   	
public test bool matchListError3() = cannotMatch("list[int] x = [1,2,3] ; [1, str S, 2] := x;");  
  	
public test bool matchListError5() = cannotMatch("str S = \"a\"; [1, S, 2] !:= [1,2,3];");  
  	
public test bool matchListError42() = cannotMatch("str S = \"a\"; list[int] x = [1,2,3]; [1, S, 2] := x;");  
  	
public test bool matchListError5() = cannotMatch("list[str] S = [\"a\"];  [1, S, 2] !:= [1,2,3];");  
  	
public test bool matchListError55() = cannotMatch("list[str] S = [\"a\"]; list[int] x = [1,2,3]; [1, S, 2] := x;");  
  	
//public test bool recursiveDataTypeNoPossibleHiddenRecursion() = 
//	cannotMatch("p = or(t,t); and(t,t) := p;", initialDecls=["data Prop = f();", "data Bool = and(list[Prop], list[Prop]) | t();"]);  
  

public test bool NoDataDecl() = 
	undeclaredVariable("f(1) := 1;", initialDecls=["data Prop = f();", "data Bool = and(list[Prop], list[Prop]) | t();"]);  

@ignore{The following test requires deeper analysis of the data signature}
public test bool descendantWrongType() = 
	undeclaredVariable("/true := f(g(1),f(g(2),g(3)));", initialDecls=["data F = f(F left, F right) | g(int N);"]);  
 
public test bool recursiveDataTypeNoPossibleMatchVertical() = 
	undeclaredVariable("T := and(T,T);", initialDecls=["data Bool = and(Bool, Bool) | t();"]);  
  
public test bool typedVariableBecomesWrongType() = cannotMatch("str N : 3 := 3;");  
  	
public test bool redeclaredTypedVariableBecomesShadowsAnother() = redeclaredVariable("int N = 5; int N : 3 := 3 && N == 3;");  
  	
public test bool doubleTypedVariableBecomes() = redeclaredVariable("[int N : 3, int N : 4] := [3,4] && N == 3;");  
  	
public test bool matchListExternalVar1() = checkOK("list[int] S; [1, *S, 2] !:= [1,2,3] && S != [3];");

public test bool listExpressions1() = unexpectedType("value n = 1; list[int] l = [ *[n, n] ];");  

public test bool listExpressions2() = unexpectedType("value n = 1; list[int] l = [ 1, *[n, n], 2 ];");  

public test bool setExpressions1() = unexpectedType("value n = 1; set[int] l = { *[n, n] };");  

public test bool setExpressions2() = unexpectedType("value n = 1; set[int] l = { 1, *[n, n], 2 };");  
 
  		