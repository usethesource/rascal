@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{Ackermann's function: a standard example of a double recursive function.
See <http://en.wikipedia.org/wiki/Ackermann_function>}
module demo::basic::Ackermann

@synopsis{Compute Ackermann's function}
int ack(int m, int n) {
	if (m == 0) {
		return n + 1;
	}
	else if (n == 0) {
		return ack(m - 1, 1);
	}
	else {
		return ack(m - 1, ack(m, n - 1));
	}
}

test bool t1() = ack(2,5) == 13;
test bool t2() = ack(3,4) == 125;
