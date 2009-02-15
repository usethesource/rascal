module Error

data Error = 
	| AssertionError(str message)
    | AssignmentError(str message)
	| ClosureInvocationError(str message)
	| EmptyListError(str message) 
	| EmptySetError(str message)
	| ExpressionError(str message)
    | IndexOutOfBoundsError(str message)
	| IOError(str message)
	| NoSuchAnnotationError(str message)
	| NoSuchFieldError(str message)
	| NoSuchFileError(str message)
	| NoSuchFunctionError(str message)
	| NoSuchModuleError(str message)
	| RunTimeError(str message)
	| SubscriptError(str message)
	| SyntaxError(str message)
	| TypeError(str message)
	| UndefinedValueError(str message)
	| UninitializedVariableError(str message)
	;