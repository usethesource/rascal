module experiments::Compiler::Examples::Tst5


import demo::lang::Exp::Abstract::Syntax;
import demo::lang::Exp::Abstract::Eval;
import demo::lang::Exp::Combined::Automatic::Load;

public int eval(str txt) = eval(load(txt));

value main(list[value] args) = eval("7") == 7;
