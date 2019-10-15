@bootstrapParser
module lang::rascalcore::check::tests::AnnotationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
 
test bool annotationNotAllowed1() = unexpectedType("1 [@an=3];");

test bool annotationNotAllowed2() = unexpectedType("1@ann;");

test bool annotationNotAllowed31() = unexpectedType("f()[@pos=true];", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F@pos;"]); 
  	
test bool annotationNotAllowed41() = unexpectedType("f() [@wrongpos=true];", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F@pos;"]); 
 
test bool UndefinedValueError11() = uninitialized("F someF; someF@pos;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F@pos;"]); 
 
test bool UndefinedValueError21() = uninitialized("F someF; someF [@pos=3];", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F@pos;"]); 
  	
test bool UninitializedVariableError1() = uninitialized("F someF; someF@pos = 3;", initialDecls=["data F = f() | f(int n) | g(int n) | deep(F f);", "anno int F@pos;"]); 
  	