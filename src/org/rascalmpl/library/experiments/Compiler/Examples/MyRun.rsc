module experiments::Compiler::Examples::MyRun

import Prelude;
import experiments::Compiler::Execute;

loc base = |rascal:///experiments/Compiler/Examples/|;

value demo(str example, bool debug = false, bool listing=false, bool testsuite=false, bool recompile=true, bool profile=false) =
  execute(base + (example + ".rsc"), [], debug=debug, listing=listing, testsuite=testsuite, recompile=recompile, profile=profile);
