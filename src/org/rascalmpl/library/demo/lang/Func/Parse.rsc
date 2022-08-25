// tag::module[]
module demo::lang::Func::Parse

import demo::lang::Func::Func;
import ParseTree;

Prog parse(loc l) = parse(#Prog, l);
Prog parse(str s) = parse(#Prog, s);
// end::module[]
