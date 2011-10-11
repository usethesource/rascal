module demo::lang::Func::Load

import demo::lang::Func::Func;
import demo::lang::Func::AST;
import demo::lang::Func::Parse;

import ParseTree;

public demo::lang::Func::AST::Prog implode(demo::lang::Func::Func::Prog p) = 
       implode(#demo::lang::Func::AST::Prog, p);

public demo::lang::Func::AST::Prog load(loc l) = implode(parse(l));
public demo::lang::Func::AST::Prog load(str s) = implode(parse(s));

