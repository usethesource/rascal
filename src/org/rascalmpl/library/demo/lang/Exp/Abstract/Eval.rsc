module demo::lang::Exp::Abstract::Eval
import demo::lang::Exp::Abstract::Syntax;

int eval(con(int n)) = n;                            // <1>
int eval(mul(Exp e1, Exp e2)) = eval(e1) * eval(e2); // <2>
int eval(add(Exp e1, Exp e2)) = eval(e1) + eval(e2); // <3>

test bool tstEval1() = eval(con(7)) == 7;
test bool tstEval2() = eval(mul(con(7), con(3))) == 21;
test bool tstEval3() = eval(add(con(7), con(3))) == 10;
test bool tstEval4() = eval(add(con(3), mul(con(4), con(5)))) == 23;
