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
module lang::rascalcore::compile::ShowTPL

import IO;
import ValueIO;
extend lang::rascalcore::check::CheckerCommon;

loc tplLoc = |file:///Users/paulklint/git/bird/bird-core/target/classes/rascal/lang/bird/$Checker.tpl|;
TModel tm = {try return readBinaryValueFile(#TModel, tplLoc); catch _: return tmodel();};

void setTPL(loc tpl){
    tplLoc = tpl;
    tm = readBinaryValueFile(#TModel, tplLoc);
}

void defines(str search = ""){
    if(search?){
        for(def <- tm.defines){
            if(def.id == search) println(def);
        }
    } else {
        iprintln(tm.defines, lineLimit=10000);
    }
}

void scopes(){
    iprintln(tm.scopes, lineLimit=10000);
}

void paths(){
    iprintln(tm.paths, lineLimit=10000);
}

void referPaths(){
    iprintln(tm.referPaths, lineLimit=10000);
}

void uses(){
    iprintln(tm.uses, lineLimit=10000);
}

void definesMap(){
    iprintln(tm.definesMap, lineLimit=10000);
}

void modelName(){
    iprintln(tm.modelName, lineLimit=10000);
}

void moduleLocs(){
    iprintln(tm.moduleLocs, lineLimit=10000);
}

void facts(){
    iprintln(tm.facts, lineLimit=10000);
}

void specializedFacts(){
    iprintln(tm.specializedFacts, lineLimit=10000);
}

void useDef(){
    iprintln(tm.useDef, lineLimit=10000);
}

void messages(){
    iprintln(tm.messages, lineLimit=10000);
}

void store(){
    iprintln(tm.store, lineLimit=10000);
}

void definintions(){
    iprintln(tm.definitions, lineLimit=10000);
}

void logical2physical(){
    iprintln(tm.logical2physical, lineLimit=10000);
}

void usesPhysicalLocs(){
    iprintln(tm.usesPhysicalLocs, lineLimit=10000);
}

void config(){
    iprintln(tm.config, lineLimit=10000);
}