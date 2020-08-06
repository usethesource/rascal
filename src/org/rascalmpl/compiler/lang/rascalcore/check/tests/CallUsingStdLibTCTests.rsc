@bootstrapParser
module lang::rascalcore::check::tests::CallUsingStdLibTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
	
test bool varArgs1() =
	checkOK("writeFile(|file:///|, 1);", importedModules=["IO"]);
	
test bool varArgs2() =
	checkOK("writeFile(|file:///|, 1, \"abc\");", importedModules=["IO"]);