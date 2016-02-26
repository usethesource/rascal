module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Execute;
import util::Reflective;

value main() = execute("experiments::Compiler::Examples::Tst2", pathConfig(binDir=|home:///bin|, libPath=[|home:///bin|]), recompile=true, jvm=true, testsuite=true);
