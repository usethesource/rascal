module Exception

// This data type declares all "soft" exceptions that are thrown by the
// rascal implementation and can be caught by a Rascal program.
// User-defined exceptions should extend this data type.

data Exception = 
	  EmptyListException(str message) 
    | EmptyMapException(str message) 
	| EmptySetException(str message)
    | IndexOutOfBoundsException(str message)
	| IOException(str message)
	| NoSuchAnnotationException(str message)
	| NoSuchFileException(str message)
	| SubscriptException(str message)
	| UndefinedValueException(str message)
	| UninitializedVariableException(str message)
	;