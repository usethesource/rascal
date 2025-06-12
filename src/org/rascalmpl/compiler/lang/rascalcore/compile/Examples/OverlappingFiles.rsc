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
module lang::rascalcore::compile::Examples::OverlappingFiles

import IO;
import util::FileSystem;
import Relation;
import String;
import Set;

str asBaseFileName(loc l){
    path = l.path;
    n = findLast(path, "/");
    return n >= 0 ? path[n+1 ..] : path;
}

rel[str, str] getFiles(loc dir){
    dirPath = dir.path;
    ndirPath = size(dirPath);
    return { <asBaseFileName(f), f.path[ndirPath..]> | loc f <- find(dir, bool (loc l) { return !isDirectory(l); }) };
}

void main(){
    rascalDir = |file:///Users/paulklint/git/rascal/src/|;
    rascalCoreDir =  |file:///Users/paulklint/git/rascal-core/src/|;
    typepalDir =  |file:///Users/paulklint/git/typepal/src|;

    srcDir = rascalCoreDir;
    rascalFiles =  getFiles(rascalDir);
    srcFiles =  getFiles(srcDir);
   
    identical = range(rascalFiles) & range(srcFiles);
    println("<size(identical)> identical files:");
    iprintln(identical);

    // These files have the same name but cannot clash when merged
    approved = {"AST.rsc", "TestGrammars.rsc", "Characters.rsc", "CharactersTests.rsc", "Names.rsc", "Keywords.rsc",
    "Layout.rsc", "LayoutTests.rsc", "LiteralsTests.rsc", "Symbols.rsc", "Literals.rsc",
    "Attributes.rsc", "Tests.rsc", "ModuleInfo.rsc", "Names.java"};
    sameName = domain(rascalFiles) & domain(srcFiles) - approved;

    println("<size(sameName)> files with same name:");
    for(c <- sameName){
        println("<c>:<for(f <- rascalFiles[c]+srcFiles[c]){>
                '   <f><}>");

   }
}

// Recent run:
// 0 identical files:
// {}
// 11 files with same name:
// JavaCompilerException.java:
//    org/rascalmpl/core/library/lang/rascalcore/compile/runtime/utils/JavaCompilerException.java
//    org/rascalmpl/interpreter/utils/JavaCompilerException.java
// Fingerprint.java:
//    org/rascalmpl/core/library/lang/rascalcore/compile/runtime/Fingerprint.java
//    org/rascalmpl/library/lang/rascal/matching/internal/Fingerprint.java
// JavaCompiler.java:
//    org/rascalmpl/interpreter/utils/JavaCompiler.java
//    org/rascalmpl/core/library/lang/rascalcore/compile/runtime/utils/JavaCompiler.java
// A.rsc:
//    org/rascalmpl/library/lang/rascal/tests/extends3/A.rsc
//    org/rascalmpl/library/lang/rascal/tests/functionality/CommonKeywordParameterImport3/A.rsc
//    org/rascalmpl/library/lang/rascal/tests/functionality/CommonKeywordParameter4/A.rsc
//    org/rascalmpl/core/library/lang/rascalcore/compile/Examples/A.rsc
// B.rsc:
//    org/rascalmpl/library/lang/rascal/tests/extends3/B.rsc
//    org/rascalmpl/library/lang/rascal/tests/functionality/CommonKeywordParameterImport3/B.rsc
//    org/rascalmpl/library/lang/rascal/tests/functionality/CommonKeywordParameter4/B.rsc
//    org/rascalmpl/core/library/lang/rascalcore/compile/Examples/B.rsc
// Fingerprint.rsc:
//    org/rascalmpl/core/library/lang/rascalcore/check/Fingerprint.rsc
//    org/rascalmpl/library/lang/rascal/matching/Fingerprint.rsc
// ConcreteSyntax.rsc:
//    org/rascalmpl/library/lang/rascal/syntax/tests/ConcreteSyntax.rsc
//    org/rascalmpl/library/lang/rascal/grammar/ConcreteSyntax.rsc
//    org/rascalmpl/core/library/lang/rascalcore/compile/Rascal2muRascal/ConcreteSyntax.rsc
// RascalFunctionActionExecutor.java:
//    org/rascalmpl/core/library/lang/rascalcore/compile/runtime/RascalFunctionActionExecutor.java
//    org/rascalmpl/parser/uptr/action/RascalFunctionActionExecutor.java
// JavaBridge.java:
//    org/rascalmpl/core/library/lang/rascalcore/compile/runtime/utils/JavaBridge.java
//    org/rascalmpl/interpreter/utils/JavaBridge.java
// SubSetGenerator.java:
//    org/rascalmpl/interpreter/matching/SubSetGenerator.java
//    org/rascalmpl/core/library/lang/rascalcore/compile/runtime/utils/SubSetGenerator.java
// .DS_Store:
//    org/rascalmpl/.DS_Store
//    org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/.DS_Store
//    org/rascalmpl/library/.DS_Store
//    org/.DS_Store
//    org/rascalmpl/core/library/lang/rascalcore/compile/.DS_Store
//    org/rascalmpl/library/lang/rascal/grammar/tests/.DS_Store
//    .DS_Store