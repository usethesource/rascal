module experiments::Compiler::Examples::ExceptionHandlingFinally6

value f() { throw "Try to catch me!"; }

value main(list[value] args) {
	
	str n = "0";
	
	try {
	
		try {
		
			n = n + " 1";
		
			try {
			
				n = n + " 2";
				f();
				n = n + " 3"; // dead code
						
			} catch 0: {
				n = n + " 4";
				// Inline in a 'catch' block: does not match!
				return n;
			} catch int i: {
				n = n + " 5";
				// Inline in 'catch' block: does not match!
				return n + " has been returned from the inner catch!";
			} finally {
				n = n + " 6";
			}
		
			n = n + " 7";
		
		} catch "0": {	
			n = n + " 8";
		} catch str s: {
			n = n + " 9";
			// Inline in 'catch' block: does match!
			return n + " has been returned from the outer catch!";
		} finally {
			n = n + " 10";
			// Inline in a 'finally' block
			return n + " has been returned from the outer finally!";
		}
	} catch: {
		;
	} finally {
		// Inline in a 'finally' block
		n = n + " and last finally";
		return n;
	}
	
	return n;
}