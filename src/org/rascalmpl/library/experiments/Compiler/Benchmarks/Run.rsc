module experiments::Compiler::Benchmarks::Run

/*
 * A simple micro-benchmarking framework that compares the execution time
 * of interpreted versus compiled Rascal programs:
 * - import this module in a RascalShell
 * - Type main() at the command line.
 * - Go and drink 99 bottles of beer :-(
 */
 
import Prelude;
import util::Benchmark;
import util::Math;
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
import experiments::Compiler::Benchmarks::BMarriage;
import experiments::Compiler::Benchmarks::BPatternMatchASTs;
import experiments::Compiler::Benchmarks::BReverse1;
import experiments::Compiler::Benchmarks::BRSFCalls;
import experiments::Compiler::Benchmarks::BSet1;
import experiments::Compiler::Benchmarks::BSetMatch1;
import experiments::Compiler::Benchmarks::BSetMatch2;
import experiments::Compiler::Benchmarks::BSetMatch3;
import experiments::Compiler::Benchmarks::BSendMoreMoney;
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


loc base = |rascal:///experiments/Compiler/Benchmarks/|;

alias Measurement = tuple[str name, list[num] compiledExec, list[num] interpretedExec];

int nsamples = 1;  // Number of samples per data point.

map[str,Measurement] measurements = ();

void precompile(list[str] bms) {
  for(bm <- bms) {
      compile(base + (bm + ".rsc"), recompile=true);
  }
}

void run(str bm,  value(list[value]) bmain) {
  println("Benchmark: <bm>");
  cexec = [];
  iexec = [];
  for(int i <- [0 .. nsamples]){
	  t1 = getNanoTime();
	  v = execute(base + (bm + ".rsc"), []);
	  t3 = getNanoTime();
	  cexec += (t3 - t1)/1000000;
  }
  for(int i <- [0 .. nsamples]){  
	  t3 = getNanoTime();
	  bmain([]);
	  t4 = getNanoTime();
	  iexec += (t4 - t3)/1000000;
  }
  measurements[bm] =  m = <bm, cexec, iexec>;
  report_one(m);
}

list[num] removeExtremes(list[num] results){
   results = delete(results, indexOf(results, min(results)));
   return delete(results, indexOf(results, max(results)));
}

str align(num n) = right(toString(precision(n,5)), 6);
str align2(num n) = right(toString(precision(n,5)), 12);

void report_one(Measurement m){
  compiledExec = removeExtremes(m.compiledExec);
  cexec = mean(compiledExec);
  int var_cexec = toInt(standardDeviation(compiledExec));
  
  interpretedExec = removeExtremes(m.interpretedExec);
  iexec = mean(interpretedExec);
  int var_iexec = toInt(standardDeviation(interpretedExec));

  speedup = iexec/cexec;
  println("<right(m.name, 25)>: speedup: <right(toString(precision(speedup,3)), 5)> x; compiled: <m.compiledExec>; mean <align(cexec)> msec (+/-<var_cexec>); interpreted: <m.interpretedExec>; mean <align(iexec)> msec (+/-<var_iexec>)");
}

void report(){
  min_speedup = 100000;
  max_speedup = 0;
  tot_speedup = 0;
  tot_comp = 0;
  tot_inter = 0;
  println("\nSummary of Measurements <now()>:\n");
  println("Number of samples = <nsamples>");
  for(bm <- sort(domain(measurements))){
      m = measurements[bm];
      report_one(m);
      speedup =  mean(removeExtremes(m.interpretedExec))/mean(removeExtremes(m.compiledExec));
      tot_speedup += speedup;  
      min_speedup = min(min_speedup, speedup);
      max_speedup = max(max_speedup, speedup);
      tot_comp += sum(removeExtremes(m.compiledExec));
      tot_inter += sum(removeExtremes(m.interpretedExec));
  }
  println("Average speedup: <precision(tot_speedup/size(measurements), 5)>");
  println("Minimal speedup: <precision(min_speedup, 5)>");
  println("Maximal speedup: <precision(max_speedup, 5)>");
  println("Total time: compiled: <tot_comp>; interpreted: <tot_inter>; speedup: <precision(tot_inter/tot_comp,5)>");
}

void report_one_latex(Measurement m){
  cexec = m.compiledExec;
  iexec = m.interpretedExec;
  speedup = iexec/cexec;
  println("<m.name[1..]> & <round(cexec, 1)> & <round(iexec, 1)> & <round(speedup, 0.1)> \\\\ \\hline");
}

