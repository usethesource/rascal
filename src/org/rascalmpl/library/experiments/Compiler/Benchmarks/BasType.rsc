module experiments::Compiler::Benchmarks::BasType

syntax A = "a";
syntax As = A+;

syntax B = "b";
syntax Bs = B+;

value main(list[value] args) {
    for(i <- [1 .. 1000]){
		<[As] "aaaa", [Bs] "bbb" >;
	}
    return 0;

}