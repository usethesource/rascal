module lang::sdf2::util::Importer

import lang::sdf2::util::Load;
import lang::sdf2::util::SDF2Grammar;
import lang::rascal::format::Grammar;
import lang::rascal::grammar::definition::Modules;
import util::Reflective;

@resource{sdf}
@doc{ 
  the sdf uri scheme works like this:
  sdf:///<modulename>
}
public str generate(str name, loc at, PathConfig pcfg) {
   def = loadSDF2Module(at.path, pcfg);
   gr = injectStarts(fuse(dup(sdf2grammar(at.path[1..], def))));
   return "module <name>
          '
          '<grammar2rascal(gr)>
          '
          'extend lang::sdf2::filters::PreferAvoid;
          'extend lang::sdf2::filters::IndirectPreferAvoid;
          'extend lang::sdf2::filters::Reject;
          ";  
}
