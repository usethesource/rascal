module lang::rascalcore::compile::Examples::Tst1
import IO;
import lang::rascalcore::check::Checker;
//import util::Benchmark;

void main() {
    //start_time = cpuTime();  
    //n = 100;
    //for(int i <- [0..n]){
        iprintln(
        rascalTModelForNames(["lang::rascalcore::compile::Examples::Tst0"], getRascalCorePathConfig(), rascalTypePalConfig(classicReifier=true))
        );
    //}
    //println("Per iteration: <(cpuTime() - start_time)/(n * 1000000)> ms");
}