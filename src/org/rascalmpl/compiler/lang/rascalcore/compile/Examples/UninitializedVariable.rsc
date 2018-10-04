module lang::rascalcore::compile::Examples::UninitializedVariable

int globalVar;

str () f() {
    int localVar;
    str trace = "";
    return str () {
    	if(localVar?) {
			trace += " local var is defined: <localVar>!";
		} else {
			localVar = 0;
			trace += " local var is not defined! And now: <localVar>!";
		}
		
		if(globalVar?) {
			trace += " global var is defined: <globalVar>!";
		} else {
			globalVar = 1;
			trace += " global var is not defined! And now: <globalVar>!";
		}
		return trace;
	};
}

value main() {
    int i;
    i = 100;
    str s = f()();
    i = i + 1;
    return s + "; <i>";
}

public value expectedResult = " local var is not defined! And now: 0! global var is not defined! And now: 1!; 101";