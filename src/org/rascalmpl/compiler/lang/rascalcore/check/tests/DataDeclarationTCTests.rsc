@bootstrapParser
module lang::rascalcore::check::tests::DataDeclarationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool undefinedValue1() = 
	uninitialized("Bool b; b.left;", initialDecls=["data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);"]);  

 
test bool unitializedVariable1() = 
	uninitialized("Bool b; b[left = btrue()];", 
				  initialDecls=["data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);"]);  
  	
test bool unitializedVariable2() = 
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

@ignore{Discuss: do we realy want this?}	
test bool exactDoubleDataDeclarationIsAllowed() = 
	checkOK("true;", initialDecls=["data D = d(int n) | e();", "data D = d(int n);"]);  // TODO: it seems that we allow exact redeclaration
  	
test bool undeclaredTypeError1() = 
	declarationError("true;", initialDecls=["data D = anE(E e);"]);                    // TODO E is not declared
	
test bool sharedKeywordsWork1() =
    checkOK("xx().ll == 0;", initialDecls=["data Y(int ll = 0) = xx();"]);
  	
test bool sharedKeywordsWork2() =
    checkOK("xx().ll == 0;", initialDecls=["data Y(int ll = 0);", "data Y = xx();"]);
  
 