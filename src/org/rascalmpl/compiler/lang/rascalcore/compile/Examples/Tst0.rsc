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
module lang::rascalcore::compile::Examples::Tst0

import IO;
import List;

loc RASCAL =      |mvn://org.rascalmpl!rascal!0.40.17/!|;
loc RASCAL_JAR  = |jar+mvn://org.rascalmpl!rascal!0.40.17/!|; 

void main(){
    println("exists(<RASCAL>): <exists(RASCAL)>");
    println("exists(<RASCAL_JAR>): <exists(RASCAL_JAR)>");
    path = "org/rascalmpl/library/analysis/grammars/Ambiguity.rsc";
    println("exists(<RASCAL+path>): <exists(RASCAL+path)>");
    println("exists(<RASCAL_JAR+path>): <exists(RASCAL_JAR+path)>");

    println("<RASCAL>.ls: <head(RASCAL.ls, 3)>");
    println("<RASCAL_JAR>.ls: <head(RASCAL_JAR.ls, 3)>");
}