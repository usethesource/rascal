@bootstrapParser
module lang::rascalcore::check::tests::ImportTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool importError() = missingModule("true;", initialDecls= ["import zap;"]);

test bool UndefinedPrivateVar1(){
	writeModule("M", "private int m = 3;");
	return undeclaredVariable("m;", importedModules=["M"]);
}

test bool UndefinedPrivateVar2(){
	writeModule("M", "private int m = 3;");
	return undeclaredVariable("int n = m;", importedModules=["M"]);
}

test bool UndefinedPrivateFunction(){
	writeModule("M", "private int f() {return 3;}");
	return undeclaredVariable("f();", importedModules=["M"]);
}