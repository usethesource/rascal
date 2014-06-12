module lang::rascal::tests::types::ImportTCTests

import lang::rascal::tests::types::StaticTestingUtils;

test bool importError() = declarationError("true;", initialDecls= ["import zap;"]);

test bool UndefinedPrivateVar1(){
	makeModule("M", "private int m = 3;");
	return declarationError("m;", importedModules=["M"]);
}

test bool UndefinedPrivateVar2(){
	makeModule("M", "private int m = 3;");
	return declarationError("int n = m;", importedModules=["M"]);
}

test bool UndefinedPrivateFunction(){
	makeModule("M", "private int f() {return 3;}");
	return declarationError("f();", importedModules=["M"]);
}