module experiments::Compiler::Examples::Tst3

import util::Reflective;
import experiments::Compiler::Execute;

value main() = execute("lang::rascal::tests::basic::Lists", pathConfig(binLoc=|home:///bin|, libLocs=[|home:///bin|]), recompile=true, testsuite=true);