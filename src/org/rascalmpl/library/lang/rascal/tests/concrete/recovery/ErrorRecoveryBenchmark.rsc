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
int rascalStandardTest() = main(["|std:///|", "max-files=1000", "max-file-size=5120"]);