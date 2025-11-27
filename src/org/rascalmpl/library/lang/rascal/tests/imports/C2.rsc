module lang::rascal::tests::imports::C2

import lang::rascal::tests::imports::C1;

data D = d(str s);

alias C2Alias = int;

bool isDstr(d(str _)) = true;
default bool isDstr(D _) = false;

C2Alias C2func(C1Alias i) = i;

public int C2function() { return 2; }

int C2testFunction(int () f = C1function) = f();
