module experiments::Compiler::Examples::Visit10

data LIST = lst(list[int] elems);

value main(list[value] args) {
	return [ visit(lst([1])) {
				// list[str] <: list[value]; typeOf(subject) <: list[value] 
				// '+' list[str] <: typeOf(subject)
				case list[value] l: insert [ "666" ];
				case list[int] l: { insert l + [ 666 ]; l = l + [ 777 ]; }
				case int i: insert 999;
		     } ]
		   +
		   [ visit(lst([1])) {
				case list[value] l => [ "666" ]
				case list[int] l => l + [ 666 ]
				case int i => 999
		     } ];
}

public value expectedResult = [ lst([999,666]),lst([999,666]) ];