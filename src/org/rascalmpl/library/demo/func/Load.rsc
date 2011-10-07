module demo::func::Load

import demo::func::Func;
import demo::func::AST;
import demo::func::Parse;

import ParseTree;

public demo::func::AST::Prog implode(demo::func::Func::Prog p) = implode(#demo::func::AST::Prog, p);

public demo::func::AST::Prog load(loc l) = implode(parse(l));
public demo::func::AST::Prog load(str s) = implode(parse(s));

