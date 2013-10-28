module experiments::Compiler::Examples::Visit11

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);

value main(list[value] args) {
	
	res = visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
				case a(x,y) => a(x + 1000, y + 2000)
			}
		;
		
	return res;
}