@bootstrapParser
module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Execute;
import ParseTree;


value main() =
   execute(|std:///experiments/Compiler/Examples/Tst2.rsc|, recompile=true);