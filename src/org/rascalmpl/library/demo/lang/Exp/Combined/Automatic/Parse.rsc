module demo::lang::Exp::Combined::Automatic::Parse
import demo::lang::Exp::Concrete::WithLayout::Syntax;

import ParseTree;

public Exp parse(str txt) = parse(#Exp, txt); 
