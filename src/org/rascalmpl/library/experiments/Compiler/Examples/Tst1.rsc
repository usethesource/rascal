module experiments::Compiler::Examples::Tst1

import lang::rascal::tests::types::StaticTestingUtils;

value main() =
    checkOK("writeFile(|file:///|, 1);", importedModules=["IO"]);