module experiments::Compiler::Examples::ExceptionHandling3

value f() { throw "Try to catch me!"; }
value f(str s) { throw "Try to catch: <s>!"; }

value main(list[value] args) {
	
	str n = "start";
		
	// Example of try/catch blocks followed by each other
	try {
		throw 100;
	// Example of the default catch
	} catch : {
		n = n + ", then default";
	}
	
	try {
		throw 100;
	// Example of an empty, default catch
	} catch : {
		;
	}
	
	try {
	
		try {
	
			try {
		
				n = n + ", then 1";
		
				try {
		
					n = n + ", then 2";
					f();
					n = n + ", then 3"; // dead code
			
				// Example of catch patterns that are incomplete with respect to the 'value' domain
				} catch 0: {		
					n = n + ", then 4";
				} catch 1: {
					n = n + ", then 5";
				} catch "0": {
					n = n + ", then 6";
				}
		
				n = n + ", then 7"; // dead code
		
			// Example of overlapping catch patterns and try/catch block within a catch block
			} catch str s: {
				n = n + ", then 8";
				try {
					n = n + ", then 9";
					f(n); // re-throws
				} catch int i: { // should not catch re-thrown value due to the type
					n = n + ", then 10";
				}
				n = n + ", then 11";
			} catch value v: {
				n = n + ", then 12";
			}
	
		// The outer try block of the catch block that throws an exception (should not catch due to the type)
		} catch int i: {
			n = n + ", then 13";
		}
	// The outer try block of the catch block that throws an exception (should catch)
	} catch value v: {
		n = "<v>, then last catch and $$$";
	}
	
	return n; // "Try to catch: start, then default, then 1, then 2, then 8, then 9!, then last catch and $$$"
	
}