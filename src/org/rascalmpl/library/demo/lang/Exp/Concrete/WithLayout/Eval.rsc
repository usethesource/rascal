module demo::lang::Exp::Concrete::WithLayout::Eval
import demo::lang::Exp::Concrete::WithLayout::Syntax;

import String;
import ParseTree;                                                 

public int eval(str txt) = eval(parse(#Exp, txt));              

public int eval((Exp)`<IntegerLiteral l>`) = toInt("<l>");       
public int eval((Exp)`<Exp e1> * <Exp e2>`) = eval(e1) * eval(e2);  
public int eval((Exp)`<Exp e1> + <Exp e2>`) = eval(e1) + eval(e2); 
public int eval((Exp)`( <Exp e> )`) = eval(e);                    

public value main(list[value] args) {
  return eval("2+3");
}

test bool tstEval1() = eval(" 7") == 7;
test bool tstEval2() = eval("7 * 3") == 21;
test bool tstEval3() = eval("7 + 3") == 10;
test bool tstEval3() = eval(" 3 + 4*5 ") == 23;