module demo::lang::Exp::Combined::Automatic::Load
import demo::lang::Exp::Combined::Syntax;
import demo::lang::Exp::Abstract::Syntax;
import demo::lang::Exp::Combined::Manual::Parse;

import ParseTree;

public demo::lang::Exp::Abstract::Syntax::Exp load(str txt) = 
        implode(#demo::lang::Exp::Abstract::Syntax::Exp, parse(txt)); 
