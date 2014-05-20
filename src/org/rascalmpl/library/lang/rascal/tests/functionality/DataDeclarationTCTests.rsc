module tests::functionality::DataDeclarationTCTests

import StaticTestingUtils;

public test bool UndefinedValue1() = 
	uninitialized("Bool b; b.left;", initialDecls=["data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);"]);  

 
public test bool UnitializedVariable21() = 
	uninitialized("Bool b; b[left = btrue()];", initialDecls=["data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);"]);  
  	
public test bool UnitializedVariable21() = 
	uninitialized("Bool b; b[left = btrue()];", initialDecls=["data Exp = let(str name, Exp exp1, Exp exp2) | var(str name) | \\int(int intVal);",
															  "data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);",
															  "alias Var2 = str;"]);  
  
public test bool doubleFieldError2() = 
	declarationError("true;", initialDecls=["data D = d(int n) | d(value v);"]);   
  	

public test bool doubleFieldError3() = 
	declarationError("true;", initialDecls=["data D = d(int n) | d(int v);"]); 
	  	
  	
public test bool doubleFieldError4() = 
	declarationError("true;", initialDecls=["alias INTEGER = int;", "data D = d(int n) | d(INTEGER v);"]); 
	
public test bool exactDoubleDataDeclarationIsAllowed() = 
	checkOK("true;", initialDecls=["data D = d(int n) | e();", "data D = d(int n);"]);  // TODO: it seems that we allow exact redeclaration
  	
public test bool undeclaredTypeError1() = 
	declarationError("true;", initialDecls=["data D = anE(E e);"]);                    // TODO E is not declared
  	
  
 