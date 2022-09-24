module demo::lang::Exp::Combined::Manual::Parse
import demo::lang::Exp::Concrete::WithLayout::Syntax;
import ParseTree;

demo::lang::Exp::Concrete::WithLayout::Syntax::Exp
 parseExp(str txt) = parse(#Exp, txt); 
