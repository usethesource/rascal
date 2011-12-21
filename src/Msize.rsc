@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
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
