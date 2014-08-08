module lang::rascal::tests::types::DeclarationTCTests

import lang::rascal::tests::types::StaticTestingUtils;
 
test bool localTypeInferenceNoEscape2() = undeclaredVariable("{ x = 1; x == 1; } x;");

test bool undeclaredType1() = undeclaredType("X N;");            // TODO Type X undeclared

test bool doubleDeclaration3() = redeclaredVariable("int f(int N){int N = 1; return N;}");

test bool shadowingDeclaration1() = redeclaredVariable("int N = 1; {int N = 2;}; N == 1;");

test bool shadowingDeclaration2() = redeclaredVariable("N = 1; {int N = 2;}; N == 1;");

test bool shadowingDeclaration4() = redeclaredVariable("int N = 3; int N := 3;");


// Variable declaration in imported module

test bool privateVarDeclarationNotVisible(){ 
	makeModule("M", "private int x = 3;"); 
	return undeclaredVariable("x;", importedModules=["M"]);
}

test bool publicVarDeclarationVisible(){ 
	makeModule("M", "public int x = 3;"); 
	return checkOK("x;", importedModules=["M"]);
}

test bool publicVarDeclarationVisibleViaQualifiedName(){ 
	makeModule("M", "public int x = 3;"); 
	return checkOK("M::x;", importedModules=["M"]);
}

test bool DefaultVarDeclarationNotVisible(){ 
	makeModule("M", "int x = 3;"); 
	return undeclaredVariable("x;", importedModules=["M"]);
}

test bool RedeclaredVarDeclaration(){
	makeModule("M", "public int x = 3;"); 
	return checkOK("int x = 4;", importedModules=["M"]);
}


test bool moduleRedeclarationError1(){ 
	makeModule("M", "public int n = 1; public int n = 2;"); 
	return redeclaredVariable("n == 1;", importedModules=["M"]);
}

test bool qualifiedScopeTest(){ 
	makeModule("M", "public int n = 1;"); 
	return checkOK("M::n == 1;", importedModules=["M"]);
}

// Function declaration in imported module

test bool privateFunDeclarationNotVisible(){ 
	makeModule("M", "private int f() = 3;"); 
	return undeclaredVariable("x();", importedModules=["M"]);
}

test bool publicFunDeclarationVisible(){ 
	makeModule("M", "public int f() = 3;"); 
	return checkOK("f();", importedModules=["M"]);
}

test bool publicFunDeclarationVisibleViaQualifiedName(){ 
	makeModule("M", "public int f() = 3;"); 
	return checkOK("M::f();", importedModules=["M"]);
}

test bool DefaultFunDeclarationVisible(){ 
	makeModule("M", "int f() = 3;"); 
	return checkOK("f();", importedModules=["M"]);
}

// Non-terminal declaration in imported module

test bool NonTerminalVisible(){ 
	makeModule("M", "syntax A = \"a\";"); 
	return checkOK("A a;", importedModules=["M"]);
}

test bool QualifiedNonTerminalVisible(){ 
	makeModule("M", "syntax A = \"a\";"); 
	return checkOK("M::A a;", importedModules=["M"]);
}

test bool UseNonTerminal1(){ 
	makeModule("M", "syntax A = \"a\";"); 
	return checkOK("[A]\"a\";", importedModules=["M"]);
}

test bool UseNonTerminal2(){ 
	makeModule("M", "syntax A = \"a\";"); 
	return checkOK("A anA = [A]\"a\";", importedModules=["M"]);
}

test bool UseNonTerminal3(){ 
	makeModule("M", "syntax A = \"a\";"); 
	return checkOK("(A)`a`;", importedModules=["M"]);
}

test bool UseNonTerminal4(){ 
	makeModule("M", "syntax A = \"a\";"); 
	return checkOK("A anA = (A)`a`;", importedModules=["M"]);
}

test bool ExtendNonTerminal(){            // TODO: EmptyList()
	makeModule("M", "syntax A = \"a\";"); 
	return checkOK("A a;", initialDecls=["syntax A = \"b\";"], importedModules=["M"]);
}

test bool UseExtendedNonTerminal(){       // TODO: EmptyList()
	makeModule("M", "syntax A = \"a\";"); 
	return checkOK("A x = [A] \"b\";", initialDecls=["syntax A = \"b\";"], importedModules=["M"]);
}

// Data declaration in imported module

test bool ADTVisible(){ 
	makeModule("M", "data DATA = d();"); 
	return checkOK("DATA x;", importedModules=["M"]);
}

test bool QualifiedADTVisible(){ 
	makeModule("M", "data DATA = d();"); 
	return checkOK("M::DATA x;", importedModules=["M"]);
}

test bool ExtendADT(){ 
	makeModule("M", "data DATA = d();"); 
	return checkOK("DATA x = d2(3);", initialDecls=["data DATA = d2(int n);"], importedModules=["M"]);
}

// MAH: We currently allow redeclarations in cases where the redeclaration exactly matches
// an existing declaration. The original test was modified to add constructor fields, which
// will then trigger an error.
test bool RedeclareConstructorError(){ 
	makeModule("M", "data DATA = d(int n);"); 
	return declarationError("DATA x = d(3);", initialDecls=["data DATA = d(int m);"], importedModules=["M"]);
}

// Alias declaration in imported module

test bool UseImportedAlias(){ 
	makeModule("M", "alias INT = int;"); 
	return checkOK("int x = 3;", importedModules=["M"]);
}


	
	