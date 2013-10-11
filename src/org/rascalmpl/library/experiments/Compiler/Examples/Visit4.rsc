module experiments::Compiler::Examples::Visit4

value main(list[value] args) {
	return visit({ <1,1>, <2,2>, <3,3> }) {
				case set[value] s => s + { <4,5> }
				case tuple[int,int] t => { elem = ( 0 | it + i | int i <- t); <elem,elem>; }
				case int i => i + 100
			} +
			top-down visit({ <1,1>, <2,2>, <3,3> }) {
				case set[value] s => s + { <4,5> }
				case tuple[int,int] t => { elem = ( 0 | it + i | int i <- t); <elem,elem>; }
				case int i => i + 100
			};
}