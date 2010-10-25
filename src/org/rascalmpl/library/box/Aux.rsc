module box::Aux
import ParseTree;
import languages::sdf22::syntax::Modules;

public Module readFromLoc(loc asf) {
     Module a = parse(#Module, asf);
     return a;
     }
