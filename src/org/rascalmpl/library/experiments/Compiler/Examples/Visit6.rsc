module experiments::Compiler::Examples::Visit6

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

value main(list[value] args) {
	return visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[value] s => s + { [ d(4,5) ] }
				case a(int x, int y) => a(x + 1000, y + 1000)
				case ABCD nd => { elem = ( 0 | it + i | int i <- nd); a(elem,elem); }
				case int i => i + 100
			} 
			+
			top-down visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case set[value] s => s + { [ d(4,5) ] }
				case a(int x, int y) => b(x + 1000, y + 1000)
				case ABCD nd => { elem = ( 0 | it + i | int i <- nd); a(elem,elem); }
				case int i => i + 100
			}
			;
}