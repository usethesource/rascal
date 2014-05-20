module tests::functionality::ComprehensionTCTests

import StaticTestingUtils;

public test bool testGen1() = unexpectedType("{x | 5};");

public test bool testVoidFunctionPredicate1() = unexpectedType("void f() { } { x | int x \<- {1,2,3}, f() };");
  	
public test bool testUndefinedValue1() = undeclaredVariable("{ y | int x \<- {1,2,3}};");

public test bool WrongListType() = cannotMatch("str S \<- [1,2,3];");

public test bool WrongSetType() = cannotMatch("str S \<- {1,2,3};");

public test bool WrongMapType() = cannotMatch("str S \<- (1:10,2:20);");

public test bool WrongStringType() = cannotMatch(" int N \<- \"abc\";");

public test bool WrongADTType1() = cannotMatch("int N \<- [true, true, false];", initialDecls=["data Bool = btrue() | bfalse() | band(Bool lhs, Bool rhs) | bor(Bool lhs, Bool rhs);"]);  
  
public test bool nodeGeneratorTypeError() = unexpectedType("[N | int N \<- f(i(1),g(i(2),i(3)))];", initialDecls=["data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);"]);  // TODO
  
public test bool anyError() = unexpectedType("any(x \<- [1,2,3], \"abc\");");

public test bool allError() = unexpectedType("all(x \<- [1,2,3], \"abc\");");
  
public test bool noLeaking() = undeclaredVariable("{ X | int X \<- [1,2,3] }; X == 3;");
 
public test bool NoLeakFromNextGenerator1() = undeclaredVariable("[\<N,M\> | int N \<- [1 .. 3], ((N==1) ? true : M \> 0), int M \<- [10 .. 12]] == [\<1,10\>,\<1,11\>,\<2,10\>\<2,11\>];");  	
  
public test bool NoLeakFromNextGenerator2() = undeclaredVariable(" [\<N,M\> | int N \<- [1 .. 3], ((N==1) ? true : M \> 0), int M := N] == [\<1,1\>,\<2,2\>];");  	

public test bool emptyTupleGeneratorError1() = cannotMatch("{\<X,Y\> | \<int X, int Y\> \<- {}} == {};");  	
  	
public test bool emptyTupleGeneratorError2() = cannotMatch("{\<X,Y\> | \<int X, int Y\> \<- []} == {};");  	
  
public test bool emptyTupleGeneratorError3() = cannotMatch("{\<X,Y\> | int X \<- {}, int Y \<- {}} == {};");  // TODO:?	
   
public test bool emptyTupleGeneratorError4() = cannotMatch("{\<X,Y\> | int X \<- [], int Y \<- []} == {};");  // TODO:?	