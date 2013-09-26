module experiments::Compiler::Examples::ExceptionHandlingFinally5

value f() { throw "Try to catch me!"; }

value main(list[value] args) {
	
	str n = "0";
	
	try {
		
		n = n + " 1";
		
		try {
			
			n = n + " 2";
			f();
			n = n + " 3"; // dead code
						
		} catch "0": {
			n = n + " 4";
			// Inline in a 'catch' block
			return n;
		} catch str s: {
			n = n + " 5";
			// Inline in 'catch' block
			return n + " has been returned from the inner catch!";
		} finally {
			n = n + " 6";
			// Inline in a 'finally' block
			return n + " has been returned from the inner finally!";
		}
		
		n = n + " 7";
		
	} catch "0": {	
		n = n + " 8";
	} catch str s: {
		n = n + " 9";
	} finally {
		n = n + " 10";
		// Inline in a 'finally' block
		return n + " has been returned from the outer finally!";
	}
	
	return n;
}