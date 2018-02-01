module lang::rascalcore::compile::Benchmarks::BVisit6e

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

value main() {
	res = {};
	for(j <- [1 .. 10000]) {
	
		res = visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[list[ABCD]] x: 0;
				case list[ABCD] x: 0;
				case ABCD x: 0;
				case int x: 0;
			} 
	}
	return res;
}