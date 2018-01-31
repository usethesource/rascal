module lang::rascalcore::compile::Benchmarks::BCompareComprehension

value main() {
	res = [];
	for(j <- [1 .. 50000]) {
		[ i | int i <- [1,2,3,4,5,6,7,8,9] ];
	}
	return res;
}