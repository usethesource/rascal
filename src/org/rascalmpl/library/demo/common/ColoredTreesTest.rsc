@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::common::ColoredTreesTest

import demo::common::ColoredTrees;

// Tests

public ColoredTree  rb = red(black(leaf(1), red(leaf(2),leaf(3))), black(leaf(3), leaf(4)));

public test bool t1() = cntRed(rb) == 2;
public test bool t2() = addLeaves(rb) == 13;
public test bool t3() = makeGreen(rb) == green(black(leaf(1),green(leaf(2),leaf(3))),black(leaf(3),leaf(4)));
