module demo::lang::Exp::Combined::Manual::Eval

import demo::lang::Exp::Abstract::Eval;
import demo::lang::Exp::Combined::Manual::Load;

public int eval(str txt) = eval(loadExp(txt));

test bool tstEval1() = eval("7") == 7;
test bool tstEval2() = eval("7*3") == 21;
test bool tstEval3() = eval("7+3") == 10;
test bool tstEval4() = eval("3+4*5") == 23;
