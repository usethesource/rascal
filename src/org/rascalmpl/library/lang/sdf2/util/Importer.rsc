module lang::sdf2::util::Importer

import lang::sdf2::util::Load;
import lang::sdf2::util::SDF2Grammar;
import lang::rascal::format::Grammar;
import lang::rascal::grammar::definition::Modules;
import util::Reflective;

@resource{
sdf
}
@synopsis{Converts an SDF2 module to a Rascal module}
@description{
The `sdf` uri scheme works like this:
`sdf:///<modulename>`

The default Rascal search path is used to resolve the 
module name to a file with the `.sdf2` extension. 

The module name is expected to be in SDF2 syntax.

If modules are "imported" by the given top module,
then these names are resolved recursively and a 
"definition" composed of all relative modules is
collected before the translation process starts.
All of the syntax rules in all of the SDF2 modules 
end up in one Rascal module.
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
