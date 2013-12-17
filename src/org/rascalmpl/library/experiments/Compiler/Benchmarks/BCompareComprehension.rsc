module experiments::Compiler::Benchmarks::BCompareComprehension

value main(list[value] args) {
	res = [];
	for(j <- [1 .. 500000]) {
		[ i | int i <- [1,2,3,4,5,6,7,8,9] ];
	}
	return res;
}