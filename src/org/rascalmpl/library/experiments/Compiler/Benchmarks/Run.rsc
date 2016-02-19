module experiments::Compiler::Benchmarks::Run

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
import experiments::Compiler::Compile;
import experiments::Compiler::Execute;

import experiments::Compiler::Benchmarks::BasType;
import experiments::Compiler::Benchmarks::BBottles;
import experiments::Compiler::Benchmarks::BCompareFor;
import experiments::Compiler::Benchmarks::BCompareIf;
import experiments::Compiler::Benchmarks::BCompareComprehension;
import experiments::Compiler::Benchmarks::BEmpty;
import experiments::Compiler::Benchmarks::BExceptions;
import experiments::Compiler::Benchmarks::BExceptionsFinally;
import experiments::Compiler::Benchmarks::BFac;
import experiments::Compiler::Benchmarks::BFib;
import experiments::Compiler::Benchmarks::BFor;
import experiments::Compiler::Benchmarks::BForCond;
import experiments::Compiler::Benchmarks::BListMatch1;
import experiments::Compiler::Benchmarks::BListMatch2;
import experiments::Compiler::Benchmarks::BListMatch3;
import experiments::Compiler::Benchmarks::BOr;
import experiments::Compiler::Benchmarks::BMarriage;
import experiments::Compiler::Benchmarks::BPatternMatchASTs;
import experiments::Compiler::Benchmarks::BReverse1;
import experiments::Compiler::Benchmarks::BRSFCalls;
import experiments::Compiler::Benchmarks::BSet1;
import experiments::Compiler::Benchmarks::BSetMatch1;
import experiments::Compiler::Benchmarks::BSetMatch2;
import experiments::Compiler::Benchmarks::BSetMatch3;
import experiments::Compiler::Benchmarks::BSetMatch4;
import experiments::Compiler::Benchmarks::BSendMoreMoney;
import experiments::Compiler::Benchmarks::BSendMoreMoneyNotTyped;
import experiments::Compiler::Benchmarks::BTemplate;
import experiments::Compiler::Benchmarks::BWhile;
import experiments::Compiler::Benchmarks::BVisit1;
import experiments::Compiler::Benchmarks::BVisit2;
import experiments::Compiler::Benchmarks::BVisit3;
import experiments::Compiler::Benchmarks::BVisit4;

import experiments::Compiler::Benchmarks::BVisit6a;
import experiments::Compiler::Benchmarks::BVisit6b;
import experiments::Compiler::Benchmarks::BVisit6c;
import experiments::Compiler::Benchmarks::BVisit6d;
import experiments::Compiler::Benchmarks::BVisit6e;
import experiments::Compiler::Benchmarks::BVisit6f;
import experiments::Compiler::Benchmarks::BVisit6g;

import experiments::Compiler::Benchmarks::BSudoku;

map[str name,  value() job] jobs = (
//"BasType" :               experiments::Compiler::Benchmarks::BasType::main,
"BBottles":                 experiments::Compiler::Benchmarks::BBottles::main,
"BCompareFor":              experiments::Compiler::Benchmarks::BCompareFor::main,
"BCompareIf":               experiments::Compiler::Benchmarks::BCompareIf::main,
"BCompareComprehension":    experiments::Compiler::Benchmarks::BCompareComprehension::main,
"BEmpty":                   experiments::Compiler::Benchmarks::BEmpty::main,
"BExceptions":              experiments::Compiler::Benchmarks::BExceptions::main,
"BExceptionsFinally":       experiments::Compiler::Benchmarks::BExceptionsFinally::main,
"BFac":                     experiments::Compiler::Benchmarks::BFac::main,
"BFib":                     experiments::Compiler::Benchmarks::BFib::main,
"BFor":                     experiments::Compiler::Benchmarks::BFor::main,
"BForCond":                 experiments::Compiler::Benchmarks::BForCond::main,

"BListMatch1":              experiments::Compiler::Benchmarks::BListMatch1::main,
"BListMatch2":              experiments::Compiler::Benchmarks::BListMatch2::main,
"BListMatch3":              experiments::Compiler::Benchmarks::BListMatch3::main,
"BOr":                      experiments::Compiler::Benchmarks::BOr::main,
"BMarriage":                experiments::Compiler::Benchmarks::BMarriage::main,
//"BPatternMatchASTs":      experiments::Compiler::Benchmarks::BPatternMatchASTs::main,
"BReverse1":                experiments::Compiler::Benchmarks::BReverse1::main,
"BRSFCalls":                experiments::Compiler::Benchmarks::BRSFCalls::main,
"BSet1":                    experiments::Compiler::Benchmarks::BSet1::main,
"BSetMatch1":               experiments::Compiler::Benchmarks::BSetMatch1::main,
"BSetMatch2":               experiments::Compiler::Benchmarks::BSetMatch2::main,
"BSetMatch3":               experiments::Compiler::Benchmarks::BSetMatch3::main,
"BSetMatch4":               experiments::Compiler::Benchmarks::BSetMatch4::main,
"BSendMoreMoney":           experiments::Compiler::Benchmarks::BSendMoreMoney::main,
"BSendMoreMoneyNotTyped":   experiments::Compiler::Benchmarks::BSendMoreMoneyNotTyped::main,
"BTemplate":                experiments::Compiler::Benchmarks::BTemplate::main,
"BWhile":                   experiments::Compiler::Benchmarks::BWhile::main,
"BVisit1":                  experiments::Compiler::Benchmarks::BVisit1::main,
"BVisit2":                  experiments::Compiler::Benchmarks::BVisit2::main,
"BVisit3":                  experiments::Compiler::Benchmarks::BVisit3::main,
"BVisit4":                  experiments::Compiler::Benchmarks::BVisit4::main,

"BVisit6a":                 experiments::Compiler::Benchmarks::BVisit6a::main,
"BVisit6b":                 experiments::Compiler::Benchmarks::BVisit6b::main,
"BVisit6c":                 experiments::Compiler::Benchmarks::BVisit6c::main,
"BVisit6d":                 experiments::Compiler::Benchmarks::BVisit6d::main,
"BVisit6e":                 experiments::Compiler::Benchmarks::BVisit6e::main,
"BVisit6f":                 experiments::Compiler::Benchmarks::BVisit6f::main,
"BVisit6g":                 experiments::Compiler::Benchmarks::BVisit6g::main,

"BSudoku":                  experiments::Compiler::Benchmarks::BSudoku::main
);

