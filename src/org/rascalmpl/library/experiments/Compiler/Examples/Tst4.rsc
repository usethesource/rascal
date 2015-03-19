module experiments::Compiler::Examples::Tst4

value main(list[value] args)  { 
	x = 10; 
	return "<while (x > 0) {> <{x -= 1; x; }> <}>" 
	//== 
	//" 9  8  7  6  5  4  3  2  1  0 "
	; 
	return true;
}

