module experiments::Compiler::Examples::Tst1
import demo::lang::Exp::Concrete::NoLayout::Syntax;

import String;
import ParseTree;                                                 /*1*/

int eval(str txt) = eval(parse(#Exp, txt));                /*2*/

int eval((Exp)`<IntegerLiteral l>`) = toInt("<l>");        /*3*/
int eval((Exp)`<Exp e1>*<Exp e2>`) = eval(e1) * eval(e2);  /*4*/
int eval((Exp)`<Exp e1>+<Exp e2>`) = eval(e1) + eval(e2);  /*5*/
int eval((Exp)`(<Exp e>)`) = eval(e);                      /*6*/

value main(list[value] args)  = eval("7+8")
;
