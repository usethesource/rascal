module demo::lang::MissGrant::Implode

import demo::lang::MissGrant::Parse;
import demo::lang::MissGrant::MissGrant;
import demo::lang::MissGrant::AST;

import ParseTree;

public demo::lang::MissGrant::AST::Controller implode(demo::lang::MissGrant::MissGrant::Controller pt) =
  implode(#demo::lang::MissGrant::AST::Controller, pt);

public demo::lang::MissGrant::AST::Controller parseAndImplode(str src, loc org) =
  implode(parse(src, org));

public demo::lang::MissGrant::AST::Controller parseAndImplode(loc file) =
  implode(parse(file));
