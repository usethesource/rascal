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
module lang::rascalcore::check::tests::PackagerTests

import lang::rascalcore::check::Checker;
import lang::rascalcore::check::RascalConfig;
import lang::rascalcore::package::Packager;

import Location;
import Map;
import IO;
import ValueIO;
import Message;

import util::FileSystem;
import util::PathConfig;

private void runChecker(PathConfig pcfg, list[str] mnames) {
    result = checkModules(mnames, rascalCompilerConfig(pcfg));
    for (/e:error(_,_) := result) {
        println(e);
    }
}

test bool packagerRewritesTModelsCorrectly () {
    println("**** Checking List");
    tplRoot = |memory:///|;
    originalBin = tplRoot + "rascal-lib";
    rewrittenBin = tplRoot + "rascal-lib-rewritten";
    rascalLibPcfg =
        pathConfig(srcs = [|mvn://org.rascalmpl--rascal--0.41.0-RC15/|], 
                   bin  = originalBin);
    runChecker(rascalLibPcfg, ["List"]);
    assert validateTModels(rascalLibPcfg.bin) : "Tpls before packager are not correct";
    println("**** Running packager");
    lang::rascalcore::package::Packager::main(pcfg = rascalLibPcfg, 
                                              sourceLookup=rascalLibPcfg.srcs[0], 
                                              relocatedClasses=rewrittenBin);
    assert validateTModels(rewrittenBin): "Tpls after packager are not correct";
    return true;
}

bool validateTModels(loc bin){
    println("**** Validating TModels in <bin>");
    bool result = true;
    for (loc file <- find(bin, "tpl")) {
        result = result && isValidTModel(readBinaryValueFile(file));
    }
    return result;
}

// Validity check on TModels focussing on the occurrence of logical/physical locations
bool isValidTModel(TModel tm){
    bool result = true;

    void report(str msg){
        result = false;
        println(msg);
    }
    visit(tm.paths){
        case loc l: if(!isModuleId(l)) report("Paths: Expected moduleId, found <l>");
    }
    visit(domain(tm.logical2physical)){
        case loc l: if(!isRascalLogicalLoc(l)) report("logical2physical: Expected logical loc, found <l>");
    }
    visit(range(tm.logical2physical)){
        case loc l: if(isRascalLogicalLoc(l)) report("logical2physical: Expected physical loc, found <l>");
    }

    visit(range(tm.moduleLocs)){
        case loc l: if(!isRascalLogicalLoc(l)) report("moduleLocs: Expected logical loc, found <l>");
    }

    visit(tm.messages){
        case loc l: if(isRascalLogicalLoc(l)) report("messages: Expected physical loc, found <l>");
    }

    return result;
}
