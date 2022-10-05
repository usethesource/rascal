module demo::lang::Func::Load

import demo::lang::Func::Func;
import demo::lang::Func::AST;
import demo::lang::Func::Parse;

import ParseTree;

demo::lang::Func::AST::Prog implode(demo::lang::Func::Func::Prog p) = 
    implode(#demo::lang::Func::AST::Prog, p);

demo::lang::Func::AST::Prog load(loc l) = implode(parse(l));
demo::lang::Func::AST::Prog load(str s) = implode(parse(s));
