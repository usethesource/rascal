module experiments::Compiler::Examples::Tst6

data LIST = lst(list[int] elems);

value main() {
	return 
		   [ visit(lst([1])) {
				case list[value] l => [ "666" ]
				case list[int] l => l + [ 666 ]
				case int i => 999
		     } ]
		   //==
		   //[ lst([999,666]) ]
		   ;
}