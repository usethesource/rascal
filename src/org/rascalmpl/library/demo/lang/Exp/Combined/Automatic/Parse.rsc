module demo::lang::Exp::Combined::Automatic::Parse

import demo::lang::Exp::Combined::Automatic::Syntax;
import ParseTree;

public Tree parseExp(str txt) = parse(#Exp, txt); 
