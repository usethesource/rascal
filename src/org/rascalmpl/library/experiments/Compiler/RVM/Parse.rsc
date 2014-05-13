module experiments::Compiler::RVM::Parse

import experiments::Compiler::RVM::Syntax;
import ParseTree;

public Tree parseRVM(str s) =  parse(#RascalVM, s);
public Tree parseRVM(loc l) =  parse(#RascalVM, l);
public Tree parseRVM(str s, loc l) =  parse(#RascalVM, s, l);
