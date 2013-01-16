@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module demo::basic::FactorialTest

import demo::basic::Factorial;

// Observe that the integers in Rascal can have arbitrary size

public test bool t1() =
  fac(47) ==  258623241511168180642964355153611979969197632389120000000000;
