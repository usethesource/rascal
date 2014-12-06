module experiments::Compiler::Examples::Tst

import lang::rascal::\syntax::tests::ImplodeTestGrammar;
import ParseTree;
import IO;

public data Num = \int(str n);
public data Exp = id(str name) | eq(Exp e1, Exp e2) | number(Num n);

public Exp number(Num::\int("0")) = Exp::number(Num::\int("01"));

public anno loc Num@location;
public anno loc Exp@location;
public anno map[int,list[str]] Num@comments;
public anno map[int,list[str]] Exp@comments;


public Exp implodeExp(str s) { pt = parseExp(s); println(pt); return implode(#Exp, pt);}


value main(list[value] args) = implodeExp("0");

