module experiments::Compiler::Examples::Tst1

value main(list[value] args){
	int f(){
		top-down visit([1,2,3]) {
				case list[int] l: insert [ ( 0 | it + i | int i <- l) ];
				case int i: { i = i + 100; return 42; i = i + 200; }
			}
		return 101;
	}
	return f();
}