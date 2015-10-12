module experiments::Compiler::RVM::Load

import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Parse;
import ParseTree;

public RVMModule loadRVM(str s) = implode(#RVMModule, parseRVM(s));