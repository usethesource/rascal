module lang::rascalcore::compile::Examples::Tst1

import Grammar;
//import lang::rascal::grammar::definition::Parameters;
//import lang::rascal::grammar::definition::Regular;
//import lang::rascal::grammar::definition::Productions;
//import lang::rascal::grammar::definition::Modules;
//import lang::rascal::grammar::definition::Priorities;
//import lang::rascal::grammar::definition::Literals;
//import lang::rascal::grammar::definition::Symbols;
//import lang::rascal::grammar::definition::Keywords;
//import lang::rascal::grammar::Lookahead;

//import util::Monitor;
import lang::rascal::\syntax::Rascal;
//import lang::rascal::grammar::ConcreteSyntax;
import ParseTree;
//import String;
//import List;
//import Node;
//import Set;

data Symbol(int id = 0, str prefix = "");

int getItemId(Symbol s, int pos, prod(label(str l, Symbol _),list[Symbol] _, set[Attr] _)) {
  switch (s) {
    case Symbol::\opt(t) : return t.id; 
    case Symbol::\iter(t) : return t.id;
    case Symbol::\iter-star(t) : return t.id; 
    case Symbol::\iter-seps(t,_) : if (pos == 0) return t.id; else fail;
    case Symbol::\iter-seps(_,ss) : if (pos > 0)  return ss[pos-1].id; else fail;
    case Symbol::\iter-star-seps(t,_) : if (pos == 0) return t.id; else fail;
    case Symbol::\iter-star-seps(_,ss) : if (pos > 0) return ss[pos-1].id; else fail;
    case Symbol::\seq(ss) : return ss[pos].id;
    // note the use of the label l from the third function parameter:
    case Symbol::\alt(aa) : if (a:conditional(_,{*_,except(l)}) <- aa) return a.id; 
    default: return s.id; // this should never happen, but let's make this robust
  } 
  throw "getItemId: no case for <s>";
}
