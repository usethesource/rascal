module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Execute;
import ParseTree;

import util::Reflective;

value main() = execute("lang::rascal::tests::basic::Booleans", pathConfig(binDir=|home:///bin|, libPath=[|home:///bin|]), recompile=true);