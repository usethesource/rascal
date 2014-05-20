module tests::functionality::AnnotationTCTests

import StaticTestingUtils;
 
public test bool annotationNotAllowed1() = unexpectedType("1 [@an=3];");

public test bool annotationNotAllowed2() = unexpectedType("1 @ ann;");

public test bool annotationNotAllowed31() = unexpectedType("f()[@pos=true];", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]); 
  	
public test bool annotationNotAllowed41() = unexpectedType("f() [@wrongpos=true];", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]); 
 
public test bool UndefinedValueError11() = uninitialized("F someF; someF @ pos;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]); 
 
public test bool UndefinedValueError21() = uninitialized("F someF; someF [@pos=3];", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]); 
  	
public test bool UninitializedVariableError1() = uninitialized("F someF; someF @ pos = 3;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F @ pos;"]); 
  	