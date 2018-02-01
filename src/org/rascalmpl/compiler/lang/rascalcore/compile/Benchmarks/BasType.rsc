module lang::rascalcore::compile::Benchmarks::BasType

syntax A = "a";
syntax As = A+;

syntax B = "b";
syntax Bs = B+;

value main() {
    for(i <- [1 .. 100]){
		<[As] "aaaa", [Bs] "bbb" >;
	}
    return 0;

}