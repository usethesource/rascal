module lang::rascal::tests::concrete::recovery::ErrorRecoveryBenchmark

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

import IO;
import util::Benchmark;
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

void runRascalBatchTest(int maxFiles=1000, int maxFileSize=4000) {
    int startTime = realTime();
    TestStats stats = batchRecoveryTest(|std:///lang/rascal/syntax/Rascal.rsc|, "Module", |std:///|, ".rsc", maxFiles, maxFileSize);
    int duration = realTime() - startTime;
    println();
    println("========================im========================================");
    println("Rascal batch test done in <duration/1000> seconds, total result:");
    printStats(stats);
}int main(list[str] args) {
    int maxFiles = 1000;
    int maxFileSize = 4000;
    if (size(args) == 2) {
        maxFiles = toInt(args[0]);
        maxFileSize = toInt(args[1]);
    } else if (size(args) != 0) {
        println("Usage: ErrorRecoveryBenchmark <max-files> <max-file-size>");
    }

    runRascalBatchTest(maxFiles=maxFiles, maxFileSize=maxFileSize);
    return 0;
}