void report_latex(){
  min_speedup = 100000;
  max_speedup = 0;
  tot_speedup = 0;
  tot_comp = 0;
  tot_inter = 0;
 
  println("\\begin{tabular}{| l | r | r | r |} \\hline");
  println("\\textbf{Name} & \\textbf{Compiled} & \\textbf{Interpreted} & \\textbf{Speedup} \\\\ \\hline \\hline");
  
  for(bm <- sort(domain(measurements))){
      m = measurements[bm];
      report_one_latex(m);
      speedup =  m.interpretedExec/m.compiledExec;
      tot_speedup += speedup;  
      min_speedup = min(min_speedup, speedup);
      max_speedup = max(max_speedup, speedup);
      tot_comp += m.compilationTime + m.compiledExec;
      tot_inter += m.interpretedExec;
  }
  println("\\textbf{Average Speedup}&   &  & \\textbf{<round(tot_speedup/size(measurements), 0.1)>} \\\\ \\hline");
  println("\\end{tabular}");
}

void main(){
  measurements = ();
  nsamples = 1;
  //run("BasType", experiments::Compiler::Benchmarks::BasType::main);
  run("BBottles", experiments::Compiler::Benchmarks::BBottles::main);
  run("BCompareFor", experiments::Compiler::Benchmarks::BCompareFor::main);
  run("BCompareIf", experiments::Compiler::Benchmarks::BCompareIf::main);
  run("BCompareComprehension", experiments::Compiler::Benchmarks::BCompareComprehension::main);
  run("BEmpty", experiments::Compiler::Benchmarks::BEmpty::main);
  run("BExceptions", experiments::Compiler::Benchmarks::BExceptions::main);
  run("BExceptionsFinally", experiments::Compiler::Benchmarks::BExceptionsFinally::main);
  run("BFac", experiments::Compiler::Benchmarks::BFac::main);
  run("BFib", experiments::Compiler::Benchmarks::BFib::main);
  run("BFor", experiments::Compiler::Benchmarks::BFor::main);
  run("BForCond", experiments::Compiler::Benchmarks::BForCond::main);
  run("BListMatch1", experiments::Compiler::Benchmarks::BListMatch1::main);
  run("BListMatch2", experiments::Compiler::Benchmarks::BListMatch2::main);
  run("BListMatch3", experiments::Compiler::Benchmarks::BListMatch3::main);
  run("BMarriage", experiments::Compiler::Benchmarks::BMarriage::main);
  run("BPatternMatchASTs", experiments::Compiler::Benchmarks::BPatternMatchASTs::main);
  run("BReverse1", experiments::Compiler::Benchmarks::BReverse1::main);
  //run("BRSFCalls", experiments::Compiler::Benchmarks::BRSFCalls::main);
  run("BSet1", experiments::Compiler::Benchmarks::BSet1::main);
  run("BSetMatch1", experiments::Compiler::Benchmarks::BSetMatch1::main);
  run("BSetMatch2", experiments::Compiler::Benchmarks::BSetMatch2::main);
  run("BSetMatch3", experiments::Compiler::Benchmarks::BSetMatch3::main);
  run("BSendMoreMoney", experiments::Compiler::Benchmarks::BSendMoreMoney::main);
  run("BSudoku", experiments::Compiler::Benchmarks::BSudoku::main);
  run("BTemplate", experiments::Compiler::Benchmarks::BTemplate::main);
  run("BVisit1", experiments::Compiler::Benchmarks::BVisit1::main);
  run("BVisit2", experiments::Compiler::Benchmarks::BVisit2::main);
  run("BVisit3", experiments::Compiler::Benchmarks::BVisit3::main);
  run("BVisit4", experiments::Compiler::Benchmarks::BVisit4::main);
  run("BVisit6a", experiments::Compiler::Benchmarks::BVisit6a::main);
  run("BVisit6b", experiments::Compiler::Benchmarks::BVisit6b::main);
  run("BVisit6c", experiments::Compiler::Benchmarks::BVisit6c::main);
  run("BVisit6d", experiments::Compiler::Benchmarks::BVisit6d::main);
  run("BVisit6e", experiments::Compiler::Benchmarks::BVisit6e::main);
  run("BVisit6f", experiments::Compiler::Benchmarks::BVisit6f::main);
  run("BVisit6g", experiments::Compiler::Benchmarks::BVisit6g::main);
  run("BWhile", experiments::Compiler::Benchmarks::BWhile::main);
 
  
  report();
  //report_latex();
}

