module experiments::Compiler::Examples::Visit5

value main(list[value] args) {
	return visit({ "a"(1,1), "b"(2,2), "c"(3,3) }) {
				case set[node] s => s + { "d"(4,5) }
				case node n:str s(int x,int y) => { elem = ( 0 | it + i | int i <- n); (s + "_been_here")(elem,elem); }
				case int i => i + 100
			} 
			+
			top-down visit({ "a"(1,1), "b"(2,2), "c"(3,3) }) {
				case set[node] s => s + { "d"(4,5) }
				case node n:str s(int x,int y) => { elem = ( 0 | it + i | int i <- n); (s + "_been_here")(elem,elem); }
				case int i => i + 100
			}
			;
}