@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::common::Calls

import Set;
import Relation;
import  analysis::graphs::Graph;

// Exploring a Call graph, see description in Rascal user manual

alias Proc = str;

private	rel[Proc, Proc] Calls = {<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, 
                                 <"d", "e">, <"f", "e">, <"f", "g">, <"g", "e">};

public test bool t1() = size(Calls) == 8;

public test bool t2() = carrier(Calls) == {"a", "b", "c", "d", "e", "f", "g"};

public test bool t3() = size(Relation::carrier(Calls)) == 7;

set[str] dCalls = domain(Calls);
set[str] rCalls = range(Calls);

set[Proc] entryPoints = top(Calls);

public test bool t4() = top(Calls) == {"a", "f"};
public test bool t5() = bottom(Calls) == {"c", "e"};

public test bool t6() =
   Calls+ == 
		{<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, 
 		 <"d","e">, <"f", "e">, <"f", "g">, <"g", "e">, 
		 <"a", "c">, <"a", "d">, <"b", "e">, <"a", "e">}
		;

public test bool t7() = (Calls+)["a"] == {"b", "c", "d", "e"};

public test bool t8() = (Calls+)["f"] == {"e", "g"};

public test bool t9() = (Calls+)["a"] & (Calls+)["f"] ==  {"e"};
