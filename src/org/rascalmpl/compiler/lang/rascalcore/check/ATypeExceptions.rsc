module lang::rascalcore::check::ATypeExceptions

data RuntimeException
    = invalidMatch(str reason)
    | invalidInstantiation(str reason)
    | rascalCheckerInternalError(str reason)
    | rascalCheckerInternalError(loc at, str reason)
    
    ;