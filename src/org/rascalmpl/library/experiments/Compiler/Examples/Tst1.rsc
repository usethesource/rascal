@bootstrapParser
module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Execute;
import ParseTree;


value main() =
   execute(|std:///lang/rascal/tests/basic/Tuples.rsc|, testsuite=true, recompile=true);
   //execute(|std:///experiments/Compiler/Examples/Tst4.rsc|, recompile=true);
  