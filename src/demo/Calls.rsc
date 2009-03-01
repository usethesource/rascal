module Calls

/*
 * TODO:
 * - Graph should extend Set and Relation
*/

import Set;
import Relation;
import Graph;

alias Proc = str;

bool test(){

	rel[Proc, Proc] Calls = {<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, <"d", "e">, <"f", "e">, <"f", "g">, <"g", "e">};

	int nCalls = size(Calls);

	assert nCalls == 8;

	set[Proc] Procs = carrier(Calls);

	assert Procs == {"a", "b", "c", "d", "e", "f", "g"};

	int nProcs = size(Relation::carrier(Calls));

	assert nProcs == 7;

	set[str] dCalls = domain(Calls);
	
	set[str] rCalls = range(Calls);

	set[Proc] entryPoints = top(Calls);

	assert entryPoints == {"a", "f"};

	set[Proc] bottomCalls = bottom(Calls);

	assert bottomCalls == {"c", "e"};

	rel[Proc,Proc] closureCalls = Calls+;

	assert closureCalls == 
		{<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, 
		<"d","e">, <"f", "e">, <"f", "g">, <"g", "e">, 
		<"a", "c">, <"a", "d">, <"b", "e">, <"a", "e">};

	set[Proc] calledFromA = closureCalls["a"];

	assert calledFromA == {"b", "c", "d", "e"};

	set[Proc] calledFromF = closureCalls["f"];

	assert calledFromF == {"e", "g"};

	set[Proc] commonProcs = calledFromA & calledFromF;

	assert commonProcs == {"e"};

	return true;
}
