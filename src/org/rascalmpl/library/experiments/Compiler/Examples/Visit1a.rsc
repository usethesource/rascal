module experiments::Compiler::Examples::Visit1a

value main(list[value] args) {
	return visit([1,2,3]) {
				case list[int] l: insert [ ( 0 | it + i | int i <- l) ];
				case int i: { i = i + 100; insert i; i = i + 200; }
			} +
			top-down visit([1,2,3]) {
				case list[int] l: insert [ ( 0 | it + i | int i <- l) ];
				case int i: { i = i + 100; insert i; i = i + 200; }
			};
}