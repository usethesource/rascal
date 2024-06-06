module lang::rascal::tests::basic::Exceptions

import List;
import Exception;
import String;

test bool exceptionHandling1(){
	value f() { throw "Try to catch me!"; }
	
	str n = "0";
	
	try {
		n = n + " 1";
		try {
			n = n + " 2";
			f();
			n = n + " 3"; // dead code
		} catch 0: {
			n = n + " 4";
		} catch int _: {
			n = n + " 5";
		}
		n = n + " 6";
	} catch "0": {
		n = n + " 7";
	} catch str _: {
		n = n + " 8";
	}
	return n == "0 1 2 8";
}

test bool exceptionHandling2(){
	value f() { throw "Try to catch me!"; }
	value f(str s) { throw "Try to catch: <s>!"; }
	
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
		} catch str _: {
			n = n + ", then 8";
			try {
				n = n + ", then 9";
				f(n);
			} catch int _: {
				n = n + ", then 10";
			}
			n = n + ", then 11";
		} catch value _: {
			n = n + ", then 12";
		}
	
	} catch value v: {
		n = "<v>, then last catch and $$$";
	}
	
	return n == "Try to catch: start, then default, then 1, then 2, then 8, then 9!, then last catch and $$$";
}

test bool exceptionHandling3(){

	value f() { throw "Try to catch me!"; }
	value f(str s) { throw "Try to catch: <s>!"; }
	
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
			} catch str _: {
				n = n + ", then 8";
				try {
					n = n + ", then 9";
					f(n); // re-throws
				} catch int _: { // should not catch re-thrown value due to the type
					n = n + ", then 10";
				}
				n = n + ", then 11";
			} catch value _: {
				n = n + ", then 12";
			}
	
		// The outer try block of the catch block that throws an exception (should not catch due to the type)
		} catch int _: {
			n = n + ", then 13";
		}
	// The outer try block of the catch block that throws an exception (should catch)
	} catch value v: {
		n = "<v>, then last catch and $$$";
	}
	
	return n == "Try to catch: start, then default, then 1, then 2, then 8, then 9!, then last catch and $$$";
}

test bool exceptionHandling4(){
    try { 
        head([]); 
    } catch EmptyList():
        return true;
    return false; 
}


test bool exceptionHandlingFinally1(){
	str main(){
		str n = "0";
		
		try {
			n = n + " 1";
			try {
				n = n + " 2";
				// Inline in a 'try' block
				return n + " has been returned!";
			} catch 0: {		
				n = n + " 4";
			} catch int _: {
				n = n + " 5";
			} finally {
				n = n + " 6";
				// Inline in a 'finally' block
				return n + " has been returned from the inner finally!";
			}
		} catch "0": {	
			n = n + " 8";
		} catch str _: {
			n = n + " 9";
		} finally {
			n = n + " 10";
		}
		return "no case matched";
	}
	
	return main() == "0 1 2 6 has been returned from the inner finally!";
}

test bool exceptionHandlingFinally2(){
	str main() {
		str n = "0";
		try {
			n = n + " 1";
			try {
				n = n + " 2";
				// Inline in a 'try' block
				return n + " has been returned!";
			} catch 0: {		
				n = n + " 4";
			} catch int _: {
				n = n + " 5";
			} finally {
				n = n + " 6";
				// Inline in a 'finally' block
				return n + " has been returned from the inner finally!";
			}
		} catch "0": {	
			n = n + " 8";
		} catch str _: {
			n = n + " 9";
		} finally {
			n = n + " 10";
			// Inline in a 'finally' block
			return n + " has been returned from the outer finally!";
		}
	}
	
	return main() == "0 1 2 6 10 has been returned from the outer finally!";
}

test bool exceptionHandlingFinally3(){
	str main() {
		str n = "0";
		
		try { // 'Try-catch-finally'
			n = n + " 1";
			try { // 'Try-catch'
				n = n + " 2";
				// Inline in a 'try' block
				return n + " has been returned!";
			} catch 0: {		
				n = n + " 4";
			} catch int _: {
				n = n + " 5";
			}		
			n = n + " 7";
			return " should not come here";
		} catch "0": {	
			n = n + " 8";
		} catch str _: {
			n = n + " 9";
		} 
		finally {
			n = n + " 10";
			// Inline in a 'finally' block
			return n + " has been returned from the outer finally!";
		}
		
	}
	return main() == "0 1 2 10 has been returned from the outer finally!";
}

test bool exceptionHandlingFinally4(){
	str main() {
		str n = "0";
		// No exceptions and no returns
		try {
			n = n + " 1";
			try {
				n = n + " 2";
				n = n + " 3";
			} catch str _: {
				n = n + " 5";
			} finally {
				n = n + " 6";
			}
			n = n + " 7";
		} catch "0": {	
			n = n + " 8";
		} catch str _: {
			n = n + " 9";
		} finally {
			n = n + " 10";
		}
		return n;
	}
	return main() == "0 1 2 3 6 7 10";
}	

