@bootstrapParser
module lang::rascalcore::check::tests::ComprehensionTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool nonVoidListComprehension() = nonVoidType("[f() | _ := 1];", initialDecls=["void f() { return; }"]);

test bool nonVoidSetComprehension() = nonVoidType("{f() | _ := 1};", initialDecls=["void f() { return; }"]);

test bool testGen1() = unexpectedType("{x | 5};");

test bool testVoidFunctionPredicate1() = unexpectedType("void f() { } { x | int x \<- {1,2,3}, f() };");
  	
test bool testUndefinedValue1() = undeclaredVariable("{ y | int x \<- {1,2,3}};");

test bool WrongListType() = cannotMatch("str S \<- [1,2,3];");

test bool WrongSetType() = cannotMatch("str S \<- {1,2,3};");

test bool WrongMapType() = cannotMatch("str S \<- (1:10,2:20);");

test bool WrongStringType() = cannotMatch(" int N \<- \"abc\";");

test bool WrongADTType1() = cannotMatch("int N \<- [true, true, false];", initialDecls=["data Bool = btrue() | bfalse() | band(Bool lhs, Bool rhs) | bor(Bool lhs, Bool rhs);"]);  

test bool nodeGenerator() = checkOK("[N | int N \<- f(i(1),g(i(2),i(3)))];", initialDecls=["data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);"]);  // TODO
  
test bool anyError() = unexpectedType("any(x \<- [1,2,3], \"abc\");");

test bool allError() = unexpectedType("all(x \<- [1,2,3], \"abc\");");
  
test bool noLeaking() = undeclaredVariable("{ X | int X \<- [1,2,3] }; X == 3;");

test bool NoLeakFromNextGenerator1() = undeclaredVariable("[\<N,M\> | int N \<- [1 .. 3], ((N==1) ? true : M \> 0), int M \<- [10 .. 12]] == [\<1,10\>,\<1,11\>,\<2,10\>,\<2,11\>];");  	
 
test bool NoLeakFromNextGenerator2() = undeclaredVariable(" [\<N,M\> | int N \<- [1 .. 3], ((N==1) ? true : M \> 0), int M := N] == [\<1,1\>,\<2,2\>];");  	

test bool emptyTupleGeneratorError1() = checkOK("{\<X,Y\> | \<int X, int Y\> \<- {}} == {};");  	
  	
test bool emptyTupleGeneratorError2() = checkOK("{\<X,Y\> | \<int X, int Y\> \<- []} == {};");  	
  
test bool emptyTupleGeneratorError3() = checkOK("{\<X,Y\> | int X \<- {}, int Y \<- {}} == {};");	
   
test bool emptyTupleGeneratorError4() = checkOK("{\<X,Y\> | int X \<- [], int Y \<- []} == {};");	