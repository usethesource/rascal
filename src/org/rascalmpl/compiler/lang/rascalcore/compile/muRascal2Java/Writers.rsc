module lang::rascalcore::compile::muRascal2Java::Writers

import lang::rascalcore::compile::muRascal2Java::JGenie;

// ListWriter

JCode transPrim("listwriter_open", [], JGenie jg)
    = "$VF.listWriter()";

JCode transPrim("listwriter_add", [str w, str v], JGenie jg)
    = "<w>.append(<v>);\n"; 

JCode transPrim("listwriter_splice",  [str w, str v], JGenie jg)
    = "<w>.splice(<v>);\n";
      
JCode transPrim("listwriter_close", [str w], JGenie jg)
    = "<w>.done()";

// SetWriter

JCode transPrim("setwriter_open", [], JGenie jg)
    = "$VF.setWriter();\n";

JCode transPrim("setwriter_add", [str w, str v], JGenie jg)
    = "<w>.insert(<v>)"; 

JCode transPrim("setwriter_splice",  [str w, str v], JGenie jg)
    = "<w>.splice(<v>)";
      
JCode transPrim("setwriter_close", [str w], JGenie jg)
    = "<w>.done()";
    
// MapWriter

JCode transPrim("mapwriter_open", [], JGenie jg)
    = "$VF.mapWriter()";

JCode transPrim("mapwriter_add", [str w, str k, str v], JGenie jg)
    = "<w>.insert(<k>, <v>);\n"; 

JCode transPrim("mapwriter_splice",  [str w, str k, str v], JGenie jg)
    = "<w>.splice(<k>, <v>)";
      
JCode transPrim("mapwriter_close", [str w], JGenie jg)
    = "<w>.done()";
    
// StringWriter
JCode transPrim("stringwriter_open", [], JGenie jg)
    = "new StringWriter()";
    
JCode transPrim("stringwriter_add", [str w, str s], JGenie jg)
    = "<w>.append(<s>);\n";
    
JCode transPrim("stringwriter_close", [str w], JGenie jg)
    = "<w>.toString()";