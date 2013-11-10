module experiments::Compiler::Benchmarks::BVisit6g

data ABCD = a(int x) | b(int x) | c(int x) | d(int x);

value main(list[value] args) {
	res = {};
	for(j <- [1 .. 100000]) {
	
		res = visit({ [ a(1) ], [ b(2) ], [ c(3) ] }) {
				case value x: 0;
			}
		;
		
	}
	return res;
}