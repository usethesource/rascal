module tests::functionality::AliasTCTests

import StaticTestingUtils;

public test bool outofOrderDeclaration() = declarationError("INTEGER0 x = 0; x == 0;", initialDecls=["alias INTEGER0 = INTEGER1;", "alias INTEGER1 = int;"]);
