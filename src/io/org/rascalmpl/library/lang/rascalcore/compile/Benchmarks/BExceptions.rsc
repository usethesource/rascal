module lang::rascalcore::compile::Benchmarks::BExceptions

value f() { throw "Try to catch me!"; }
value f(str s) { throw "Try to catch: <s>!"; }

value tryToCatchMe() {
	
	str n = "start";
		
	try {
		throw 100;
	} catch : {
		n = n + ", then default";
	}
	
	try {
		throw 100;
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
		} catch value v: {
			n = n + ", then 12";
		}
	
	} catch value v: {
		n = "<v>, then last catch and $$$";
	}
	
	return n; // "Try to catch: start, then default, then 1, then 2, then 8, then 9!, then last catch and $$$"
}

value main(){
	value x;
    for(i <- [1 .. 50000]){
       x = tryToCatchMe();
    }
    return x;
}