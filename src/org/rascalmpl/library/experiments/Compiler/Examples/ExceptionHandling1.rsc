module experiments::Compiler::Examples::ExceptionHandling1

value f() { throw "Try to catch me!"; }

value main(list[value] args) {
	
	str n = "0";
	
	try {
		
		n = n + " 1";
		
		try {
		
			n = n + " 2";
			f();
			n = n + " 3"; // dead code
			
		} catch 0: {
		
			n = n + " 4";
			
		} catch int i: {
		
			n = n + " 5";
			
		}
		
		n = n + " 6";
		
	} catch "0": {
	
		n = n + " 7";
		
	} catch str s: {
	
		n = n + " 8";
		
	}
	
	return n; // "0 1 2 8"
	
}