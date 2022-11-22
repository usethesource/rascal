@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::common::Trans

@synopsis{Compute transitive closure: R+ = R + (R o R) + (R o R o R) + ...}
@description{
  This demo can be also implemented by `R+` but here
  we want to demonstrate the use of the ((Statements-Solve)) statement.
}
rel[int,int] trans(rel[int,int] R){
  rel[int,int] T = R;
	
  solve (T) {
    T = T + (T o R);
  }

  assert T == R+;
  return T;
}

// Tests

test bool t1() = trans({<1,2>, <2,3>, <3,4>}) == {<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};
test bool t2() = trans(x) == x+ when x := {<1,2>,<2,3>,<3,4>};
