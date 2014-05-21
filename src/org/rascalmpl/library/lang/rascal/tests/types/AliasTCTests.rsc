module lang::rascal::tests::types::AliasTCTests

import lang::rascal::tests::types::StaticTestingUtils;

public test bool outofOrderDeclaration() = declarationError("INTEGER0 x = 0; x == 0;", initialDecls=["alias INTEGER0 = INTEGER1;", "alias INTEGER1 = int;"]);
