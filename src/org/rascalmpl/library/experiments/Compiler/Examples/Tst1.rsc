
module experiments::Compiler::Examples::Tst1

import util::Reflective;
import experiments::Compiler::Execute;

value main() =  execute("experiments::Compiler::Examples::Fac", pathConfig(binDir=|home:///bin|, libPath=[|home:///bin|]), recompile=true, jvm=true);
    
 
