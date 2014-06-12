module lang::rascal::tests::types::DataDeclarationTCTests

import lang::rascal::tests::types::StaticTestingUtils;

test bool UndefinedValue1() = 
	uninitialized("Bool b; b.left;", initialDecls=["data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);"]);  

 
test bool UnitializedVariable21() = 
	uninitialized("Bool b; b[left = btrue()];", 
				  initialDecls=["data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);"]);  
  	
test bool UnitializedVariable21() = 
	uninitialized("Bool b; b[left = btrue()];", 
				  initialDecls=["data Exp = let(str name, Exp exp1, Exp exp2) | var(str name) | \\int(int intVal);",
								"data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);",
								"alias Var2 = str;"]); 
															  
test bool letWrongTypeViaAlias() =
	unexpectedType("Var2 varx !:= let(\"a\",\\int(1),var(\"a\"));", 
				    initialDecls=["alias Var2 = str;", 
				    			  "data Exp2 = let(Var2 var, Exp2 exp1, Exp2 exp2) | var(Var2 var) | \\int(int intVal);"]); 
														 
  
test bool doubleFieldError2() = 
	declarationError("true;", initialDecls=["data D = d(int n) | d(value v);"]);   
  	

test bool doubleFieldError3() = 
	declarationError("true;", initialDecls=["data D = d(int n) | d(int v);"]); 
	  	
  	
test bool doubleFieldError4() = 
	declarationError("true;", initialDecls=["alias INTEGER = int;", "data D = d(int n) | d(INTEGER v);"]); 
	
test bool exactDoubleDataDeclarationIsAllowed() = 
	checkOK("true;", initialDecls=["data D = d(int n) | e();", "data D = d(int n);"]);  // TODO: it seems that we allow exact redeclaration
  	
test bool undeclaredTypeError1() = 
	declarationError("true;", initialDecls=["data D = anE(E e);"]);                    // TODO E is not declared
  	
  
 