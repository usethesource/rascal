module demo::lang::Exp::Combined::Manual::Eval

import demo::lang::Exp::Abstract::Syntax;
import demo::lang::Exp::Abstract::Eval;
import demo::lang::Exp::Combined::Manual::Load;

public int eval(str txt) = eval(loadExp(txt));
