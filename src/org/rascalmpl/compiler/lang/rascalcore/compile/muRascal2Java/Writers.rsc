@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
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