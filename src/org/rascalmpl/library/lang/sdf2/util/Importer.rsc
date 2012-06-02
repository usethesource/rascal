module lang::sdf2::util::Importer

import lang::sdf2::util::Load;
import lang::sdf2::util::SDF2Grammar;
import lang::rascal::format::Grammar;

@resource{sdf}
@doc{
  the sdf uri scheme works like this:
  sdf://<modulename>
}
public str generate(str name, loc at) {
   SDF def = loadSDF2Module(at.host, |rascal:///|);
   Grammar gr = sdf2grammar o fuse (name, def);
   return "module <name>
          '
          '<grammar2rascal(gr)>
          ";  
}

