module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Execute;
import experiments::Compiler::RVM::AST;

value main(list[value] args) = execute(|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst3.rsc|, [], recompile=true, testsuite=true);
