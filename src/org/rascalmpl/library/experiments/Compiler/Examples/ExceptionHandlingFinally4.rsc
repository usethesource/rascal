module experiments::Compiler::Examples::ExceptionHandlingFinally4

value f() { throw "Try to catch me!"; }

value main(list[value] args) {
	
	str n = "0";
	
	try {
		
		n = n + " 1";
		
		try {
			
			n = n + " 2";
			n = n + " 3";
			
		} catch str s: {
			n = n + " 5";
		} finally {
			n = n + " 6";
		}
		
		n = n + " 7";
		
	} catch "0": {	
		n = n + " 8";
	} catch str s: {
		n = n + " 9";
	} finally {
		n = n + " 10";
	}
	
	return n;
}