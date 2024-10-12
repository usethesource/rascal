@bootstrapParser
module lang::rascalcore::check::tests::DeclarationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
 
test bool localTypeInferenceNoEscape2() = undeclaredVariable("{ x = 1; x == 1; } x;");

test bool undeclaredType1() = undeclaredType("X N;");            // TODO Type X undeclared

test bool doubleDeclaration3() = redeclaredVariable("int f(int N){int N = 1; return N;}");

test bool doubleDeclaration4() = redeclaredVariable("void main() {
    'int bar = 8;
    'bool bar() = true;
    'bar();
}");

test bool shadowingDeclaration1() = checkOK("int N = 1; {int N = 2;}; N == 1;");

test bool shadowingDeclaration2() = checkOK("N = 1; {int N = 2; N == 2;}; N == 1;");

test bool shadowingDeclaration4() = checkOK("int N = 3; int N := 3;");

// Variable declaration in imported module

test bool privateVarDeclarationNotVisible(){ 
	makeModule("MMM", "private int x = 3;"); 
	return undeclaredVariable("x;", importedModules=["MMM"]);
}

test bool publicVarDeclarationVisible(){ 
	makeModule("MMM", "public int x = 3;"); 
	return checkOK("x;", importedModules=["MMM"]);
}

test bool publicVarDeclarationVisibleViaQualifiedName(){ 
	makeModule("MMM", "public int x = 3;"); 
	return checkOK("MMM::x;", importedModules=["MMM"]);
}

test bool DefaultVarDeclarationNotVisible(){ 
	makeModule("MMM", "int x = 3;"); 
	return undeclaredVariable("x;", importedModules=["MMM"]);
}

test bool RedeclaredVarDeclaration(){
	makeModule("MMM", "public int x = 3;"); 
	return checkOK("int x = 4;", importedModules=["MMM"]);
}

// MAH: I will need to look for a good way to test this now; the configuration no
// longer includes errors detected in imported modules unless they are actual
// import errors, in this case the first n is in the imported configuration
// but the second n isn't since it would raise an error while checking M.
//test bool moduleRedeclarationError1(){ 
//	makeModule("MMM", "public int n = 1; public int n = 2;"); 
//	return redeclaredVariable("n == 1;", importedModules=["MMM"]);
//}

test bool qualifiedScopeTest(){ 
	makeModule("MMM", "public int n = 1;"); 
	return checkOK("MMM::n == 1;", importedModules=["MMM"]);
}

// Function declaration in imported module

test bool privateFunDeclarationNotVisible(){ 
	makeModule("MMM", "private int f() = 3;"); 
	return undeclaredVariable("f();", importedModules=["MMM"]);
}

test bool publicFunDeclarationVisible(){ 
	makeModule("MMM", "public int f() = 3;"); 
	return checkOK("f();", importedModules=["MMM"]);
}

test bool publicFunDeclarationVisibleViaQualifiedName(){ 
	makeModule("MMM", "public int f() = 3;"); 
	return checkOK("MMM::f();", importedModules=["MMM"]);
}

test bool DefaultFunDeclarationVisible(){ 
	makeModule("MMM", "int f() = 3;"); 
	return checkOK("f();", importedModules=["MMM"]);
}

// Non-terminal declaration in imported module

test bool NonTerminalVisible(){ 
	makeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A a;", importedModules=["MMM"]);
}

test bool QualifiedNonTerminalVisible(){ 
	makeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("MMM::A a;", importedModules=["MMM"]);
}

test bool UseNonTerminal1(){ 
	makeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("[A]\"a\";", importedModules=["MMM"]);
}

test bool UseNonTerminal2(){ 
	makeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A anA = [A]\"a\";", importedModules=["MMM"]);
}

test bool UseNonTerminal3(){ 
	makeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("(A)`a`;", importedModules=["MMM"]);
}

test bool UseNonTerminal4(){ 
	makeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A anA = (A)`a`;", importedModules=["MMM"]);
}

test bool ExtendNonTerminal(){            // TODO: EmptyList()
	makeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A a;", initialDecls=["syntax A = \"b\";"], importedModules=["MMM"]);
}

test bool UseExtendedNonTerminal(){       // TODO: EmptyList()
	makeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A x = [A] \"b\";", initialDecls=["syntax A = \"b\";"], importedModules=["MMM"]);
}

// Data declaration in imported module

test bool ADTVisible(){ 
	makeModule("MMM", "data DATA = d();"); 
	return checkOK("DATA x;", importedModules=["MMM"]);
}

test bool QualifiedADTVisible(){ 
	makeModule("MMM", "data DATA = d();"); 
	return checkOK("MMM::DATA x;", importedModules=["MMM"]);
}

test bool ExtendADT(){ 
	makeModule("MMM", "data DATA = d();"); 
	return checkOK("DATA x = d2(3);", initialDecls=["data DATA = d2(int n);"], importedModules=["MMM"]);
}

