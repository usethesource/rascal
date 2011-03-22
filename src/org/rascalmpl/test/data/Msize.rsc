module Msize

import Set;

public int f() {
	set[int] Procs = {1, 2, 3};
	int nProcs = Set::size(Procs);
	return nProcs;
}

public int g() {
	set[int] Procs = {1, 2, 3};
	int nProcs = size(Procs);
	return nProcs;
}
