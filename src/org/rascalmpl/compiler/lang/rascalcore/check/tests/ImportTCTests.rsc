@bootstrapParser
module lang::rascalcore::check::tests::ImportTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool ImportError() = missingModuleInModule("
	module ImportError
		import zap;
	");

test bool UndefinedPrivateVar1(){
	writeModule("module M private int m = 3;");
	return undeclaredVariableInModule("
		module UndefinedPrivateVar1
			import M;
			value main() = m;
		");
}

test bool UndefinedPrivateVar2(){
	writeModule("module M private int m = 3;");
	return undeclaredVariableInModule("
		module UndefinedPrivateVar2
			import M;
			int n = m;
		");
}

test bool UndefinedPrivateFunction(){
	writeModule("module M private int f() {return 3;}");
	return undeclaredVariableInModule("
		module UndefinedPrivateFunction
			import M;
			value main() = f();
		");
}