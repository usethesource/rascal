module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Compile;

value main(list[value] args) =
 compile(|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|);