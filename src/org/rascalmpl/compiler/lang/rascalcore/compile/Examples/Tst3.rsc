module lang::rascalcore::compile::Examples::Tst3

import IO;
import util::Reflective;
import lang::rascalcore::compile::Compile;

void main(){
    pcfg = pathConfig(   
        srcs = [
                |home:///git/typepal//src|
                ],
        bin = |project://rascal-core/target/test-classes|,
        generatedSources = |project://rascal-core/target/generated-test-sources|,
        resources = |project://rascal-core/target/generated-test-resources|,
        libs = [ |lib://rascal/| ]
    );
    msgs = compile("Boolean", rascalCompilerConfig(pcfg)[logPathConfig=true]);
    iprintln(msgs);
}