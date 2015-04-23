module lang::rascal::tests::imports::C1

import lang::rascal::tests::imports::C2;

data D = d(int n);

bool isDint(d(int _)) = true;
default bool isDint(D _) = false;