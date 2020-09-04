@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalType

import lang::rascalcore::check::AType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascal::\syntax::Rascal;
import IO;

/*
 * translateType: translate a concrete (textual) type description to a Symbol
 */
 
 AType translateType(Type tp) {
    println("translateType: <tp>");
    iprintln(tp);
    return getType(tp@\loc);
}