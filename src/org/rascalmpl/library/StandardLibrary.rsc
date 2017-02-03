@doc{
  This module exists to document the modules needed to bootstrap the current compiled implementation. 
  The modules imported by this module will end up in the pre-compiled standard library of Rascal, while
  other modules in the current library folder will not. This is a temporary solution until the whole library
  is split into reusable packaged components.
}
@contributor{Jurgen Vinju - Jurgen.Vinju@cwi.nl}
module StandardLibrary

import Prelude;
import util::Benchmark;
import util::FileSystem;
import util::Highlight;
import util::IDEServices;
import util::Math;
import util::Maybe;
import util::Monitor;
import util::Reflective;
import util::REPL;
import util::SemVer;
import util::ShellExec;
import util::SystemAPI;
import util::UUID;
import util::Webserver;