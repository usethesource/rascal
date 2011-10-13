module demo::lang::Exp::Combined::Manual::Parse
import demo::lang::Exp::Concrete::WithLayout::Syntax;
import ParseTree;

public Exp parse(str txt) = parse(#Exp, txt); 
