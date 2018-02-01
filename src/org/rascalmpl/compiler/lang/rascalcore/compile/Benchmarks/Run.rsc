module lang::rascalcore::compile::Benchmarks::Run

/*
 * A simple micro-benchmarking framework that compares the execution time
 * of interpreted versus compiled Rascal programs:
 * - import this module in a RascalShell
 * - Type main() at the command line.
 * - Go and drink 99 bottles of beer :-(
 */
 

import IO;
import DateTime;
import Relation;
import Map;
import List;
import Set;
import String;
import ValueIO;
import util::Benchmark;
import util::Math;
import util::Reflective;
import analysis::statistics::Descriptive;
import lang::rascalcore::compile::Compile;
import lang::rascalcore::compile::Execute;

import lang::rascalcore::compile::Benchmarks::BasType;
import lang::rascalcore::compile::Benchmarks::BBottles;
import lang::rascalcore::compile::Benchmarks::BCompareFor;
import lang::rascalcore::compile::Benchmarks::BCompareIf;
import lang::rascalcore::compile::Benchmarks::BCompareComprehension;
import lang::rascalcore::compile::Benchmarks::BEmpty;
import lang::rascalcore::compile::Benchmarks::BExceptions;
import lang::rascalcore::compile::Benchmarks::BExceptionsFinally;
import lang::rascalcore::compile::Benchmarks::BFac;
import lang::rascalcore::compile::Benchmarks::BFib;
import lang::rascalcore::compile::Benchmarks::BFor;
import lang::rascalcore::compile::Benchmarks::BForCond;
import lang::rascalcore::compile::Benchmarks::BListMatch1;
import lang::rascalcore::compile::Benchmarks::BListMatch2;
import lang::rascalcore::compile::Benchmarks::BListMatch3;
import lang::rascalcore::compile::Benchmarks::BOr;
import lang::rascalcore::compile::Benchmarks::BMarriage;
import lang::rascalcore::compile::Benchmarks::BPatternMatchASTs;
import lang::rascalcore::compile::Benchmarks::BReverse1;
import lang::rascalcore::compile::Benchmarks::BRSFCalls;
import lang::rascalcore::compile::Benchmarks::BSet1;
import lang::rascalcore::compile::Benchmarks::BSetMatch1;
import lang::rascalcore::compile::Benchmarks::BSetMatch2;
import lang::rascalcore::compile::Benchmarks::BSetMatch3;
import lang::rascalcore::compile::Benchmarks::BSetMatch4;
import lang::rascalcore::compile::Benchmarks::BSendMoreMoney;
import lang::rascalcore::compile::Benchmarks::BSendMoreMoneyNotTyped;
import lang::rascalcore::compile::Benchmarks::BTemplate;
import lang::rascalcore::compile::Benchmarks::BWhile;
import lang::rascalcore::compile::Benchmarks::BVisit1;
import lang::rascalcore::compile::Benchmarks::BVisit2;
import lang::rascalcore::compile::Benchmarks::BVisit3;
import lang::rascalcore::compile::Benchmarks::BVisit4;

import lang::rascalcore::compile::Benchmarks::BVisit6a;
import lang::rascalcore::compile::Benchmarks::BVisit6b;
import lang::rascalcore::compile::Benchmarks::BVisit6c;
import lang::rascalcore::compile::Benchmarks::BVisit6d;
import lang::rascalcore::compile::Benchmarks::BVisit6e;
import lang::rascalcore::compile::Benchmarks::BVisit6f;
import lang::rascalcore::compile::Benchmarks::BVisit6g;

import lang::rascalcore::compile::Benchmarks::BSudoku;

