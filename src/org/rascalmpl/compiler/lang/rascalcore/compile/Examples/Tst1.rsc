module lang::rascalcore::compile::Examples::Tst1
import IO;
import util::Reflective;
import lang::rascalcore::check::Checker;
import util::Benchmark;

void main() {
    start_time = cpuTime();  
    input_module = "lang::rascalcore::compile::Compile";
    
    pcfg = pathConfig(
                    bin=|project://rascal-core/target/generated-test-sources2|,
                    generatedSources=|project://rascal-core/target/generated-test-sources2|,
                    resources = |project://rascal-core/target/generated-test-resources2|,
                    srcs=[ //|project://rascal/src/org/rascalmpl/library|, 
                           //|std:///|,  
                           |project://rascal-core/src/org/rascalmpl/core/library|,
                           |project://typepal/src|,
                           |project://rascal-tutor/src|],
                     libs=[|jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal/0.39.5-BOOT2/rascal-0.39.5-BOOT2.jar!|]
                );
    ModuleStatus result =  rascalTModelForNames([input_module],
                                                  rascalCompilerConfig(pcfg),
                                                  dummy_compile1
                                                 );
    iprintln(result.tmodels, lineLimit=10000);
    //iprintln(result.tmodels[input_module].messages);
    println("Total time for checker: <(cpuTime() - start_time)/1000000> ms");
}