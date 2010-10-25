module demo::Calls

import Set;
import Relation;
import Graph;

// Exploring a Call graph, see description in Rascal user manual

alias Proc = str;

private	rel[Proc, Proc] Calls = {<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, 
                                 <"d", "e">, <"f", "e">, <"f", "g">, <"g", "e">};

test size(Calls) == 8;

test carrier(Calls) == {"a", "b", "c", "d", "e", "f", "g"};

test size(Relation::carrier(Calls)) == 7;

set[str] dCalls = domain(Calls);
set[str] rCalls = range(Calls);

set[Proc] entryPoints = top(Calls);

test top(Calls) == {"a", "f"};
test bottom(Calls) == {"c", "e"};

test Calls+ == 
		{<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, 
 		 <"d","e">, <"f", "e">, <"f", "g">, <"g", "e">, 
		 <"a", "c">, <"a", "d">, <"b", "e">, <"a", "e">}
		;

test (Calls+)["a"] == {"b", "c", "d", "e"};

test (Calls+)["f"] == {"e", "g"};

test (Calls+)["a"] & (Calls+)["f"] ==  {"e"};
