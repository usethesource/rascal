/**
* Copyright (c) 2024-2025, NWO-I Centrum Wiskunde & Informatica (CWI)
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

// Usage: ErrorRecoveryBenchmark <base-loc> [<max-files> [<min-file-size> [<max-file-size> [<from-file>]]]]
int main(list[str] args) {
    RecoveryTestConfig config = createRecoveryTestConfig(args);
    batchRecoveryTest(config);
    return 0;
}

int rascalSmokeTest() = main(["source-loc=|std:///|", "max-amb-depth=2", "max-files=3", "max-file-size=500", "sample-window=3", "random-seed=1", "count-nodes=true"]);
int rascalStandardTest() = main(["source-loc=|std:///|", "max-files=1000", "max-file-size=5120"]);