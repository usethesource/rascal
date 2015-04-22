module lang::rascal::tests::imports::C2

import lang::rascal::tests::imports::C1;

data D = d(str s);

bool isDstr(d(str _)) = true;
default bool isDstr(D _) = false;