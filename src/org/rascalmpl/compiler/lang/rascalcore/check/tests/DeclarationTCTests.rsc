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
	writeModule("MMM", "private int x = 3;"); 
	return undeclaredVariable("x;", importedModules=["MMM"]);
}

test bool publicVarDeclarationVisible(){ 
	writeModule("MMM", "public int x = 3;"); 
	return checkOK("x;", importedModules=["MMM"]);
}

test bool publicVarDeclarationVisibleViaQualifiedName(){ 
	writeModule("MMM", "public int x = 3;"); 
	return checkOK("MMM::x;", importedModules=["MMM"]);
}

test bool DefaultVarDeclarationNotVisible(){ 
	writeModule("MMM", "int x = 3;"); 
	return undeclaredVariable("x;", importedModules=["MMM"]);
}

test bool RedeclaredVarDeclaration(){
	writeModule("MMM", "public int x = 3;"); 
	return checkOK("int x = 4;", importedModules=["MMM"]);
}

// MAH: I will need to look for a good way to test bool this now; the configuration no
// longer includes errors detected in imported modules unless they are actual
// import errors, in this case the first n is in the imported configuration
// but the second n isn't since it would raise an error while checking M.
//test bool moduleReunexpectedDeclaration1(){ 
//	writeModule("MMM", "public int n = 1; public int n = 2;"); 
//	return redeclaredVariable("n == 1;", importedModules=["MMM"]);
//}

test bool qualifiedScopeTest(){ 
	writeModule("MMM", "public int n = 1;"); 
	return checkOK("MMM::n == 1;", importedModules=["MMM"]);
}

// Function declaration in imported module

test bool privateFunDeclarationNotVisible(){ 
	writeModule("MMM", "private int f() = 3;"); 
	return undeclaredVariable("f();", importedModules=["MMM"]);
}

test bool publicFunDeclarationVisible(){ 
	writeModule("MMM", "public int f() = 3;"); 
	return checkOK("f();", importedModules=["MMM"]);
}

test bool publicFunDeclarationVisibleViaQualifiedName(){ 
	writeModule("MMM", "public int f() = 3;"); 
	return checkOK("MMM::f();", importedModules=["MMM"]);
}

test bool DefaultFunDeclarationVisible(){ 
	writeModule("MMM", "int f() = 3;"); 
	return checkOK("f();", importedModules=["MMM"]);
}

// Non-terminal declaration in imported module

test bool NonTerminalVisible(){ 
	writeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A a;", importedModules=["MMM"]);
}

test bool QualifiedNonTerminalVisible(){ 
	writeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("MMM::A a;", importedModules=["MMM"]);
}

test bool UseNonTerminal1(){ 
	writeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("[A]\"a\";", importedModules=["MMM"]);
}

test bool UseNonTerminal2(){ 
	writeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A anA = [A]\"a\";", importedModules=["MMM"]);
}

test bool UseNonTerminal3(){ 
	writeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("(A)`a`;", importedModules=["MMM"]);
}

test bool UseNonTerminal4(){ 
	writeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A anA = (A)`a`;", importedModules=["MMM"]);
}

test bool ExtendNonTerminal(){            // TODO: EmptyList()
	writeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A a;", initialDecls=["syntax A = \"b\";"], importedModules=["MMM"]);
}

test bool UseExtendedNonTerminal(){       // TODO: EmptyList()
	writeModule("MMM", "syntax A = \"a\";"); 
	return checkOK("A x = [A] \"b\";", initialDecls=["syntax A = \"b\";"], importedModules=["MMM"]);
}

// Data declaration in imported module

test bool ADTVisible(){ 
	writeModule("MMM", "data DATA = d();"); 
	return checkOK("DATA x;", importedModules=["MMM"]);
}

test bool QualifiedADTVisible(){ 
	writeModule("MMM", "data DATA = d();"); 
	return checkOK("MMM::DATA x;", importedModules=["MMM"]);
}

test bool ExtendADT(){ 
	writeModule("MMM", "data DATA = d();"); 
	return checkOK("DATA x = d2(3);", initialDecls=["data DATA = d2(int n);"], importedModules=["MMM"]);
}

