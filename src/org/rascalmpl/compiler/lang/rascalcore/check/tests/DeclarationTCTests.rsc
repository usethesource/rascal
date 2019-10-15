@bootstrapParser
module lang::rascalcore::check::tests::DeclarationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
 
test bool localTypeInferenceNoEscape2() = undeclaredVariable("{ x = 1; x == 1; } x;");

test bool undeclaredType1() = undeclaredType("X N;");            // TODO Type X undeclared

test bool doubleDeclaration3() = redeclaredVariable("int f(int N){int N = 1; return N;}");

test bool shadowingDeclaration1() = redeclaredVariable("int N = 1; {int N = 2;}; N == 1;");

test bool shadowingDeclaration2() = redeclaredVariable("N = 1; {int N = 2;}; N == 1;");

test bool shadowingDeclaration4() = redeclaredVariable("int N = 3; int N := 3;");


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

// MAH: We currently allow redeclarations in cases where the redeclaration exactly matches
// an existing declaration. The original test was modified to add constructor fields, which
// will then trigger an error.
test bool RedeclareConstructorError(){ 
	makeModule("MMM", "data DATA = d(int n);"); 
	return declarationError("DATA x = d(3);", initialDecls=["data DATA = d(int m);"], importedModules=["MMM"]);
}

// Alias declaration in imported module

test bool UseImportedAlias(){ 
	makeModule("MMM", "alias INT = int;"); 
	return checkOK("int x = 3;", importedModules=["MMM"]);
}


	
	