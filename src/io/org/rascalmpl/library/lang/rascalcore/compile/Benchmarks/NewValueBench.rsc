module lang::rascalcore::compile::Benchmarks::NewValueBench

import ValueIO;
import IO;
import String;
import Set;
import ParseTree;
import util::Math;
import util::Reflective;
import util::Benchmark;
import util::FileSystem;
import lang::java::m3::Core;
import lang::java::m3::AST;
import analysis::statistics::Descriptive;


tuple[void(str) reporter, void() finished] progressReporter(str prefix) {
    lastSize = 0;
    println();
    return <void (str msg) {
        // clear line
        print("\r<("" | it + " " | _ <- [0..lastSize])>");
        newMsg = prefix + msg;
        lastSize = size(newMsg);
        print("\r" + newMsg);
    }, void () { println(); }>;
}
str formatTime(real time) = "<round(time / 1000000)>ms";

void printTime(str name, int time) {
    if (time > 10000000) {
        println("<name>: <time / 1000000>ms");
    }
    else {
        println("<name>: <time / 1000>us");
    }
}

@javaClass{org.rascalmpl.library.Prelude}
public java void writeBinaryValueFileOld(loc file, value val, bool compression = true);
@javaClass{org.rascalmpl.library.Prelude}
public java &T readBinaryValueFileOld(type[&T] result, loc file);

@javaClass{org.rascalmpl.library.Prelude}
public java int __getFileSize(loc file);

loc targetLoc = |test-temp:///value-io.bench|;
//loc targetLocOld = |test-temp:///value-io2.bench|;
loc targetLocOld = |compressed+test-temp:///value-io.bench.gz|;
void bench(str name, type[&T] result, value v, int warmup, int measure) {
    for (i <- [0..warmup]) {
        writeBinaryValueFile(targetLoc, v);
        writeBinaryValueFileOld(targetLoc, v, compression = false);
    }
    printTime("<name>-new-write", cpuTime(() { 
        for (i <- [0..measure]) {
            writeBinaryValueFile(targetLoc, v);
        }
    }) / measure);
    println("<name>-new-size: <__getFileSize(targetLoc)>");
    printTime("<name>-old-write", cpuTime(() { 
        for (i <- [0..measure]) {
            writeBinaryValueFileOld(targetLocOld, v, compression=false);
        }
    })/measure);
    println("<name>-old-size: <__getFileSize(targetLocOld)>");

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


/*
void benchNew(str name, type[&T] result, value v, int warmup, int measure, list[ValueIOCompression] compressions) {
    <report, done> = progressReporter("<name>-");
    for (i <- [0..warmup], c <- compressions) {
        report("warmup: <i + 1>/<warmup>");
        writeBinaryValueFile(targetLoc, v, compression = c);
        readBinaryValueFile(result, targetLoc);
    }
    report("warmup: <warmup>/<warmup>");
    lrel[ValueIOCompression comp, real write, real read, int size] meas = [];
    for (c <- compressions) {
        writes = [];
        for (i <-[0..measure]) {
            report("<c>-write: <i + 1>/<measure>");
            writes += cpuTime(() { writeBinaryValueFile(targetLoc, v, compression = c); });
        }
        reads = [];
        for (i <-[0..measure]) {
            report("<c>-read: <i + 1>/<measure>");
            reads += cpuTime(() { readBinaryValueFile(result, targetLoc); });
        }
        meas += <c, median(writes), median(reads), __getFileSize(targetLoc)>;
    }
    print("\r                                   ");
    println("\r<name>:");
    for (<c, w, r, s> <- meas) {
        println("\t<left("<c>", 10)>\t write: <formatTime(w)>\t read: <formatTime(r)>\t size: <s> bytes");
    }
    println();
}
*/


@memo
set[Declaration] getRascalASTs(loc root) 
    = createAstsFromDirectory(root,  true, errorRecovery = true, javaVersion = "1.8");
    
@memo
list[Tree] getTrees()
    = [
          parseNamedModuleWithSpaces("lang::rascal::syntax::Rascal")
        , parseNamedModuleWithSpaces("lang::rascal::types::CheckTypes")
    ];

void benchValueIO(loc rascalRoot = |home:///PhD/workspace-rascal-source/rascal/|, int warmup = 10, int measure = 10) {
    bench("parse trees", #list[Tree], getTrees(), warmup, measure);
    bench("int list", #list[int], [i, i*2,i*3 | i <- [1..1000000]], warmup, measure);
    bench("str list", #list[str], ["aaa<i>asf<i *3>" | i <- [1..100000]], warmup, measure);
    bench("m3 asts", #set[Declaration], getRascalASTs(rascalRoot), warmup, measure);
}

void benchValueIOSmall(loc rascalRoot = |home:///PhD/workspace-rascal-source/rascal/|, int warmup = 40, int measure = 60) {
    bench("small parse tree", #Tree, parseNamedModuleWithSpaces("util::Maybe"), warmup, measure);
    bench("tiny int list", #list[int], [i, i*2,i*3 | i <- [1..100]], warmup, measure);
    bench("small int list", #list[int], [i, i*2,i*3 | i <- [1..1000]], warmup, measure);
    bench("tiny str list", #list[str], ["aaa<i>asf<i *3>" | i <- [1..100]], warmup, measure);
    bench("small str list", #list[str], ["aaa<i>asf<i *3>" | i <- [1..1000]], warmup, measure);
}


/*
void benchValueIO2(loc rascalRoot = |home:///PhD/workspace-rascal-source/rascal/|, int warmup = 10, int measure = 10, list[ValueIOCompression] compr = [fast(), normal(), strong()]) {
    benchNew("parse trees", #list[Tree], getTrees(), warmup, measure, compr);
    benchNew("int list", #list[int], [i, i*2,i*3 | i <- [1..1000000]], warmup, measure, compr);
    benchNew("str list", #list[str], ["aaa<i>asf<i *3>" | i <- [1..100000]], warmup, measure, compr);
    benchNew("m3 asts", #set[Declaration], getRascalASTs(rascalRoot), warmup, measure,compr);
}
*/

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