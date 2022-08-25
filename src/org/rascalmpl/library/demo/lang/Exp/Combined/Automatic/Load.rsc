// tag::module[]
module demo::lang::Exp::Combined::Automatic::Load

import demo::lang::Exp::Combined::Automatic::Parse; // <1>
import demo::lang::Exp::Abstract::Syntax; // <2>
import ParseTree; // <3>

Exp load(str txt) = implode(#Exp, parseExp(txt)); 
// end::module[]
