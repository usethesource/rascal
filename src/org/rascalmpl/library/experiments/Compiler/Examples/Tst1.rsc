module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Execute;
import ParseTree;


value main(list[value] args) =
   execute(|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|, [], recompile=true);