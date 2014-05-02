module experiments::Compiler::Examples::ExceptionHandlingNotHandled

value f() { throw "Try to catch me!"; }
value f(str s) { throw "Try to catch: <s>!"; }

value main(list[value] args) {
	
	str n = "start";
	
	try {
	
		try {
		
			n = n + ", then 1";
		
			try {
		
				n = n + ", then 2";
				f();
				n = n + ", then 3"; // dead code
			
			} catch 0: {		
				n = n + ", then 4";
			} catch 1: {
				n = n + ", then 5";
			} catch "0": {
				n = n + ", then 6";
			}
		
			n = n + ", then 7"; // dead code
		
		} catch str s: {
			n = n + ", then 8";
			try {
			
				n = n + ", then 9";
				f(n);
				
			} catch int i: {
				n = n + ", then 10";
			}
			n = n + ", then 11";
		}
	
	} catch int i: {
		n = n + ", then 13";
	}
	
	return n;
	
}

public str expectedResult = "Runtime exception: \"Try to catch: start, then 1, then 2, then 8, then 9!\"";
