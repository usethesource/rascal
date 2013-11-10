module experiments::Compiler::Benchmarks::BVisit6d

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

value main(list[value] args) {
	res = {};
	for(j <- [1 .. 100000]) {
	
		res = visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[list[ABCD]] x: insert x;
				case list[ABCD] x: insert x;
				case ABCD x: insert x;
				case int x: insert x;
			} 
	}
	return res;
}