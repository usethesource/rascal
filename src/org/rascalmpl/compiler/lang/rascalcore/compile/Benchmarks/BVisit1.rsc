module lang::rascalcore::compile::Benchmarks::BVisit1

value main() {
	res = [];
	for(j <- [1 .. 10000]) {
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