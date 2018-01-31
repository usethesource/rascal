module lang::rascalcore::compile::Benchmarks::BVisit2

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

value main() {
	res = {};
	for(j <- [1 .. 5000]) {
	
		res = bottom-up-break visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[list[ABCD]] s => s + { [ d(5,5) ] }
				case list[ABCD] l => l + [ d(4,4) ]
				case a(int x, int y) => a(x + 1000, y + 1000)
				case b(int x, int y) => b(x + 1000, y + 1000)
				case 2 => 102
			} // { [ b(102,102) ], [ c(3,3), d(4,4) ], [ a(1001,1001) ] }
			+
			top-down-break visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case [ a(int x, int y) ] => [ a(x + 10, y + 10), d(4,4) ]
				case a(int x, int y) => a(x + 1000, y + 1000)
				case b(int x, int y) => b(x + 1000, y + 1000)
				case int i => i + 100
			} // { [ b(1002,1002) ], [ a(11,11),d(4,4) ], [ c(103,103) ] }
			;
	}
	return res;
}