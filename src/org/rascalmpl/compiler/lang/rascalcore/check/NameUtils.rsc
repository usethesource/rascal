@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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
@bootstrapParser
module lang::rascalcore::check::NameUtils

/*
    Utilities related to Name and QualifiedName
*/

import lang::rascal::\syntax::Rascal;

import List;
import String;

public str prettyPrintName(QualifiedName qn){
    //if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
        nameParts = [ prettyPrintName(n) | n <- qn.names];
        return intercalate("::", nameParts);
       //return replaceAll("<qn>", "\\", "");
    //}
    //throw "Unexpected syntax for qualified name: <qn>";
}

public str prettyPrintName(Name nm){ 
    return prettyPrintName("<nm>");
}

public str prettyPrintName(str nm){
    return replaceFirst(nm, "\\", "");
}

public str prettyPrintBaseName(QualifiedName qn){
    //if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
        nameParts = [ n | n <- qn.names ];
        return prettyPrintName(nameParts[-1]);
        //return replaceFirst("<nameParts[-1]>", "\\", "");
    //}
   // throw "Unexpected syntax for qualified name: <qn>";
}

public str prettyPrintBaseName(Name nm){ 
    return prettyPrintName(nm);
   // return replaceFirst("<nm>", "\\", "");
}

public tuple[str qualifier, str base] splitQualifiedName(QualifiedName qn){
    //if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
        //nameParts = [ replaceFirst("<n>", "\\", "") | n <- nl ];
        nameParts = [ prettyPrintName(n) | n <- qn.names ];
        return size(nameParts) > 1 ? <intercalate("::", nameParts[0 .. -1]), nameParts[-1]> : <"", nameParts[0]>;
   // }
   // throw "Unexpected syntax for qualified name: <qn>";
}

public bool isWildCard(str name) = name[0] == "_";