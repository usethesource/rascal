module experiments::Compiler::Examples::QL::util::GenTest

import IO;
import experiments::Compiler::Examples::QL::lang::qla::AST;
import experiments::Compiler::Examples::QL::lang::qla::Load;
import experiments::Compiler::Examples::QL::lang::qla::Parse;
import experiments::Compiler::Examples::QL::lang::qla::Resolve;
import experiments::Compiler::Examples::QL::lang::qla::Check;
import experiments::Compiler::Examples::QL::lang::qla::Compile;

import ParseTree;
import util::Benchmark;
import Set;
import List;
import String;
import ValueIO;
import util::Math;

private int MAXQ = 2001;
private int STEP = 10;

void mergeCSVs() {
  parsing = readTextValueFile(#lrel[int,num], |rascal:///experiments/Compiler/Examples/QL/output/parse.csv|);
  binding = readTextValueFile(#lrel[int,num], |rascal:///experiments/Compiler/Examples/QL/output/bind.csv|);
  checking = readTextValueFile(#lrel[int,num], |rascal:///experiments/Compiler/Examples/QL/output/typecheck.csv|);
  compiling = readTextValueFile(#lrel[int,num], |rascal:///experiments/Compiler/Examples/QL/output/compile.csv|);
  csv = [];
  
  num toS(num ns) = toReal(ns) / 1000000000.0;
  println(size(parsing));
  for (i <- [0..size(parsing)]) {
    csv += [<parsing[i][0], 
             toS(parsing[i][1]), 
             toS(binding[i][1]), 
             toS(checking[i][1]), 
             toS(compiling[i][1])>]; 
  }
  myWriteCSV(csv, |rascal:///experiments/Compiler/Examples/QL/output/benchmarks.csv|); 
}

void myWriteCSV(lrel[num, num, num, num, num] csv, loc out) {
  bool first = true;
  for (<a, b, c, d, e> <- csv) {
    line = "<a>,<b>,<c>,<d>,<e>\n";
    if (first) {
      writeFile(out, line);
      first = false;
    }
    else {
      appendToFile(out, line);
    }
  }
}


void main(list[value] args) {
  benchmarkAll();
}

void benchmarkAll() {
  // trigger pgen
  parseQL("form bla {}");


  // Parse
  benchmarkIt(|rascal:///experiments/Compiler/Examples/QL/output/parse.csv|,
    str(str src) { return src; },
    void(str src) {
      // bug in type checker? Assign does not work.
      parseQL(src);
    });

  benchmarkBind();
  // benchmarkCheck();
  // benchmarkCompile();
}

map[int,num] benchmarkCheck() =
  benchmarkIt(|rascal:///experiments/Compiler/Examples/QL/output/typecheck.csv|,
    tuple[Form, Info](str src) {
      Form f = load(src);
      rs = resolve(f);
      return <f, rs>;
    }, value(tuple[Form, Info] tup) {
        return checkForm(tup[0], tup[1]);
    });

map[int, num] benchmarkBind() = 
  benchmarkIt(|rascal:///experiments/Compiler/Examples/QL/output/bind.csv|,
    Form(str src) {
      return load(src);
    },
    tuple[Form, Info](Form f) {
      rs = resolve(f);
      return <f, rs>;
    });


map[int, num] benchmarkCompile() =
  benchmarkIt(|rascal:///experiments/Compiler/Examples/QL/output/compile.csv|, 
    Form(str src) {
      return load(src);
    }, form2js);


map[int, num] benchmarkIt(loc out, &T(str) pre, value(&T) doIt) {
  bm = ();  
  for (i <- [0,STEP..MAXQ]) {
    println("i = <i>");
    src = binForm(1, i);
    &T t = pre(src);
    bm[size(src)] = cpuTime(() { doIt(t); });
  }

  csv = [ <k, bm[k]> | k <- sort(toList(bm<0>))];
  writeTextValueFile(out, csv);
  return bm;
}



str binForm(int min, int max) {
  return "form binary {
         '  <recBin(min, max)>
         '}"; 
}

str recBin(int min, int max) {
  //println("min = <min>, max = <max>");

  if (max - min <= 1) {
    return "\"The answer is\"" + " answer_<min>_<max>: integer = (<min>)";
  }

  half = min + ((max - min) / 2);
  
  return "\"Is the number between " + "<min> and <half>" + "\"" + " x_<min>_<half>: boolean
         'if (x_<min>_<half>) { 
         '  <recBin(min, half)>
         '}
         'else { 
         '  <recBin(half, max)>
         '}";
}