map[str name,  value() job] jobs = (
//"BasType" :               lang::rascalcore::compile::Benchmarks::BasType::main,
"BBottles":                 lang::rascalcore::compile::Benchmarks::BBottles::main,
"BCompareFor":              lang::rascalcore::compile::Benchmarks::BCompareFor::main,
"BCompareIf":               lang::rascalcore::compile::Benchmarks::BCompareIf::main,
"BCompareComprehension":    lang::rascalcore::compile::Benchmarks::BCompareComprehension::main,
"BEmpty":                   lang::rascalcore::compile::Benchmarks::BEmpty::main,
"BExceptions":              lang::rascalcore::compile::Benchmarks::BExceptions::main,
"BExceptionsFinally":       lang::rascalcore::compile::Benchmarks::BExceptionsFinally::main,
"BFac":                     lang::rascalcore::compile::Benchmarks::BFac::main,
"BFib":                     lang::rascalcore::compile::Benchmarks::BFib::main,
"BFor":                     lang::rascalcore::compile::Benchmarks::BFor::main,
"BForCond":                 lang::rascalcore::compile::Benchmarks::BForCond::main,

"BListMatch1":              lang::rascalcore::compile::Benchmarks::BListMatch1::main,
"BListMatch2":              lang::rascalcore::compile::Benchmarks::BListMatch2::main,
"BListMatch3":              lang::rascalcore::compile::Benchmarks::BListMatch3::main,
"BOr":                      lang::rascalcore::compile::Benchmarks::BOr::main,
"BMarriage":                lang::rascalcore::compile::Benchmarks::BMarriage::main,
//"BPatternMatchASTs":      lang::rascalcore::compile::Benchmarks::BPatternMatchASTs::main,
"BReverse1":                lang::rascalcore::compile::Benchmarks::BReverse1::main,
"BRSFCalls":                lang::rascalcore::compile::Benchmarks::BRSFCalls::main,
"BSet1":                    lang::rascalcore::compile::Benchmarks::BSet1::main,
"BSetMatch1":               lang::rascalcore::compile::Benchmarks::BSetMatch1::main,
"BSetMatch2":               lang::rascalcore::compile::Benchmarks::BSetMatch2::main,
"BSetMatch3":               lang::rascalcore::compile::Benchmarks::BSetMatch3::main,
"BSetMatch4":               lang::rascalcore::compile::Benchmarks::BSetMatch4::main,
"BSendMoreMoney":           lang::rascalcore::compile::Benchmarks::BSendMoreMoney::main,
"BSendMoreMoneyNotTyped":   lang::rascalcore::compile::Benchmarks::BSendMoreMoneyNotTyped::main,
"BTemplate":                lang::rascalcore::compile::Benchmarks::BTemplate::main,
"BWhile":                   lang::rascalcore::compile::Benchmarks::BWhile::main,
"BVisit1":                  lang::rascalcore::compile::Benchmarks::BVisit1::main,
"BVisit2":                  lang::rascalcore::compile::Benchmarks::BVisit2::main,
"BVisit3":                  lang::rascalcore::compile::Benchmarks::BVisit3::main,
"BVisit4":                  lang::rascalcore::compile::Benchmarks::BVisit4::main,

"BVisit6a":                 lang::rascalcore::compile::Benchmarks::BVisit6a::main,
"BVisit6b":                 lang::rascalcore::compile::Benchmarks::BVisit6b::main,
"BVisit6c":                 lang::rascalcore::compile::Benchmarks::BVisit6c::main,
"BVisit6d":                 lang::rascalcore::compile::Benchmarks::BVisit6d::main,
"BVisit6e":                 lang::rascalcore::compile::Benchmarks::BVisit6e::main,
"BVisit6f":                 lang::rascalcore::compile::Benchmarks::BVisit6f::main,
"BVisit6g":                 lang::rascalcore::compile::Benchmarks::BVisit6g::main,

"BSudoku":                  lang::rascalcore::compile::Benchmarks::BSudoku::main
);

str base = "lang::rascalcore::compile::Benchmarks";

loc mfile = |tmp:///experiments/Compiler/Benchmarks/MeasurementsInterpreted15.value|;


map[str, list[num]] measurementsCompiled = ();      // list of timings of repeated runs per job, compiled
map[str, list[num]] measurementsInterpreted = ();   // and interpreted
map[str, list[num]] prevMeasurementsInterpreted = ();

int nsamples = 10;                                      // Number of samples per data point.

