module lang::rascalcore::compile::Examples::Tst1
import IO;
import util::Reflective;
import lang::rascalcore::check::Checker;
import util::Benchmark;

void main() {
    start_time = cpuTime();  
    input_module = "lang::rascalcore::compile::Examples::Tst0";
    pcfg = getRascalCorePathConfig();
    testConfig = pathConfig(
                    bin=pcfg.bin,
                    generatedSources=|project://rascal-core/target/generated-test-sources2|,
                    resources = |project://rascal-core/target/generated-test-resources2|,
                    srcs=[ |project://rascal/src/org/rascalmpl/library|, 
                           |std:///|,  
                           |project://rascal-core/src/org/rascalmpl/core/library|,
                           |project://typepal/src|],
                    libs = [|lib:///| ]
                );
    ModuleStatus result =  rascalTModelForNames([input_module],
                                                 rascalTypePalConfig(testConfig),
                                                 getRascalCompilerConfig(),
                                                 dummy_compile1
                                                 );
    iprintln(result.tmodels[input_module], lineLimit=10000);
    iprintln(result.tmodels[input_module].messages);
    println("Total time for checker: <(cpuTime() - start_time)/1000000> ms");
}