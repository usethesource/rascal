module experiments::Compiler::Examples::ExceptionHandlingFinally7

value f() { throw "Try to catch me!"; }

value main(list[value] args) {
	
	str n = "0";
	
	try {
	
		try {
		
			n = n + " 1";
		
			try {
			
				n = n + " 2";
				f();          // Throws
				n = n + " 3"; // dead code
						
			} catch 0: { // Does not match!
				n = n + " 4";
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
		} catch str s: { // Does match!
			n = n + " 9";
		} finally {
			n = n + " 10";
		}
		
		n = n + " 11";
		f();            // Throws again!
		n = n + " 12";
		
	} catch: {
		n = n + " 13";
	} finally {
		// Inline in a 'finally' block
		n = n + " and last finally";
	}
	
	n = n + ", then before return!";
	
	return n; // result: 0 1 2 6 9 10 11 13 and last finally, then before return!
}