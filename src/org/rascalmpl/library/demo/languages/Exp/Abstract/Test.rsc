module demo::languages::Exp::Abstract::Test
import demo::languages::Exp::Abstract::Syntax;
import demo::languages::Exp::Abstract::Eval;

public test bool t1() = eval(con(7)) == 7;
public test bool t2() = eval(mul(con(7), con(3))) == 21;
public test bool t3() = eval(add(con(7), con(3))) == 10;
public test bool t3() = eval(add(con(3), mul(con(4), con(5)))) == 23;
