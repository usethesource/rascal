module experiments::Compiler::Examples::Visit2

value main(list[value] args) {
	return visit( (1:"1",2:"2",3:"3") ) { // ( 306:"1 + 100; 2 + 100; 3 + 100; " )
				case map[int,str] m => ( ( 0 | it + k | int k <- m) : ( "" | it + m[k] | int k <- m) )
				case int i => i + 100
				case str s => s + " + 100; "
			} +
			top-down visit( (1:"1",2:"2",3:"3") ) { // ( 106:"321 + 100; " )
				case map[int,str] m => ( ( 0 | it + k | int k <- m) : ( "" | it + m[k] | int k <- m) )
				case int i => i + 100
				case str s => s + " + 100; "
			};
}