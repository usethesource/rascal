module experiments::Compiler::Examples::Tst4

import util::Reflective;
import experiments::Compiler::Execute;

value main(){
   pcfg =pathConfig(bin=|home:///bin-tests-intp|, srcs=[|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library|]);

   return execute("experiments::Compiler::Examples::Tst1", pcfg, recompile=true);
}