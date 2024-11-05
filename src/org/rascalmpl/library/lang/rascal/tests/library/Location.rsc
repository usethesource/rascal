@license{
  Copyright (c) 2013-2024 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::tests::library::Location

import Location;

test bool isOverlapping1() =  isOverlapping(|unknown:///|(0, 2), |unknown:///|(0, 2));
test bool isOverlapping2() =  isOverlapping(|unknown:///|(0, 2), |unknown:///|(1, 2));
test bool isOverlapping3() = !isOverlapping(|unknown:///|(0, 2), |unknown:///|(2, 2));
test bool isOverlapping4() =  isOverlapping(|unknown:///|(1, 2), |unknown:///|(0, 2));
test bool isOverlapping5() =  isOverlapping(|unknown:///|(1, 2), |unknown:///|(1, 2));
test bool isOverlapping6() =  isOverlapping(|unknown:///|(1, 2), |unknown:///|(2, 2));
test bool isOverlapping7() = !isOverlapping(|unknown:///|(2, 2), |unknown:///|(0, 2));
test bool isOverlapping8() =  isOverlapping(|unknown:///|(2, 2), |unknown:///|(1, 2));
test bool isOverlapping9() =  isOverlapping(|unknown:///|(2, 2), |unknown:///|(2, 2));