str base = "experiments::Compiler::Benchmarks";

loc mfile = |tmp:///experiments/Compiler/Benchmarks/MeasurementsInterpreted8.value|;


map[str, list[num]] measurementsCompiled = ();      // list of timings of repeated runs per job, compiled
map[str, list[num]] measurementsInterpreted = ();   // and interpreted
map[str, list[num]] prevMeasurementsInterpreted = ();

int nsamples = 10;                                      // Number of samples per data point.

// Analysis of the data of one job
alias Analysis = tuple[str job, num speedup, num sdev, num cmean, num cdev, num imean, num idev];

// Run all benchmarks

list[Analysis] run_benchmarks(int n, list[str] jobs, bool jvm=false){
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

void precompile(list[str] jobs, bool jvm=false) {
  for(job <- jobs) {
      compileAndLink("<base>::<job>", pathConfig(), jvm=jvm);
  }
}

void runAll(list[str] jobs, bool jvm=false){
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

void runCompiled(str job, bool jvm=false) {
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
  run_benchmarks(10, toList(domain(jobs)), jvm=jvm);
}

void main_visit(bool jvm=false){
    run_benchmarks(10, ["BVisit1","BVisit2","BVisit3","BVisit4","BVisit6a","BVisit6b","BVisit6c","BVisit6d","BVisit6e","BVisit6f","BVisit6g"], jvm=jvm); 
}

void main_fac(bool jvm=false){
    run_benchmarks(10, ["BFac"], jvm=jvm);   
}

void main_fib(bool jvm=false){
    run_benchmarks(10, ["BFib"], jvm=jvm);   
}

void main_marriage(bool jvm=false){
    run_benchmarks(10, ["BMarriage"], jvm=jvm);   
}

void main_sudoku(bool jvm=false){
    run_benchmarks(10, ["BSudoku"], jvm=jvm);   
}

void main_template(bool jvm=false){
    run_benchmarks(10, ["BTemplate"], jvm=jvm);   
}

void main_bottles(bool jvm=false){
    run_benchmarks(10, ["BBottles"], jvm=jvm);   
}

void main_rsf(bool jvm=false) {
    run_benchmarks(10, ["BRSFCalls"], jvm=jvm);   
}

void main_money(bool jvm=false){
    run_benchmarks(10, ["BSendMoreMoney"], jvm=jvm); 
}

void main_paper(bool jvm=false){
  main_paper1(jvm=jvm);
  main_paper2(jvm=jvm);
}

void main_paper1(bool jvm=false){
   run_benchmarks(5, ["BCompareFor","BCompareIf","BCompareComprehension","BExceptions","BEmpty",/*"BExceptionsFinally",*/"BFor","BForCond","BListMatch1","BListMatch2","BListMatch3",
                      "BOr","BReverse1","BSet1","BSetMatch1","BSetMatch2","BSetMatch3","BWhile","BVisit1","BVisit2","BVisit3"
                     ,"BVisit4","BVisit6a","BVisit6b","BVisit6c","BVisit6d","BVisit6e","BVisit6f","BVisit6g"
                ], jvm=jvm);
}

void main_paper2(bool jvm=false){
   run_benchmarks(5, ["BBottles","BFac","BFib","BMarriage",
                        //"BRSFCalls",
                        "BSendMoreMoney",
                        "BSendMoreMoneyNotTyped",
                        "BSudoku",
                        "BTemplate"
                     ], jvm=jvm);
}

void main_setmatch(bool jvm=false){
   run_benchmarks(10, ["BListMatch1", "BListMatch2", "BListMatch3", "BSetMatch1", "BSetMatch2", "BSetMatch3"], jvm=jvm);
}

