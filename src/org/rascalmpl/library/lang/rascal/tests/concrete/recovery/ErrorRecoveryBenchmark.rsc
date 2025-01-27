/**
* Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
*
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
**/
module lang::rascal::tests::concrete::recovery::ErrorRecoveryBenchmark

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

import IO;
import ValueIO;
import util::Benchmark;
import util::SystemAPI;
import String;
import List;

void runTestC() { testRecoveryC(); }
void runTestDiff() { testRecoveryDiff(); }
void runTestDot() { testRecoveryDot(); }
void runTestJava() { testRecoveryJava(); }
void runTestJson() { testRecoveryJson(); }
void runTestPico() { testRecoveryPico(); }
void runTestRascal() { testRecoveryRascal(); }

FileStats testRecoveryC() = testErrorRecovery(|std:///lang/c90/syntax/C.rsc|, "TranslationUnit", |std:///lang/c90/examples/hello-world.c|);
FileStats testRecoveryDiff() = testErrorRecovery(|std:///lang/diff/unified/UnifiedDiff.rsc|, "DiffFile", |std:///lang/diff/unified/examples/example.diff|);
FileStats testRecoveryDot() = testErrorRecovery(|std:///lang/dot/syntax/Dot.rsc|, "DOT", |std:///lang/dot/examples/parser-state.dot|);
FileStats testRecoveryJava() = testErrorRecovery(|std:///lang/java/syntax/Java15.rsc|, "CompilationUnit", zippedFile("m3/snakes-and-ladders-project-source.zip", "src/snakes/LastSquare.java"));
FileStats testRecoveryJson() = testErrorRecovery(|std:///lang/json/syntax/JSON.rsc|, "JSONText", |std:///lang/json/examples/ex01.json|);
FileStats testRecoveryPico() = testErrorRecovery(|std:///lang/pico/syntax/Main.rsc|, "Program", |std:///lang/pico/examples/fac.pico|);
FileStats testRecoveryRascal() = testErrorRecovery(|std:///lang/rascal/syntax/Rascal.rsc|, "Module", |std:///lang/rascal/vis/ImportGraph.rsc|);

void runLanguageTests() {
    testRecoveryC();
    testRecoveryDiff();
    testRecoveryDot();
    testRecoveryJava();
    testRecoveryJson();
    testRecoveryPico();
    testRecoveryRascal();
}

void runRascalBatchTest(int maxFiles=1000, int minFileSize=0, int maxFileSize=4000, int fromFile=0) {
    int startTime = realTime();
    
    map[str,str] env = getSystemEnvironment();
    loc statFile = "STATFILE" in env ? readTextValueString(#loc, env["STATFILE"]) : |unknown:///|;

    println("Writing stats to <statFile>");

    TestStats stats = batchRecoveryTest(|std:///lang/rascal/syntax/Rascal.rsc|, "Module", |std:///|, ".rsc", maxFiles, minFileSize, maxFileSize, fromFile, statFile);
    int duration = realTime() - startTime;
    println();
    println("================================================================");
    println("Rascal batch test done in <duration/1000> seconds, total result:");
    printStats(stats);
}

// Usage: ErrorRecoveryBenchmark [\<max-files\> [\<min-file-size\> [\<max-file-size\> [\<from-file\>]]]]
int main(list[str] args) {
    int maxFiles = 1000;
    int maxFileSize = 1000000;
    int minFileSize = 0;
    int fromFile = 0;
    if (size(args) > 0) {
        maxFiles = toInt(args[0]);
    }
    if (size(args) > 1) {
        minFileSize = toInt(args[1]);
    }
    if (size(args) > 2) {
        maxFileSize = toInt(args[2]);
    }
    if (size(args) > 3) {
        fromFile = toInt(args[3]);
    }

    runRascalBatchTest(maxFiles=maxFiles, minFileSize=minFileSize, maxFileSize=maxFileSize, fromFile=fromFile);

    return 0;
}