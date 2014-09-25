module demo::lang::Exp::Concrete::NoLayout::Eval
import demo::lang::Exp::Concrete::NoLayout::Syntax;

import String;
import ParseTree;                                                 /*1*/

public int eval(str txt) = eval(parse(#Exp, txt));                /*2*/

public int eval((Exp)`<IntegerLiteral l>`) = toInt("<l>");        /*3*/
public int eval((Exp)`<Exp e1>*<Exp e2>`) = eval(e1) * eval(e2);  /*4*/
public int eval((Exp)`<Exp e1>+<Exp e2>`) = eval(e1) + eval(e2);  /*5*/
public int eval((Exp)`(<Exp e>)`) = eval(e);                      /*6*/