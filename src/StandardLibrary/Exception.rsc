module Exception

// This data type declares all exceptions that are thrown by the
// Rascal run-time environment which can be caught by a Rascal program.

data RuntimeException = 
	  EmptyList
    | EmptyMap 
	| EmptySet
    | IndexOutOfBounds(int index)
    | AssertionFailed 
    | AssertionFailed(str label)
    | NoSuchElement(value v)
    | IllegalArgument(value v)
    | IllegalArgument
	| IO(str message)
	| FileNotFound(str filename)
	| LocationNotFound(loc location)
	| PermissionDenied
	| PermissionDenied(str message)
	| ModuleNotFound(str name)
	| NoSuchKey(value key);
	| NoSuchAnnotation(str label)
	| Java(str message)
	;