void precompile_paper() {
   precompile_paper1();
   precompile_paper2();
}

void main_paper(){
  main_paper1();
  main_paper2();
}

void precompile_paper1() {
  precompile(["BCompareFor","BCompareIf","BCompareComprehension","BExceptions","BEmpty","BExceptionsFinally","BFor","BForCond","BListMatch1","BListMatch2","BListMatch3",
              "BReverse1","BSet1","BSetMatch1","BSetMatch2","BSetMatch3","BWhile","BVisit1","BVisit2","BVisit3"
              /*,"BVisit4","BVisit6a","BVisit6b","BVisit6c","BVisit6d","BVisit6e","BVisit6f","BVisit6g"*/]);
}

void main_paper1(){
  measurements = ();
  nsamples = 5;
  run("BCompareFor", experiments::Compiler::Benchmarks::BCompareFor::main);
  run("BCompareIf", experiments::Compiler::Benchmarks::BCompareIf::main);
  run("BCompareComprehension", experiments::Compiler::Benchmarks::BCompareComprehension::main);
  run("BExceptions", experiments::Compiler::Benchmarks::BExceptions::main);
  run("BEmpty", experiments::Compiler::Benchmarks::BEmpty::main);
  run("BExceptionsFinally", experiments::Compiler::Benchmarks::BExceptionsFinally::main);
  run("BFor", experiments::Compiler::Benchmarks::BFor::main);
  run("BForCond", experiments::Compiler::Benchmarks::BForCond::main);
  run("BListMatch1", experiments::Compiler::Benchmarks::BListMatch1::main);
  run("BListMatch2", experiments::Compiler::Benchmarks::BListMatch2::main);
  run("BListMatch3", experiments::Compiler::Benchmarks::BListMatch3::main);
  run("BReverse1", experiments::Compiler::Benchmarks::BReverse1::main);
  run("BSet1", experiments::Compiler::Benchmarks::BSet1::main);
  run("BSetMatch1", experiments::Compiler::Benchmarks::BSetMatch1::main);
  run("BSetMatch2", experiments::Compiler::Benchmarks::BSetMatch2::main);
  run("BSetMatch3", experiments::Compiler::Benchmarks::BSetMatch3::main);
  run("BWhile", experiments::Compiler::Benchmarks::BWhile::main);
  run("BVisit1", experiments::Compiler::Benchmarks::BVisit1::main);
  run("BVisit2", experiments::Compiler::Benchmarks::BVisit2::main);
  run("BVisit3", experiments::Compiler::Benchmarks::BVisit3::main);
  //run("BVisit4", experiments::Compiler::Benchmarks::BVisit4::main);
  //run("BVisit6a", experiments::Compiler::Benchmarks::BVisit6a::main);
  //run("BVisit6b", experiments::Compiler::Benchmarks::BVisit6b::main);
  //run("BVisit6c", experiments::Compiler::Benchmarks::BVisit6c::main);
  //run("BVisit6d", experiments::Compiler::Benchmarks::BVisit6d::main);
  //run("BVisit6e", experiments::Compiler::Benchmarks::BVisit6e::main);
  //run("BVisit6f", experiments::Compiler::Benchmarks::BVisit6f::main);
  //run("BVisit6g", experiments::Compiler::Benchmarks::BVisit6g::main);
  report();
  //();
}

void precompile_paper2() {
  precompile(["BBottles","BFac","BFib","BMarriage",/*"BRSFCalls",*/"BSendMoreMoney","BSudoku","BTemplate"]);
}

void main_paper2(){
  measurements = ();
  nsamples = 5;
  run("BBottles", experiments::Compiler::Benchmarks::BBottles::main);
  run("BFac", experiments::Compiler::Benchmarks::BFac::main);
  run("BFib", experiments::Compiler::Benchmarks::BFib::main);
  run("BMarriage", experiments::Compiler::Benchmarks::BMarriage::main);
  //run("BRSFCalls", experiments::Compiler::Benchmarks::BRSFCalls::main);
  run("BSendMoreMoney", experiments::Compiler::Benchmarks::BSendMoreMoney::main);
  run("BSudoku", experiments::Compiler::Benchmarks::BSudoku::main);
  run("BTemplate", experiments::Compiler::Benchmarks::BTemplate::main);
  report();
  //report_latex();
}
