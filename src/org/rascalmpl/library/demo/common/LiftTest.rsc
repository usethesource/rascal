@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::common::LiftTest

import demo::common::Lift;

// The following only for testing

rel[proc,proc] Calls = {<"main", "a">, <"main", "b">, <"a", "b">, <"a", "c">, <"a", "d">, <"b", "d">};        

set[comp] Components = {"Appl", "DB", "Lib"};

rel[proc, comp] PartOf = {<"main", "Appl">, <"a", "Appl">, <"b", "DB">, 
			  			  <"c", "Lib">, <"d", "Lib">};

rel[comp,comp] ComponentCalls = lift(Calls, PartOf);

// Tests

public test bool t1() =
  ComponentCalls == { < "DB" , "Lib" > , < "Appl" , "Lib" > , 
			          < "Appl" , "DB" > , < "Appl" , "Appl" > };



