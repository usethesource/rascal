module experiments::Compiler::Examples::Tst3

import experiments::Compiler::Execute;
import util::Reflective;

value main() = execute("experiments::Compiler::Examples::Tst4", pathConfig(binDir=|home:///bin|, libPath=[|home:///bin|]), recompile=true, trackCalls=true);