test bool UseVariableInConcreteSyntax() {
    makeModule("MMM", "syntax A = a:\"a\"; 
                      'A hello() {
                      '  A given_a = (A) `a`;
                      '  return (A) `\<A given_a\> \<A given_a\>`;
                      '}");
                      
    return checkOK("hello();", importedModules=["MMM"]);
}

test bool RedeclareConstructorError(){ 
	makeModule("MMM", "data DATA = d(int n);"); 
	return declarationError("DATA x = d(3);", initialDecls=["data DATA = d(int m);"], importedModules=["MMM"]);
}

// Alias declaration in imported module

test bool UseImportedAlias(){ 
	makeModule("MMM", "alias INT = int;"); 
	return checkOK("int x = 3;", importedModules=["MMM"]);
}

test bool closureInTuple() {
    makeModule("MMM", "tuple[int(int)] f() = \<int(int i) { return 2 * i; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}
test bool voidClosureInTuple1() {
    makeModule("MMM", "tuple[void(int)] f() = \<void(int i) { return; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}

test bool voidClosureInTuple2() {
    makeModule("MMM", "tuple[void(int)] f() = \<(int i) { return; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}

test bool voidClosureInTuple3() {
    makeModule("MMM", "tuple[void(int)] f() = \<void(int i) { return; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}

test bool voidClosureInTuple4() {
    makeModule("MMM", "tuple[void(int)] f() = \<(int i) { return; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}

test bool nonAmbiguousParameter1() =
    checkOK("int getN(f(str s, n)) = n;", 
            initialDecls=["data F = f(str s, int n) | f(int n, str s);"]);
            
test bool nonAmbiguousParameter2() =
    checkOK("int getN(f(\"a\", n)) = n;", 
            initialDecls=["data F = f(str s, int n) | f(int n, str s);"]);
    
test bool nonAmbiguousParameter3() =
    checkOK("int getN(f(s, int n)) = n;", 
            initialDecls=["data F = f(str s, int n) | f(int n, str s);"]);	

test bool nonAmbiguousParameter4() =
    checkOK("int getN(f(n)) = n;", 
            initialDecls=["data F = f(int n) | f(int n, str s);"]);

test bool nonAmbiguousParameter5() =
    checkOK("int getN(f(n, s)) = n;", 
            initialDecls=["data F = f(int n) | f(int n, str s);"]);                 
            
test bool ambiguousParameter1() =
    declarationError("int getN(f(s, n)) = n;", 
            initialDecls=["data F = f(str s, int n) | f(int n, str s);"]);
            
test bool listWithWrongArity() =
    declarationError("list[int,str] x = [];");

test bool F1() = checkOK("int x = f(1);", initialDecls = ["int f(int n, int k = 0) { return n; }"]);

test bool F2() = argumentMismatch("int x = f(true);", initialDecls = ["int f(int n, int k = 0) { return n; }"]);

test bool F3() = argumentMismatch("int x = f();", initialDecls = ["int f(int n, int k = 0) { return n; }"]);

test bool F4() = argumentMismatch("int x = f(1, \"a\");", initialDecls = ["int f(int n, int k = 0) { return n; }"]);

test bool F5() = checkOK("int x = f(1, k=3);", initialDecls = ["int f(int n, int k = 0) { return n; }"]);

test bool F6() = argumentMismatch("int x = f(1, k=\"a\");", initialDecls = ["int f(int n, int k = 0) { return n; }"]);

test bool F7() = argumentMismatch("int x = f(1, kkk=\"a\");", initialDecls = ["int f(int n, int k = 0) { return n; }"]);

test bool F8() = unexpectedType("int x = g(1, kkk=\"a\");", initialDecls = ["int f(int n, int k = 0) { return n; }"]);

test bool O1() = checkOK("int x = f(1, k=1); str y = f(\"a\");",
	initialDecls = ["int f(int n, int k = 0) { return n; }",
	                "str f(str s, bool b = false) { return s; }"]
					);

test bool O2() = checkOK("void main(){ int x = f(1, k=1); str y = f(\"a\"); z = f(true, \"a\"); z = f(\"a\", false); }",
	initialDecls = ["data D
						= f(bool n, str s, int z = 0)
						| f(str q, bool b);",
					"int f(int n, int k = 0) { return n; }",
	                "str f(str s, bool b = false) { return s; }"]
					);
test bool K1() = checkOK("D x = d1();", initialDecls=["data D(int n = 0) = d1();"]);

test bool K2() = checkOK("D x = d1(n=3);", initialDecls=["data D(int n = 0) = d1();"]);

test bool K3() = argumentMismatch("D x = d1(m=3);", initialDecls=["data D(int n = 0) = d1();"]);
test bool K4() = argumentMismatch("D x = d1(n=true);", initialDecls=["data D(int n = 0) = d1();"]);
test bool K5() = checkOK("D x = d2(b=true);", 
	initialDecls=["data D(int n = 0) = d1();",
	 			  "data D(bool b = true) = d2();"]);

test bool K6() = checkOK("D x = d2(n=3);", 
	initialDecls=["data D(int n = 0) = d1();",
	 			  "data D(bool b = true) = d2();"]);

test bool R1() = checkOK("int f() { return 10; }");

test bool R2() = unexpectedType("int f() { return \"a\"; }");

test bool R3() = unexpectedType("int f(n) { return n; }");

test bool R4() = unexpectedType("list[int] f([n]) { return [n]; }");

test bool R5() = checkOK("value f(b) { return b; }");

test bool R6() = checkOK("value f(b, bool c) { if(c) 10; return b; }");

test bool R7() = unexpectedType("value f(b) {  b || true; return b; }");

test bool R8() = unexpectedType("value f(b) { n = b || true; return b; }");

test bool R9() = unexpectedType("alue f(b) { n = b || true; if(b) 10; return b; }");

test bool R10() = unexpectedType(" value f(b) { n = b || true; if(b) 10; return b; b && false; }");

test bool Issue1051() = checkOK("int main() = apply(func, f());",
	initialDecls = [
		"data F = f() | g();",
    	"int func(f()) = 1;",
    	"int func(g()) = 2;",
    	"int apply(int (F) theFun, F theArg) = theFun(theArg);"
	]);

test bool Var1() = checkOK("int z = f(\"a\");", initialDecls=["int f(str s, int n...) = n[0];"]);