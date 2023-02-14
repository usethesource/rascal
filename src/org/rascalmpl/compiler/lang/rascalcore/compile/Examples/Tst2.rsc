@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

import lang::rascalcore::check::Checker;

value main() = rascalTModelForNames(["lang::rascalcore::compile::Examples::Tst1"], getRascalCorePathConfig(), rascalTypePalConfig(classicReifier=true));