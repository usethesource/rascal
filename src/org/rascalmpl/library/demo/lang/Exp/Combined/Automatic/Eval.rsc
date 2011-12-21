module demo::lang::Exp::Combined::Automatic::Eval

import demo::lang::Exp::Abstract::Syntax;
import demo::lang::Exp::Abstract::Eval;
import demo::lang::Exp::Combined::Automatic::Load;

public int eval(str txt) = eval(load(txt));
