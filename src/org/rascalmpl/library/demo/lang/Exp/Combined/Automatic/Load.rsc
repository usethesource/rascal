module demo::lang::Exp::Combined::Automatic::Load
import demo::lang::Exp::Combined::Automatic::Syntax;
import demo::lang::Exp::Abstract::Syntax;
import demo::lang::Exp::Combined::Automatic::Parse;

import ParseTree;

public demo::lang::Exp::Abstract::Syntax::Exp load(str txt) = 
       implode(#demo::lang::Exp::Abstract::Syntax::Exp, parse(txt)); 
