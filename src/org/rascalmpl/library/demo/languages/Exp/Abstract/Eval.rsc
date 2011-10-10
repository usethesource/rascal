module demo::languages::Exp::Abstract::Eval
import demo::languages::Exp::Abstract::Syntax;

public int eval(con(int n)) = n;                            /*1*/
public int eval(mul(Exp e1, Exp e2)) = eval(e1) * eval(e2); /*2*/
public int eval(add(Exp e1, Exp e2)) = eval(e1) + eval(e2); /*3*/