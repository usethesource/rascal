@ignoreCompiler{Mysterious loop in tests}
@bootstrapParser
module lang::rascalcore::check::tests::AliasTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool doubleDeclarationError() = declarationError("A x = 0;", initialDecls=["alias A = str;", "alias A = int;"]);

test bool circularDeclarationError() = declarationError("A x = 0;", initialDecls=["alias A = A;"]);

test bool undeclaredTypeError() = undeclaredType("A x = 0;", initialDecls=["alias A = B;"]);
test bool incompatibleInitError() = declarationError("A x = 0;", initialDecls=["alias B = str;", "alias A = B;"]);

test bool circularAliasError1() = declarationError("INTEGER0 x = 0;", initialDecls=["alias INTEGER0 = INTEGER1;", "alias INTEGER1 = INTEGER2;", "alias INTEGER2 = INTEGER0;"]);
test bool circularAliasError2() = declarationError("INTEGER1 x = 0;", initialDecls=["alias INTEGER0 = INTEGER1;", "alias INTEGER1 = INTEGER2;", "alias INTEGER2 = INTEGER0;"]);
test bool circularAliasError3() = declarationError("INTEGER2 x = 0;", initialDecls=["alias INTEGER0 = INTEGER1;", "alias INTEGER1 = INTEGER2;", "alias INTEGER2 = INTEGER0;"]);
test bool circularAliasError4() = declarationError("INTEGER0 x = 0; x == 0;", initialDecls=["alias INTEGER0 = INTEGER1;", "alias INTEGER1 = INTEGER0;"]);
