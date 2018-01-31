module lang::rascalcore::compile::Benchmarks::BVisit6g

data ABCD = a(int x) | b(int x) | c(int x) | d(int x);

value main() {
	res = {};
	for(j <- [1 .. 10000]) {
	
		res = visit({ [ a(1) ], [ b(2) ], [ c(3) ] }) {
				case value x: 0;
			}
		;
		
	}
	return res;
}