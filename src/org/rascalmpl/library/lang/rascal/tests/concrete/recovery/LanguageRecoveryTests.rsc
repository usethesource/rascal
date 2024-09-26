module lang::rascal::tests::concrete::recovery::LanguageRecoveryTests

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

void runCTest() { testRecoveryC(); }
void runPicoTest() { testRecoveryPico(); }
void runJavaTest() { testRecoveryJava(); }
void runRascalTest() { testRecoveryRascal(); }

TestStats testRecoveryC() = testErrorRecovery(|std:///lang/c90/syntax/C.rsc|, "TranslationUnit", |std:///lang/c90/examples/hello-world.c|);
TestStats testRecoveryPico() = testErrorRecovery(|std:///lang/pico/syntax/Main.rsc|, "Program", |std:///lang/pico/examples/fac.pico|);
TestStats testRecoveryJava() = testErrorRecovery(|std:///lang/java/syntax/Java15.rsc|, "CompilationUnit", zippedFile("m3/snakes-and-ladders-project-source.zip", "src/snakes/LastSquare.java"));
TestStats testRecoveryRascal() = testErrorRecovery(|std:///lang/rascal/syntax/Rascal.rsc|, "Module", |std:///lang/rascal/vis/ImportGraph.rsc|);

void runLanguageTests() {
    runPicoTest();
    runCTest();    
    runJavaTest();
    runRascalTest();    
}