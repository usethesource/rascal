@bootstrapParser
module lang::rascal::meta::ModuleInfoTests

import lang::rascal::\syntax::Rascal;

import lang::rascal::meta::ModuleInfo;

test bool tstGetImports1() = getImports((Module) `module MMM`) == importsInfo({}, {});
test bool tstGetImports2() = getImports((Module) `module MMM import A;`) == importsInfo({"A"}, {});
test bool tstGetImports3() = getImports((Module) `module MMM import A;extend B;import C;`) == importsInfo({"A", "C"}, {"B"});
test bool tstGetImports4() = getImports((Module) `module MMM syntax A = "a";`) == importsInfo({}, {});
