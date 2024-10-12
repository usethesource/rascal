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

test bool exactDoubleDataDeclarationIsNotAllowed() = 
	declarationError("true;", initialDecls=["data D = d(int n) | e();", "data D = d(int n);"]);

test bool undeclaredTypeError1() = 
	declarationError("true;", initialDecls=["data D = anE(E e);"]);                    // TODO E is not declared
	
test bool sharedKeywordsWork1() =
    checkOK("xx().ll == 0;", initialDecls=["data Y(int ll = 0) = xx();"]);
  	
test bool sharedKeywordsWork2() =
    checkOK("xx().ll == 0;", initialDecls=["data Y(int ll = 0);", "data Y = xx();"]);

test bool D1() = unexpectedType("D x;");

test bool D2() = checkOK("D x;", initialDecls=["data D;"]);

test bool D3() = checkOK("D x;", initialDecls=["data D = d1();"]);

test bool D4() = checkOK("D x = d1();", initialDecls=["data D = d1();"]);

test bool D5() = checkOK("D[int] x = d1(3);", initialDecls=["data D[&T] = d1(&T fld);"]);

test bool D6() = unexpectedType("D x = d1(3);", initialDecls=["data D[&T] = d1(&T fld);"]);

test bool D7() = unexpectedType("D[int,str] x = d1(3);", initialDecls=["data D[&T] = d1(&T fld);"]);

test bool D8() = unexpectedType("D[&T] x = d1();", initialDecls=["data D = d1();"]);

test bool D9() = unexpectedType("D[int] x = d1(3, \"a\");", initialDecls=["data D[&T, &U] = d1(&T fld1, &U fld2);"]);

test bool D10() {
	makeModule("A", "data D[&T] = d1(&T fld);");
	makeModule("B", "import A;
                    'data D[&T, &U] = d1(&T fld1, &U fld2);
                    'void main(){ D[int] x = d1(3, \"a\"); }");
	return unexpectedType("D[int] x = d1(3, \"a\"); ", imports = ["A", "B"]);
}

test bool K1() = checkOK("d1(1, l=[2]);", initialDecls=["data D[&T] = d1(&T n, list[&T] l = [n]);"]);

test bool K2() = argumentMismatch("d1(1, l=[\"a\"]); ", initialDecls=["data D[&T] = d1(&T n, list[&T] l = [n]);"]);

test bool K3() = checkOK("d1(1, l=[2]);", initialDecls=["data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);"]);

test bool K4() = checkOK("d1(\"a\", \"b\");", initialDecls=["data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);"]);

test bool K5() = checkOK("D[str] x = d1(\"a\", \"b\");", initialDecls=["data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);"]);

test bool K6() = checkOK("d1(\"a\", 3);", initialDecls=["data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);"]);

test bool K7() = checkOK("D[int] x = d1(\"a\", 3);", initialDecls=["data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);"]);

test bool P1() = cannotMatch("d1(1) := d1(1); ", initialDecls=["data D[&T] = d1(str s, &T x) | d1(&T n, list[&T] l = [n]);"]);

test bool P2() = unexpectedType("d1(1, l=[\"a\"]) := d1(1);", initialDecls=["data D[&T] = d1(str s, &T x) | d1(&T n, list[&T] l = [n]);"]);

test bool P3() = unexpectedType("d1(\"a\", 3) := d1(1);", initialDecls=["data D[&T] = d1(str s, &T x) | d1(&T n, list[&T] l = [n]);"]);

test bool P4() = cannotMatch("d1(\"a\", \"b\") := d1(1);", initialDecls=["data D[&T] = d1(str s, &T x) | d1(&T n, list[&T] l = [n]);"]);