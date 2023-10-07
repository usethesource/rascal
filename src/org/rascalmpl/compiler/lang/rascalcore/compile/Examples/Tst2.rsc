//@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

import util::Reflective;
import  lang::rascalcore::compile::Compile;

value main() {
    pcfg = getRascalCorePathConfig();
    testConfig = pathConfig(
                    bin=|project://rascal-core/target|,
                    generatedSources=|project://rascal-core/target/generated-test-sources2|,
                    resources = |project://rascal-core/target/generated-test-resources2|,
                    srcs=[ |project://rascal/src/org/rascalmpl/library|, 
                           |std:///|,  
                           |project://rascal-core/src/org/rascalmpl/core/library|,
                           |project://typepal/src|],
                    libs = [|lib:///| ]
                );
    msgs = compile("lang::rascalcore::compile::Examples::Tst5", getRascalCorePathConfig(), getRascalCompilerConfig());
    return msgs;
}