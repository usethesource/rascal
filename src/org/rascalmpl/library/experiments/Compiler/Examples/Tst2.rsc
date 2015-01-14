module experiments::Compiler::Examples::Tst2

import lang::rascal::tests::types::StaticTestingUtils;

test bool undeclaredTypeError() = declarationError("A x = 0;", initialDecls=["alias A = B;"]);