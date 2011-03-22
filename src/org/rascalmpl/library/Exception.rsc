module Exception

/*
 * This data type declares all exceptions that are thrown by the
 * Rascal run-time environment which can be caught by a Rascal program.
 */

data RuntimeException = 
      EmptyList()
    | EmptyMap() 
    | EmptySet()
    | IndexOutOfBounds(int index)
    | AssertionFailed() 
    | AssertionFailed(str label)
    | NoSuchElement(value v)
    | IllegalArgument(value v)
    | IllegalArgument()
    | IO(str message)
    | PathNotFound(loc l)
    | FileNotFound(str file)
    | SchemeNotSupported(loc l)
    | HostNotFound(loc l)
    | AccessDenied(loc l)
    | PermissionDenied()
    | PermissionDenied(str message)
    | ModuleNotFound(str name)
    | NoSuchKey(value key)
    | NoSuchAnnotation(str label)
    | Java(str message)
    | ParseError(loc location)
    | IllegalIdentifier(str name)
    | MissingCase(value x)
    | Subversion(str message)
    | Timeout()
	;
