module lang::rascalcore::check::ATypeExceptions

/*
    Declare the exceptions that can be generated during checking.
    Note that TypePal can also generate exceptions, in particular TypeUnavailable(), that are used in the checker.
*/
data RuntimeException
    = invalidMatch(str reason)
    | invalidInstantiation(str reason)
    | rascalCheckerInternalError(str reason)
    | rascalCheckerInternalError(loc at, str reason)
    | nonConstantType()
    ;