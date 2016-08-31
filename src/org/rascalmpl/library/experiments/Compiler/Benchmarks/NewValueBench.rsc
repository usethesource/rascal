module experiments::Compiler::Benchmarks::NewValueBench

import ValueIO;
import IO;
import ParseTree;
import util::Reflective;
import util::Benchmark;
import lang::java::m3::Core;
import lang::java::m3::AST;

void printTime(str name, int time) {
    println("<name>: <time / 1000000>ms");
}

@javaClass{org.rascalmpl.library.Prelude}
private java void writeBinaryValueFileOld(loc file, value val, bool compression = true);
@javaClass{org.rascalmpl.library.Prelude}
public java &T readBinaryValueFileOld(type[&T] result, loc file);

loc targetLoc = |test-temp:///value-io.bench|;
//loc targetLocOld = |test-temp:///value-io2.bench|;
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

void writeInterestingFile(loc target, loc rascalRoot = |home:///PhD/workspace-rascal-source/rascal/|) {
    println("Reading trees");
    trees = [
          parseNamedModuleWithSpaces("lang::rascal::syntax::Rascal")
        , parseNamedModuleWithSpaces("lang::rascal::types::CheckTypes")
    ];
    println("Constructing list");
    lists = {
          [i, i*2,i*3 | i <- [1..1000000]]
        , ["aaa<i>asf<i *3>" | i <- [1..100000]]
        , [ { i, i * 2, i *3 } | i <- [1..100000]]
    };

    println("Constructing M3");
    m3s = createM3FromDirectory(rascalRoot, errorRecovery = true, javaVersion = "1.8");
    println("Constructing ASTs");
    asts = createAstsFromDirectory(rascalRoot,  true, errorRecovery = true, javaVersion = "1.8");
    writeBinaryValueFile(target, <trees, lists, m3s, asts>);
}