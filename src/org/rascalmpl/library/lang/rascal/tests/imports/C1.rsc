module lang::rascal::tests::imports::C1

import lang::rascal::tests::imports::C2;

data D = d(int n);

alias C1Alias = int;

bool isDint(d(int _)) = true;
default bool isDint(D _) = false;

C1Alias C1func(C2Alias i) = i;

public int C1function() { return 1; }

int C1testFunction(int () f = C2function) = f();
