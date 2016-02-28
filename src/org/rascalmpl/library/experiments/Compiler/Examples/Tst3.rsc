module experiments::Compiler::Examples::Tst3

import experiments::Compiler::Execute;

value main() = execute("experiments::Compiler::Examples::Fac", pathConfig(binDir=|home:///bin|, libPath=[|home:///bin|]), recompile=true, jvm=true);