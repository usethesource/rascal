module demo::lang::Exp::Combined::Automatic::Load

import demo::lang::Exp::Combined::Automatic::Syntax; /*1*/
import demo::lang::Exp::Combined::Automatic::Parse;  /*2*/
import demo::lang::Exp::Abstract::Syntax;            /*3*/
import ParseTree;

public demo::lang::Exp::Abstract::Syntax::Exp load(str txt) = 
       implode(#demo::lang::Exp::Abstract::Syntax::Exp, parse(txt)); 
