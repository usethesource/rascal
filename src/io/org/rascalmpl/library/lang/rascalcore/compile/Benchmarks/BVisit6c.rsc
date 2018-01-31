module lang::rascalcore::compile::Benchmarks::BVisit6c

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

value main() {
	res = {};
	for(j <- [1 .. 10000]) {
	
		res = visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[list[ABCD]] x => x
				case list[ABCD] x => x
				case ABCD x => x
				case int x => x
			} 
	}
	return res;
}