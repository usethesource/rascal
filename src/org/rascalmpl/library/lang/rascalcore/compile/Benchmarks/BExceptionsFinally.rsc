module lang::rascalcore::compile::Benchmarks::BExceptionsFinally

value f() { throw "Try to catch me!"; }

value tryToCatchMe() {
	
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
			f();        // Throws again!
			n = n + " 11";
		}
		
		n = n + " 12";
		f();            // Throws again, but unreachable
		n = n + " 13";
		
	} catch value v: {
		n = n + " 14";
	} finally {
		// Inline in a 'finally' block
		n = n + " and last finally";
	}
	
	n = n + ", then before return!";
	
	return n; // result: 0 1 2 6 9 10 14 and last finally, then before return!
}

value main(){
	value x;
    for(i <- [1 .. 100000]){
       x = tryToCatchMe();
    }
    return x;
}