module lang::rascalcore::compile::Benchmarks::BVisit6b

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

value main() {
	res = {};
	for(j <- [1 .. 100000]) {
	
		res = visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case int x: ;
			}
		;
		
	}
	return res;
}