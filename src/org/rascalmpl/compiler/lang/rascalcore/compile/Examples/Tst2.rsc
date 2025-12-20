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
//@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

import IO;
import util::UUID;
import util::PathConfig;
import lang::rascalcore::check::ModuleLocations;

private loc testLibraryLoc = |memory://myTestLibrary-<uuid().authority>/|;

void showName(loc m, PathConfig pcfg){
  println("<m> =\> <getRascalModuleName(m, pcfg)>");
}
void confusion() {
    listTPL = testLibraryLoc + "/rascal/$List.tpl";
    writeFile( listTPL, "$List.tpl (only file name matters, content irrelevant)");
    testListTPL = testLibraryLoc + "/rascal/lang/rascal/tests/library/$List.tpl";
    writeFile(testListTPL, "lang/rascal/tests/library/$List.tpl (content irrelevant)");
    pcfg = pathConfig(srcs = [], libs=[testLibraryLoc]);
    
    listSrc = |project://rascal/src/org/rascalmpl/library/List.rsc|;
    testListSrc = |project://rascal/src/org/rascalmpl/library/lang/rascal/tests/library/List.rsc|;
    println("\nConfusion");
    showName(listSrc, pcfg);
    showName(testListSrc, pcfg);

    //rename library => libraries
    remove(testListTPL);
    renamedTestListTPL = testLibraryLoc + "/rascal/lang/rascal/tests/libraries/$List.tpl";
    writeFile(renamedTestListTPL, "lang/rascal/tests/libraries/$List.tpl (content irrelevant)");

    println("\nNo confusion after renaming library to libraries");
    showName(listSrc, pcfg);
    showName(testListSrc, pcfg);
}