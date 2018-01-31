module lang::rascalcore::compile::Benchmarks::BVisit6f

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

value main() {
	res = {};
	for(j <- [1 .. 10000]) {
	
		res = visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[list[ABCD]] x => x
				case list[ABCD] x => x
				case a(x,y) => d(x,y)
				case b(x,y) => d(x,y)
				case c(x,y) => d(x,y)
				case d(x,y) => d(x,y)
				case int x => x
			} 
	}
	return res;
}