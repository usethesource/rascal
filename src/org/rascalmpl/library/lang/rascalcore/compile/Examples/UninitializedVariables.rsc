module lang::rascalcore::compile::Examples::UninitializedVariables

public int globalVar;

public str expectedResult = " global var is not defined! global var is defined: 0! local var is not defined! local var is defined: 2!";

public value main() {
	int localVar;
	
	str trace = "";
	
	if(globalVar?) {
		trace += " global var is defined: <globalVar>!";
	} else {
		trace += " global var is not defined!";
		globalVar = 0;
	}
	
	if(globalVar?) {
		trace += " global var is defined: <globalVar>!";
	} else {
		trace += " global var is not defined!";
		globalVar = 1;
	}
	
	if(localVar?) {
		trace += " local var is defined: <localVar>!";
	} else {
		trace += " local var is not defined!";
		localVar = 2;
	}
	
	if(localVar?) {
		trace += " local var is defined: <localVar>!";
	} else {
		trace += " local var is not defined!";
		localVar = 3;
	}
	
	return trace;
}