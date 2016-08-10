module experiments::Compiler::Benchmarks::NewValueBench

import ValueIO;
import IO;
import ParseTree;
import util::Reflective;
import util::Benchmark;

void printTime(str name, int time) {
    println("<name>: <time / 1000000>ms");
}

@javaClass{org.rascalmpl.library.Prelude}
private java void writeBinaryValueFileOld(loc file, value val, bool compression = true);
@javaClass{org.rascalmpl.library.Prelude}
public java &T readBinaryValueFileOld(type[&T] result, loc file);

loc targetLoc = |test-temp:///value-io.bench|;
loc targetLocOld = |compressed+test-temp:///value-io.bench.gz|;
void bench(str name, type[&T] result, value v) {
    printTime("<name>-new-write", cpuTime(() { 
       writeBinaryValueFile(targetLoc, v);
    }));
    printTime("<name>-old-write", cpuTime(() { 
       writeBinaryValueFileOld(targetLocOld, v, compression=false);
    }));

    v = "";
    printTime("<name>-new-read ", cpuTime(() { 
       readBinaryValueFile(result, targetLoc);
    }));
    printTime("<name>-old-read ", cpuTime(() { 
       readBinaryValueFileOld(result, targetLocOld);
    }));
}


void benchValueIO() {
    bench("parse trees", #list[Tree], [
          parseNamedModuleWithSpaces("lang::rascal::syntax::Rascal")
        , parseNamedModuleWithSpaces("lang::rascal::types::CheckTypes")
        ]);

    bench("int list", #list[int], [i, i*2,i*3 | i <- [1..1000000]]);
    bench("str list", #list[str], ["aaa<i>asf<i *3>" | i <- [1..100000]]);
}