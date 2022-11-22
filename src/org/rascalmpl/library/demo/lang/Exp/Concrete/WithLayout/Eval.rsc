module demo::lang::Exp::Concrete::WithLayout::Eval
import demo::lang::Exp::Concrete::WithLayout::Syntax;

import String;
import ParseTree;                                                   

int eval(str txt) = eval(parse(#start[Exp], txt).top);              

int eval((Exp)`<IntegerLiteral l>`) = toInt("<l>");       
int eval((Exp)`<Exp e1> * <Exp e2>`) = eval(e1) * eval(e2);  
int eval((Exp)`<Exp e1> + <Exp e2>`) = eval(e1) + eval(e2); 
int eval((Exp)`( <Exp e> )`) = eval(e);                    

value main() {
  return eval(" 2+3");
}

test bool tstEval1() = eval(" 7") == 7;
test bool tstEval2() = eval("7 * 3") == 21;
test bool tstEval3() = eval("7 + 3") == 10;
test bool tstEval4() = eval(" 3 + 4*5 ") == 23;
