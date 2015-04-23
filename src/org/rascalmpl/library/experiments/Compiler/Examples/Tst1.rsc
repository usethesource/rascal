module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Compile;
import experiments::Compiler::RVM::AST;

value main(list[value] args) = compile(|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst3.rsc|);
