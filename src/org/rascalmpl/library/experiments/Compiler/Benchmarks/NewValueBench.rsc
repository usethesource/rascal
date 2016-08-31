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

int warmup = 10;
int measure = 10;

loc targetLoc = |test-temp:///value-io.bench|;
//loc targetLocOld = |test-temp:///value-io2.bench|;
loc targetLocOld = |compressed+test-temp:///value-io.bench.gz|;
void bench(str name, type[&T] result, value v) {
    for (i <- [0..warmup]) {
        writeBinaryValueFile(targetLoc, v);
        writeBinaryValueFileOld(targetLoc, v, compression = false);
    }
    printTime("<name>-new-write", cpuTime(() { 
        for (i <- [0..measure]) {
            writeBinaryValueFile(targetLoc, v);
        }
    }) / measure);
    printTime("<name>-old-write", cpuTime(() { 
        for (i <- [0..measure]) {
            writeBinaryValueFileOld(targetLocOld, v, compression=false);
        }
    })/measure);

    v = "";
    for (i <- [0..warmup]) {
        readBinaryValueFile(result, targetLoc);
        readBinaryValueFileOld(result, targetLocOld);
    }
    printTime("<name>-new-read ", cpuTime(() { 
        for (i <- [0..measure]) {
            readBinaryValueFile(result, targetLoc);
        }
    })/ measure);
    printTime("<name>-old-read ", cpuTime(() { 
        for (i <- [0..measure]) {
            readBinaryValueFileOld(result, targetLocOld);
        }
    }) / measure);
}


@memo
set[Declaration] getRascalASTs(loc root) 
    = createAstsFromDirectory(root,  true, errorRecovery = true, javaVersion = "1.8");
    
@memo
list[Tree] getTrees()
    = [
          parseNamedModuleWithSpaces("lang::rascal::syntax::Rascal")
        , parseNamedModuleWithSpaces("lang::rascal::types::CheckTypes")
    ];

void benchValueIO(loc rascalRoot = |home:///PhD/workspace-rascal-source/rascal/|) {
    bench("parse trees", #list[Tree], getTrees());
    bench("int list", #list[int], [i, i*2,i*3 | i <- [1..1000000]]);
    bench("str list", #list[str], ["aaa<i>asf<i *3>" | i <- [1..100000]]);
    bench("m3 asts", #set[Declaration], getRascalASTs(rascalRoot));
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
    asts = getRascalASTs(rascalRoot);
    writeBinaryValueFile(target, <trees, lists, m3s, asts>);
}