// Analysis of the data of one job
alias Analysis = tuple[str job, num speedup, num sdev, num cmean, num cdev, num imean, num idev];

// Run all benchmarks

list[Analysis] run_benchmarks(int n, list[str] jobs, bool jvm=true){
  initialize(n);
  jobs = sort(jobs);
  precompile(jobs, jvm=jvm);
  runAll(jobs, jvm=jvm);
  results = analyze_all(jobs);
  report(results);
  report_latex(results);
  measurementsInterpreted += (prevMeasurementsInterpreted - measurementsInterpreted);
  writeTextValueFile(mfile, measurementsInterpreted);
  return results;
}

void initialize(int n){
  measurementsInterpreted = ();
  measurementsCompiled = ();
  nsamples = n;
  try {
     prevMeasurementsInterpreted = readTextValueFile(#map[str, list[num]], mfile);
  } catch _: println("MeasurementsInterpreted.value not found, measurements will be repeated");
}

void precompile(list[str] jobs, bool jvm=true) {
  for(job <- jobs) {
      compileAndLink("<base>::<job>", pathConfig(), jvm=jvm);
  }
}

void runAll(list[str] jobs, bool jvm=true){
   for(int i <- index(jobs)){
       job = jobs[i];
       println("**** Run compiled: <job> (<i+1>/<size(jobs)>)");
       runCompiled(job, jvm=jvm);
   }
  
   for(int i <- index(jobs)){
       job = jobs[i];
       println("**** Run interpreted: <job> (<i+1>/<size(jobs)>)");
        if(prevMeasurementsInterpreted[job]?){
           measurementsInterpreted[job] = prevMeasurementsInterpreted[job];
        } else {
           runInterpreted(job);
       }
   }
}

void runCompiled(str job, bool jvm=true) {
  measurementsCompiled[job] =
	  for(int i <- [0 .. nsamples]){
		  t1 = cpuTime();
		  v = execute("<base>::<job>", pathConfig(), jvm=jvm);
		  t2 = cpuTime();
		  append (t2 - t1)/1000000;
	  }
}

void runInterpreted(str job) {  
  bmain = jobs[job];
  measurementsInterpreted[job] =
      for(int i <- [0 .. nsamples]){  
          t1 = cpuTime();
          bmain();
          t2 = cpuTime();
          append (t2 - t1)/1000000;
      }
}

// Remove the smalles and largest number from a list of observations

list[num] removeExtremes(list[num] results){
   results = delete(results, indexOf(results, List::min(results)));
   return delete(results, indexOf(results, List::max(results)));
}

// Analyze the timings for on benchmark job

Analysis analyze_one(str job){
  compiledExec = removeExtremes(measurementsCompiled[job]);
  cmean = mean(compiledExec);
  int cdev = toInt(standardDeviation(compiledExec));
  
  interpretedExec = removeExtremes(measurementsInterpreted[job]);
  imean = mean(interpretedExec);
  int idev = toInt(standardDeviation(interpretedExec));

  speedup = (cmean != 0) ? imean/cmean : 1000.0;
  // Standard deviation  of speedup
  sdev = 0.0;
  if(cmean != 0 && imean != 0){
     x = cdev/cmean;
     y = idev/imean;
     sdev = speedup * sqrt(x * x + y * y);
   }
  
  return <job, speedup, sdev, cmean, cdev, imean, idev>;
}

list[Analysis] analyze_all(list[str] jobs){
  return [ analyze_one(job) | job <- jobs ];
}

// Reporting:
// plain text:  report, report_one
// latex:       report_latex, report_one_latex

str align(num n) = right(toString(precision(n,5)), 6);
str align2(num n) = right(toString(precision(n,5)), 12);

void report_one(Analysis a){  
  println("<right(a.job, 25)>: speedup: <right(toString(precision(a.speedup,3)), 5)> x (+/-<precision(a.sdev,2)>); compiled: <measurementsCompiled[a.job]>; mean <align(a.cmean)> msec (+/-<a.cdev>); interpreted: <measurementsInterpreted[a.job]>; mean <align(a.imean)> msec (+/-<a.idev>)");
}

void report(list[Analysis] results){
  sep = "==========================================================";
  println("\n<sep>\nSummary of Measurements <now()>:\n");
  println("Number of samples = <nsamples>");
  for(Analysis a <- results){
     report_one(a);
  }
  println("Average speedup: <precision(mean(results.speedup), 5)>");
  println("Minimal speedup: <precision(List::min(results.speedup), 5)>");
  println("Maximal speedup: <precision(List::max(results.speedup), 5)>");
  println("<sep>");
}

void report_one_latex(Analysis a){
  println("<a.job[1..]> & <round(a.cmean, 1)> & <round(a.imean, 1)> & <round(a.speedup, 0.1)> \\\\ \\hline");
}

void report_latex(list[Analysis] results){
  println("\\begin{tabular}{| l | r | r | r |} \\hline");
  println("\\textbf{Name} & \\textbf{Compiled} & \\textbf{Interpreted} & \\textbf{Speedup} \\\\ \\hline \\hline");
   for(Analysis a <- results){
     report_one_latex(a);
  }
  println("\\textbf{Average Speedup}&   &  & \\textbf{<round(mean(results.speedup), 0.1)>} \\\\ \\hline");
  println("\\end{tabular}");
}

// Various combinations of benchmarking jobs

void main(){
  run_benchmarks(10, toList(domain(jobs)));
}

void main_visit(bool jvm=true){
    run_benchmarks(10, ["BVisit1","BVisit2","BVisit3","BVisit4","BVisit6a","BVisit6b","BVisit6c","BVisit6d","BVisit6e","BVisit6f","BVisit6g"], jvm=jvm); 
}

void main_fac(bool jvm=true){
    run_benchmarks(10, ["BFac"], jvm=jvm);   
}

void main_fib(bool jvm=true){
    run_benchmarks(10, ["BFib"], jvm=jvm);   
}

void main_marriage(bool jvm=true){
    run_benchmarks(10, ["BMarriage"], jvm=jvm);   
}

void main_sudoku(bool jvm=true){
    run_benchmarks(10, ["BSudoku"], jvm=jvm);   
}

void main_template(bool jvm=true){
    run_benchmarks(10, ["BTemplate"], jvm=jvm);   
}

void main_bottles(bool jvm=true){
    run_benchmarks(10, ["BBottles"], jvm=jvm);   
}

void main_rsf(bool jvm=true) {
    run_benchmarks(10, ["BRSFCalls"], jvm=jvm);   
}

void main_money(bool jvm=true){
    run_benchmarks(10, ["BSendMoreMoney"], jvm=jvm); 
}

void main_paper(bool jvm=true){
  main_paper1(jvm=jvm);
  main_paper2(jvm=jvm);
}

void main_paper1(bool jvm=true){
   run_benchmarks(10, ["BCompareFor","BCompareIf","BCompareComprehension","BExceptions","BEmpty",/*"BExceptionsFinally",*/"BFor","BForCond","BListMatch1","BListMatch2","BListMatch3",
                      "BOr","BReverse1","BSet1","BSetMatch1","BSetMatch2","BSetMatch3","BWhile","BVisit1","BVisit2","BVisit3"
                     ,"BVisit4","BVisit6a","BVisit6b","BVisit6c","BVisit6d","BVisit6e","BVisit6f","BVisit6g"
                ], jvm=jvm);
}

void main_paper2(bool jvm=true){
   run_benchmarks(10, ["BBottles","BFac","BFib","BMarriage",
                        //"BRSFCalls",
                        "BSendMoreMoney",
                        "BSendMoreMoneyNotTyped",
                        "BSudoku",
                        "BTemplate"
                     ], jvm=jvm);
}

void main_listmatch(bool jvm=true){
   run_benchmarks(10, ["BListMatch1", "BListMatch2", "BListMatch3"], jvm=jvm);
}

void main_setmatch(bool jvm=true){
   run_benchmarks(10, ["BSetMatch1", "BSetMatch2", "BSetMatch3"], jvm=jvm);
}

