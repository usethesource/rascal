module demo::lang::Exp::Abstract::Test
import demo::lang::Exp::Abstract::Syntax;
import demo::lang::Exp::Abstract::Eval;

test bool t1() = eval(con(7)) == 7;
test bool t2() = eval(mul(con(7), con(3))) == 21;
test bool t3() = eval(add(con(7), con(3))) == 10;
test bool t3() = eval(add(con(3), mul(con(4), con(5)))) == 23;
