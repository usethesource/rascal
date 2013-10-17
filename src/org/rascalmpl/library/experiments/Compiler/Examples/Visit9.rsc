module experiments::Compiler::Examples::Visit9

value main(list[value] args) {
	l = [ 1,0,1,0,1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,0,1,0 ];

	return outermost visit({ l }) {
		case [*int sub, 1, 0] => [ 998, *sub ]
		case [*int sub, 1, 1] => [ 666, *sub ]
		case 998 => 999
	}
	
}