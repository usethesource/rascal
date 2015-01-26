 module lang::rascal::tests::library::GraphTests
  /*******************************************************************************
   * Copyright (c) 2009-2015 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/
 import analysis::graphs::Graph;
  
// bottom
  
test bool bottom1() = bottom({}) == {};
test bool bottom2() = bottom({<1,2>, <1,3>, <2,4>, <3,4>}) == {4};
  
// predecessors
  
test bool predecessors1()=  predecessors({<1,2>, <1,3>, <2,4>, <3,4>}, 4) =={2, 3};
  
// reachR()
  
test bool reachR1() = reachR({}, {}, {}) == {};
test bool reachR2() = reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {}) =={};
test bool reachR3() = reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2}) =={2};
test bool reachR4() = reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2,3}) =={2,3};
test bool reachR5() = reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2,4}) =={2, 4};
  
// reachX
  
test bool reachX1() = reachX({}, {}, {}) == {};
test bool reachX() = reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {}) =={2, 3, 4};
test bool reachX2() = reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2}) =={3, 4};
test bool reachX() = reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2,3}) =={};
test bool reachX3() = reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {4}) =={2, 3};
  	
// reach
  
test bool reach0() = reach({}, {}) == {};
test bool reach1() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {1}) =={1,2, 3, 4};
test bool reach2() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {2}) =={2, 4};
test bool reach3() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {3}) =={3, 4};
test bool reach4() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {4}) =={4};
test bool reach5() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {3,4}) =={3,4};
test bool reach6() = reach({<1,2>, <1,3>, <2,4>, <3,4>}, {2,3}) =={2, 3,4};
  	
// successors
  
test bool successors1() = successors({<1,2>, <1,3>, <2,4>, <3,4>}, 1) =={2, 3};
  
// top
  
test bool top1() = top({}) == {};
test bool top2() = top({<1,2>, <1,3>, <2,4>, <3,4>}) == {1};
 