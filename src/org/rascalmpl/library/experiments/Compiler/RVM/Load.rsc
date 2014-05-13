module experiments::Compiler::RVM::Load

import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Parse;
import ParseTree;

public RVMProgram loadRVM(str s) = implode(#RVMProgram, parseRVM(s));