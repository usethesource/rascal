module experiments::Compiler::Benchmarks::BCompareIf

value main(list[value] args) {
	res = [];
	for(j <- [1 .. 500000]) {
		if(int i <- [1,2,3,4,5,6,7,8,9]) {
			fail;
		}
	}
	return res;
}