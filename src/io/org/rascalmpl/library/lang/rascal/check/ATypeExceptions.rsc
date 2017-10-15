module lang::rascal::check::ATypeExceptions

import Exception;
import Message;
import lang::rascal::check::AType;

data RuntimeException
    = invalidMatch(str reason)
    | invalidInstantiation(str reason)
    | rascalCheckerInternalError(str reason)
    ;