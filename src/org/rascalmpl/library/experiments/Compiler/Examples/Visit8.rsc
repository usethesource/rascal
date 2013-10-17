module experiments::Compiler::Examples::Visit8

value main(list[value] args) {
	
	l = [ 1,0,1,1,0,1,0,1,0,1,0 ]; // 11

	return outermost visit({ l }) {
		case [*int sub, 1, 0] => [ 10, *sub ]
		case 10 => 20
	}
}