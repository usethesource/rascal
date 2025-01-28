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
import String;

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

FileStats testMemoBug() = testErrorRecovery(|std:///lang/rascal/syntax/Rascal.rsc|, "Module", |std:///lang/rascal/tests/concrete/PostParseFilter.rsc|);

void runLanguageTests() {
    testRecoveryC();
    testRecoveryDiff();
    testRecoveryDot();
    testRecoveryJava();
    testRecoveryJson();
    testRecoveryPico();
    testRecoveryRascal();
}

void runRascalBatchTest(RecoveryTestConfig config) {
    int startTime = realTime();
    
    TestStats stats = batchRecoveryTest(config);
    int duration = realTime() - startTime;
    println();
    println("================================================================");
    println("Rascal batch test done in <duration/1000> seconds, total result:");
    printStats(stats);
}

// Usage: ErrorRecoveryBenchmark <base-loc> [<max-files> [<min-file-size> [<max-file-size> [<from-file>]]]]
int main(list[str] args) {
    loc baseLoc  = readTextValueString(#loc, args[0]);
    int maxFiles = 1000;
    int maxFileSize = 1000000;
    int minFileSize = 0;
    int fromFile = 0;
    loc statFile = |tmp:///error-recovery-test.stats|; // |unknown:///| to disable stat writing
    int memoVerificationTimeout = 0;
    bool abortOnNoMemoTimeout = false;

    for (str arg <- args) {
        if (/<name:[^=]*>=<val:.*>/ := arg) {
            switch (toLowerCase(name)) {
                case "max-files": maxFiles = toInt(val);
                case "max-file-size": maxFileSize = toInt(val);
                case "min-file-size": minFileSize = toInt(val);
                case "from-file": fromFile = toInt(val);
                case "stat-file": statFile = readTextValueString(#loc, val);
                case "memo-verification-timeout": memoVerificationTimeout = toInt(val);
            }
            println("arg: <arg>");
        } else switch (toLowerCase(arg)) {
            case "abort-on-no-memo-timeout": abortOnNoMemoTimeout = true;
        }
    }

    RecoveryTestConfig config = recoveryTestConfig(
        syntaxFile=|std:///lang/rascal/syntax/Rascal.rsc|,
        topSort="Module",
        dir=baseLoc,
        ext=".rsc",
        maxFiles=maxFiles,
        minFileSize=minFileSize,
        maxFileSize=maxFileSize,
        fromFile=fromFile,
        statFile=statFile,
        memoVerificationTimeout=memoVerificationTimeout,
        abortOnNoMemoTimeout=abortOnNoMemoTimeout
    );
    runRascalBatchTest(config);

    return 0;
}

int rascalSmokeTest() = main(["|std:///|", "max-files=3", "max-file-size=500"]);