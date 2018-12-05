module lang::rascalcore::compile::muRascal::interpret::RValue

import lang::rascalcore::check::AType;

data RValue
     = undefined()
     | rvalue(value val)
     | rtype(AType tp)
     ;
 
 RValue rvalue(rvalue(v)) = rvalue(v);