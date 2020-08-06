@bootstrapParser
module lang::rascalcore::check::tests::StaticTestingUtilsUsingStdLibTests

import lang::rascalcore::check::tests::StaticTestingUtils;

// Sanity check on the testing utilities themselves

test bool testUtils10() = checkOK("13;", importedModules = ["util::Math"]);
	
test bool testUtils11() = checkOK("max(3, 4);", importedModules = ["util::Math"]);

test bool testUtils12() = checkOK("size([1,2,3]);", importedModules=["Exception", "List"]);

