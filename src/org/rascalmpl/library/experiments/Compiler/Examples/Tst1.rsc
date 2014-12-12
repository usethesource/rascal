module experiments::Compiler::Examples::Tst1

import demo::lang::Exp::Concrete::WithLayout::Syntax;

import ParseTree; 
import IO;   
                                                             
public value main(list[value] args) {
  //return #start[Exp];
  return parse(#start[Exp], " 7");
}