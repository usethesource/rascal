module Exception

data Exception = 
	| AssertionFails(str message)
    | AssignmentError(str message)
	| ClosureInvocationError(str message)
	| EmptyList(str message) 
	| EmptySet(str message)
	| ExpressionError(str message)
    | IndexOutOfBounds(str message)
	| IOError(str message)
	| NoSuchAnnotation(str message)
	| NoSuchField(str message)
	| NoSuchFile(str message)
	| NoSuchFunction(str message)
	| NoSuchModule(str message)
	| SyntaxError(str message)
	| TypeError(str message)
	| UninitializedVariable(str message)
	;