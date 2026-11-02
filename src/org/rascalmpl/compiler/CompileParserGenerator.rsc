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
module CompileParserGenerator

import IO;
import lang::rascalcore::compile::Compile;

@synopsis{Compile the parser generator including all needed library modules}
void main(loc REPO     = |file:///Users/paulklint/git/|, 
          loc COMPILED = REPO + "compiled-rascal"){

    pcfg = pathConfig(
        srcs                   = [ REPO + "rascal/src/org/rascalmpl/library",
                                   REPO + "rascal/test/org/rascalmpl/benchmark/",
                                   REPO + "rascal/src/org/rascalmpl/compiler",
                                   REPO + "rascal/src/org/rascalmpl/tutor"],
        bin                    = COMPILED + "/target/classes",
        generatedSources       = COMPILED + "/src/main/java",
        generatedTestSources   = COMPILED + "/src/test/java/",
        generatedResources     = COMPILED + "/src/main/java",
        generatedTestResources = COMPILED + "/src/test/java/",
        libs                   = [ ]
    ); 

    ccfg = rascalCompilerConfig(pcfg)[verbose = true][logWrittenFiles=true];
    msgs = compile(["lang::rascal::grammar::ParserGenerator"], ccfg);
    if(isEmpty(msgs)){
        println("ok");
    } else {
        iprintln(msgs);
    }
}