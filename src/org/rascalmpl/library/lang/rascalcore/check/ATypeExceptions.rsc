module lang::rascalcore::check::ATypeExceptions

import Exception;
import Message;
import lang::rascalcore::check::AType;

data RuntimeException
    = invalidMatch(str reason)
    | invalidInstantiation(str reason)
    | rascalCheckerInternalError(str reason)
    ;