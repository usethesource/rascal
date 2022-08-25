// tag::module[]
module demo::lang::Exp::Concrete::NoLayout::Eval
import demo::lang::Exp::Concrete::NoLayout::Syntax;

import String;
import ParseTree; // <1>

int eval(str txt) = eval(parse(#Exp, txt)); // <2>

int eval((Exp)`<IntegerLiteral l>`) = toInt("<l>");       // <3>
int eval((Exp)`<Exp e1>*<Exp e2>`) = eval(e1) * eval(e2); // <4>
int eval((Exp)`<Exp e1>+<Exp e2>`) = eval(e1) + eval(e2); // <5>
int eval((Exp)`(<Exp e>)`) = eval(e);                     // <6>
// end::module[]

test bool tstEval1() = eval("7") == 7;
test bool tstEval2() = eval("7*3") == 21;
test bool tstEval3() = eval("7+3") == 10;
test bool tstEval4() = eval("3+4*5") == 23;
