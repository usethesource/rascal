module lang::rascal::tests::imports::M6

import Set;

public set[int] Procs = {1, 2, 3};

public int f() {int nProcs = Set::size(Procs); return nProcs;}

public int g() {int nProcs = size(Procs); return nProcs;}
