@bootstrapParser
module lang::rascalcore::check::ATypeExceptions

//extend lang::rascalcore::check::AType;

import Exception;
import Message;

data RuntimeException
    = invalidMatch(str reason)
    | invalidInstantiation(str reason)
    | rascalCheckerInternalError(str reason)
    | rascalCheckerInternalError(loc at, str reason)
    
    ;