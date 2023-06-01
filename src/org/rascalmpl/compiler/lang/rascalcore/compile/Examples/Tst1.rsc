module lang::rascalcore::compile::Examples::Tst1
import IO;
import lang::rascalcore::check::Checker;
import util::Benchmark;

void main() {
    start_time = cpuTime();  
    input_module = "lang::rascalcore::compile::Examples::Tst0";
    CheckerResult result =  rascalTModelForNames([input_module], getRascalCorePathConfig(), 
                                                 rascalTypePalConfig(
                                                    classicReifier=true
                                                    //logSolverSteps=true,
                                                    //logSolverIterations=true,
                                                    //logAttempts=true
                                                 ));
    iprintln(result.tmodels[input_module], lineLimit=10000);
    iprintln(result.tmodels[input_module].messages);
    println("Total time for checker: <(cpuTime() - start_time)/1000000> ms");
}
/*

 rascalTModelForNames(["lang::rascalcore::compile::Examples::Tst0"], getRascalCorePathConfig(), 
                                                     rascalTypePalConfig(
                                                        classicReifier=true,
                                                        logSolverSteps=true,
                                                        logSolverIterations=true,
                                                        logAttempts=true
                                                     ));
*/