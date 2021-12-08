@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalType

import lang::rascalcore::check::AType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascal::\syntax::Rascal;
import ParseTree;

/*
 * translateType: translate a concrete (textual) type description to a Symbol
 */
 
 AType translateType(Type tp) {
    return getType(tp@\loc);
}