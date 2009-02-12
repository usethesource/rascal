module Exception

data Exception = 
	  OutOfBounds(str message)
	| EmptyList(str message) 
	| ArithmeticOperation(str message)
	| FileNotFound(str message)
	| IOError(str message)
	| TypecheckError(str message)
	;