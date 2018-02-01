module lang::rascalcore::compile::Benchmarks::BVisit4

value main() {
	res = {};
	for(j <- [1 .. 10000]) {
	
		res = bottom-up-break visit({ [ <1,1> ], [ <2,2> ], [ <3,3> ] }) {
				case set[lrel[int,int]] s => s + { [ <5,5> ] }
				case lrel[int,int] l => l + [ <4,4> ]
				case <int x, int y> => <x + 1000, y + 1000>
				case 2 => 102
			}
			+
			top-down-break visit({ [ <1,1> ], [ <2,2> ], [ <3,3> ] }) {
				case [ <1, 1> ] => [ <11, 11>, <4,4> ]
				case <int x, int y> => <x + 1000, y + 1000>
				case int i => i + 100
			}
			;
	}
	return res;
}