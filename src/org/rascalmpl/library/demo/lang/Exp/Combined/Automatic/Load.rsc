module demo::lang::Exp::Combined::Automatic::Load

import demo::lang::Exp::Combined::Automatic::Parse;  /*1*/
import demo::lang::Exp::Abstract::Syntax;            /*2*/
import ParseTree;                                    /*3*/

public demo::lang::Exp::Abstract::Syntax::Exp load(str txt) = 
       implode(#demo::lang::Exp::Abstract::Syntax::Exp, parse(txt)); 
