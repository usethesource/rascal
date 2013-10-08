module experiments::Compiler::Examples::ExceptionHandlingNotHandledSimple

void divide() { 1/0; }

value main(list[value] args) {
	return divide();
}

public str expectedResult = "Runtime exception \<currently unknown location\>: ArithmeticException(\"/ by zero\")";