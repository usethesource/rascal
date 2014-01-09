module experiments::Compiler::Benchmarks::BVisit1

value main(list[value] args) {
	res = [];
	for(j <- [1 .. 100000]) {
		res = visit([1,2,3]) {
					case list[int] l => [ ( 0 | it + i | int i <- l) ]
					case int i => i + 100
			  } +
			  top-down visit([1,2,3]) {
							case list[int] l => [ ( 0 | it + i | int i <- l) ]
							case int i => i + 100
			  };
	}
	return res;
}