test bool exceptionHandlingFinally5(){
	value f() { throw "Try to catch me!"; }
	
	str main() {
		str n = "0";
		
		try {
			n = n + " 1";
			try {
				n = n + " 2";
				f();
				n = n + " 3"; // dead code
				return " should not come here";
			} catch "0": {
				n = n + " 4";
				// Inline in a 'catch' block
				return n;
			} catch str _: {
				n = n + " 5";
				// Inline in 'catch' block
				return n + " has been returned from the inner catch!";
			} finally {
				n = n + " 6";
				// Inline in a 'finally' block
				return n + " has been returned from the inner finally!";
			}
		} catch "0": {	
			n = n + " 8";
		} catch str _: {
			n = n + " 9";
		} finally {
			n = n + " 10";
			// Inline in a 'finally' block
			return n + " has been returned from the outer finally!";
		}
	}
	return main() == "0 1 2 5 6 10 has been returned from the outer finally!";
}	

test bool exceptionHandlingFinally6(){
	value f() { throw "Try to catch me!"; }
	
	str main() {
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
				} catch int _: {
					n = n + " 5";
					// Inline in 'catch' block: does not match!
					return n + " has been returned from the inner catch!";
				} finally {
					n = n + " 6";
				}
				n = n + " 7";
				return " should not come here";
			} catch "0": {	
				n = n + " 8";
			} catch str _: {
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
	}
	return main() == "0 1 2 6 9 10 and last finally";
}	

test bool exceptionHandlingFinally7(){
	value f() { throw "Try to catch me!"; }
	
	str main() {
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
				} catch int _: {
					n = n + " 5";
					// Inline in 'catch' block: does not match!
					return n + " has been returned from the inner catch!";
				} finally {
					n = n + " 6";
				}
				n = n + " 7";
			} catch "0": {	
				n = n + " 8";
			} catch str _: { // Does match!
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
	return main() == "0 1 2 6 9 10 11 13 and last finally, then before return!";
}

test bool exceptionHandlingFinally8(){	
	value f() { throw "Try to catch me!"; }
	
	str main() {
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
				} catch int _: {
					n = n + " 5";
					// Inline in 'catch' block: does not match!
					return n + " has been returned from the inner catch!";
				} finally {
					n = n + " 6";
				}
				n = n + " 7";
			} catch "0": {	
				n = n + " 8";
			} catch str _: { // Does match!
				n = n + " 9";
			} finally {
				n = n + " 10";
				f();        // Throws again!
				n = n + " 11";
			}
			n = n + " 12";
			f();            // Throws again, but unreachable
			n = n + " 13";
		} catch value _: {
			n = n + " 14";
		} finally {
			// Inline in a 'finally' block
			n = n + " and last finally";
		}
		n = n + ", then before return!";
		return n; // result: 0 1 2 6 9 10 14 and last finally, then before return!
	}
	return main() == "0 1 2 6 9 10 14 and last finally, then before return!";
}

test bool exceptionHandlingNotHandled(){
	value f() { throw "Try to catch me!"; }
	value f(str s) { throw "Try to catch: <s>!"; }
	
	str main() {
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
			} catch str _: {
				n = n + ", then 8";
				try {
					n = n + ", then 9";
					f(n);
				} catch int _: {
					n = n + ", then 10";
				}
				n = n + ", then 11";
			}
		} catch int _: {
			n = n + ", then 13";
		}
		return n;
	}
	
	try {
		main();
		return false;
	} 
	catch str s: {
		return s == "Try to catch: start, then 1, then 2, then 8, then 9!";
	}
}

test bool exceptionHandlingNotHandledSimple1(){
	void divide() { 1/0; }

	void main() {
		divide();
	}
	
	try {
		main();
		return false;
    } 
    catch value v: {
		return ArithmeticException(str msg) := v && endsWith(msg, "by zero"); 
    }
}

test bool rascalException1() {
	map[int,str] m = (0:"0", 1:"1",2:"2");
	
	str trace = "";
	
	try {
		m[3];
	} catch NoSuchKey(k): {
		trace = trace + "<k> (not found)";
	} finally {
		trace = "map key: " + trace;
	}
	return trace == "map key: 3 (not found)";
}

test bool rascalRuntimeExceptionsPlusOverloading(){
	str trace = "";

	void f(int _) {
	    map[int,str] m = (0:"0", 1:"1",2:"2");
	    trace = trace + "Bad function f; ";
	    m[3];
	}
	
	void g(0) {
	    try {
	        return f(0);
	    } catch NoSuchKey(k): {
			trace = trace + "map key: <k> (not found); ";
		} finally {
			trace = trace + "finally; ";
		}
		fail;
	}
	
	default void g(int i) {
	    trace = trace + "default void g(int);";
	}
	
	trace = "";
	g(0);
	return trace == "Bad function f; map key: 3 (not found); finally; default void g(int);";
}

test bool untypedCatch1() {
    x = 1;
	try {
		throw "exception";
	} 
	catch _: x += 1;
	return x == 2;
}

test bool untypedCatch2() {
    x = 1;
	try {
		throw "exception";
	} 
	catch int _: return false;
	catch _:     x += 1;
	return x == 2;
}

test bool untypedCatch3() {
    x = 1;
	try {
		throw "exception";
	} 
	catch int _: return false;
	catch _:     x += 1;
	finally;
	
	return x == 2;
}		

test bool definedVarCatch() {
    x = 1;
    try {
        throw 1;
    } 
    catch x:     x += 1;
    finally;
    
    return x == 2;
}   

test bool valueCatch() {
    x = 1;
    try {
        throw "exception";
    } 
    catch value _:     x += 1;
    finally;
    
    return x == 2;
}           
    	