test bool UseVariableInConcreteSyntax() {
    writeModule("MMM", "syntax A = a:\"a\"; 
                      'A hello() {
                      '  A given_a = (A) `a`;
                      '  return (A) `\<A given_a\> \<A given_a\>`;
                      '}");
                      
    return checkOK("hello();", importedModules=["MMM"]);
}

test bool RedeclareConstructorError(){ 
	writeModule("MMM", "data DATA = d(int n);"); 
	return unexpectedDeclaration("DATA x = d(3);", initialDecls=["data DATA = d(int m);"], importedModules=["MMM"]);
}

// Alias declaration in imported module

test bool UseImportedAlias(){ 
	writeModule("MMM", "alias INT = int;"); 
	return checkOK("int x = 3;", importedModules=["MMM"]);
}

test bool closureInTuple() {
    writeModule("MMM", "tuple[int(int)] f() = \<int(int i) { return 2 * i; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}
test bool voidClosureInTuple1() {
    writeModule("MMM", "tuple[void(int)] f() = \<void(int i) { return; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}

test bool voidClosureInTuple2() {
    writeModule("MMM", "tuple[void(int)] f() = \<(int i) { return; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}

test bool voidClosureInTuple3() {
    writeModule("MMM", "tuple[void(int)] f() = \<void(int i) { return; }\>;");
    return checkOK("f();",  importedModules=["MMM"]);
}

test bool voidClosureInTuple4() {
    writeModule("MMM", "tuple[void(int)] f() = \<(int i) { return; }\>;");
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
    unexpectedDeclaration("int getN(f(s, n)) = n;", 
            initialDecls=["data F = f(str s, int n) | f(int n, str s);"]);
            
test bool listWithWrongArity() =
    unexpectedDeclaration("list[int,str] x = [];");

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


// ---- alias -----------------------------------------------------------------

test bool OkAlias1() = checkOK("INT z = 13;", initialDecls = ["alias INT = int;"]);

test bool UnknownAlias() = unexpectedType("INT z = 13;");

test bool IllegalParameter() = unexpectedType("INT[str] z = 13;", initialDecls = ["alias INT = int;"]);

test bool OkParameter() = checkOK("LIST[int] z = [1,2];", initialDecls = ["alias LIST[&T] = list[&T];"]);

test bool WrongTypeParameter() = unexpectedType("LIST[str] z = [1,2];", initialDecls = ["alias LIST[&T] = list[&T];"]);

test bool OkIndirectAlias1() = checkOK("LIST[int] z = [1,2];",
	initialDecls = [ "alias LIST[&T] = LIST1[&T];", "alias LIST1[&T] = list[&T];"]);

test bool OkIndirectAlias2() = checkOK("LIST[int] z = [1,2];",
	initialDecls = [ "alias LIST1[&T] = list[&T];", "alias LIST[&T] = LIST1[&T];"]);

test bool CircularAlias() = unexpectedDeclaration("true;", 
	initialDecls = ["alias X = Y;", "alias Y = X;"]);

/////////////////////////////////////////
test bool LF1() = unexpectedDeclaration("list[int n] l = [];");

test bool SF1() = unexpectedDeclaration("set[int n] l = {};");

test bool MF1() = checkOK("map[str key, int val] x; set[str] y = x.key;");

test bool MF2() = unexpectedType("map[str key, int val] x; set[int] y = x.key;");

test bool MF3() = checkOK("map[str key, int val] x; set[int] y = x.val;");

test bool MF4() = undefinedField("map[str key, int val] x; set[int] y = x.vals;");

test bool MF5() = unexpectedDeclaration("map[str key, int] x;");

test bool MF6() = unexpectedDeclaration("map[str key, int key] x;");

test bool RF1() = checkOK("rel[str a, int b, real r] x; set[str] y = x.a;");

test bool RF2() = checkOK("rel[str a, int b, real r] x; set[int] y = x.b;");

test bool RF3() = checkOK("rel[str a, int b, real r] x; set[real] y = x.r;");

test bool RF4() = undefinedField("rel[str a, int b, real r] x; set[real] y = x.c;");

test bool RF5() = unexpectedDeclaration("rel[str a, int b, real] x;");

test bool RF6() = unexpectedDeclaration("rel[str a, int b, real a] x;");

test bool LRF1() = checkOK("lrel[str a, int b, real r] x; list[str] y = x.a;");

test bool LRF2() = checkOK("lrel[str a, int b, real r] x; list[int] y = x.b;");

test bool LRF3() = checkOK("lrel[str a, int b, real r] x; list[real] y = x.r;");

test bool LRF4() = undefinedField("lrel[str a, int b, real r] x; list[real] y = x.c;");

test bool LRF5() = unexpectedDeclaration("lrel[str a, int b, real] x;");

test bool LRF6() = unexpectedDeclaration("lrel[str a, int b, real a] x;");

test bool TF1() = checkOK("tuple[str a, int b, real r] x; str y = x.a;");

test bool RT2() = checkOK("tuple[str a, int b, real r] x; int y = x.b;");

test bool RT3() = checkOK("tuple[str a, int b, real r] x; real y = x.r;");

test bool TF4() = undefinedField("tuple[str a, int b, real r] x; real y = x.c;");

test bool TF5() = unexpectedDeclaration("tuple[str a, int b, real] x;");

test bool TF6() = unexpectedDeclaration("tuple[str a, int b, real a] x;");