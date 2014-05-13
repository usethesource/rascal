module experiments::Compiler::Examples::ExceptionHandlingNotHandledSimple

void divide() { 1/0; }

value main(list[value] args) {
	return divide();
}

public str expectedResult = "Runtime exception: ArithmeticException(\"/ by zero\")";