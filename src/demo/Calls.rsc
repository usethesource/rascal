module demo::Calls

import Set;
import Relation;
import Graph;
import UnitTest;

// Exploring a Call graph, see extensive description in Rascal user manual

alias Proc = str;

public bool test(){

	rel[Proc, Proc] Calls = {<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, <"d", "e">, <"f", "e">, <"f", "g">, <"g", "e">};

	int nCalls = size(Calls);

	assertEqual(nCalls, 8);

	set[Proc] Procs = carrier(Calls);

	assertEqual(Procs, {"a", "b", "c", "d", "e", "f", "g"});

	int nProcs = size(Relation::carrier(Calls));

	assertEqual(nProcs, 7);

	set[str] dCalls = domain(Calls);
	
	set[str] rCalls = range(Calls);

	set[Proc] entryPoints = top(Calls);

	assertEqual(entryPoints, {"a", "f"});

	set[Proc] bottomCalls = bottom(Calls);

	assertEqual(bottomCalls, {"c", "e"});

	rel[Proc,Proc] closureCalls = Calls+;

	assertEqual(closureCalls,
		{<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, 
		<"d","e">, <"f", "e">, <"f", "g">, <"g", "e">, 
		<"a", "c">, <"a", "d">, <"b", "e">, <"a", "e">}
		);

	set[Proc] calledFromA = closureCalls["a"];

	assertEqual(calledFromA, {"b", "c", "d", "e"});

	set[Proc] calledFromF = closureCalls["f"];

	assertEqual(calledFromF, {"e", "g"});

	set[Proc] commonProcs = calledFromA & calledFromF;

	assertEqual(commonProcs, {"e"});

	return report("Calls");
}
