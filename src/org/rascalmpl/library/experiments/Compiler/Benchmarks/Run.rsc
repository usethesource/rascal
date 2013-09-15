module experiments::Compiler::Benchmarks::Run

/*
 * A simple micro-benchmarking framework that compares the execution time
 * of interpreted versus compiled Rascal programs.
 */
 
import Prelude;
import util::Benchmark;
import util::Math;
import experiments::Compiler::Execute;

import experiments::Compiler::Benchmarks::BBottles;
import experiments::Compiler::Benchmarks::BFac;
import experiments::Compiler::Benchmarks::BFib;
import experiments::Compiler::Benchmarks::BFor;
import experiments::Compiler::Benchmarks::BForCond;
import experiments::Compiler::Benchmarks::BListMatch1;
import experiments::Compiler::Benchmarks::BListMatch2;
import experiments::Compiler::Benchmarks::BReverse1;
import experiments::Compiler::Benchmarks::BSet1;
import experiments::Compiler::Benchmarks::BSetMatch1;
import experiments::Compiler::Benchmarks::BSetMatch2;
import experiments::Compiler::Benchmarks::BSendMoreMoney;
import experiments::Compiler::Benchmarks::BWhile;

loc base = |std:///experiments/Compiler/Benchmarks/|;

alias Measurement = tuple[str name, num compilationTime, num compiledExec, num interpretedExec];

map[str,Measurement] measurements = ();

void run(str bm,  value(list[value]) bmain) {
  println("Benchmark: <bm>");
  t1 = getMilliTime();
  <v, t2> = execute_and_time(base + (bm + ".rsc"));
  t3 = getMilliTime();
  bmain([]);
  t4 = getMilliTime();
  measurements[bm] =  m = <bm, t3 - t1, t2, t4 - t3>;
  report_one(m);
}

str align(num n) = right(toString(precision(n,5)), 6);
str align2(num n) = right(toString(precision(n,5)), 12);

void report_one(Measurement m){
  comp  = m.compilationTime;
  cexec = m.compiledExec;
  iexec = m.interpretedExec;
  speedup = iexec/cexec;
  saved = 100.0 * (iexec - (comp + cexec)) / iexec;
  println("<right(m.name, 15)>: compiled: (compilation <align(comp)> msec, execution <align(cexec)> msec); interpreted: <align(iexec)> msec; speedup: <align(speedup)> x; saved: <align(saved)> %");
}

void report(){
  min_speedup = 100000;
  max_speedup = 0;
  tot_speedup = 0;
  tot_comp = 0;
  tot_inter = 0;
  println("\nSummary of Measurements <now()>:\n");
  for(bm <- sort(domain(measurements))){
      m = measurements[bm];
      report_one(m);
      speedup =  m.interpretedExec/m.compiledExec;
      tot_speedup += speedup;  
      min_speedup = min(min_speedup, speedup);
      max_speedup = max(max_speedup, speedup);
      tot_comp += m.compilationTime + m.compiledExec;
      tot_inter += m.interpretedExec;
  }
  println("Average speedup: <precision(tot_speedup/size(measurements), 5)>");
  println("Minimal speedup: <precision(min_speedup, 5)>");
  println("Maximal speedup: <precision(max_speedup, 5)>");
  println("Total time: compiled: <tot_comp>; interpreted: <tot_inter>; saved: <precision(100 * (tot_inter - tot_comp) / tot_inter, 5)>%");
}

void main(){
  measurements = ();
  run("BBottles", experiments::Compiler::Benchmarks::BBottles::main);
  run("BFac", experiments::Compiler::Benchmarks::BFac::main);
  run("BFib", experiments::Compiler::Benchmarks::BFib::main);
  run("BFor", experiments::Compiler::Benchmarks::BFor::main);
  run("BForCond", experiments::Compiler::Benchmarks::BForCond::main);
  run("BListMatch1", experiments::Compiler::Benchmarks::BListMatch1::main);
  run("BListMatch2", experiments::Compiler::Benchmarks::BListMatch2::main);
  run("BReverse1", experiments::Compiler::Benchmarks::BReverse1::main);
  run("BSet1", experiments::Compiler::Benchmarks::BSet1::main);
  run("BSetMatch1", experiments::Compiler::Benchmarks::BSetMatch1::main);
  run("BSetMatch2", experiments::Compiler::Benchmarks::BSetMatch2::main);
  run("BSendMoreMoney", experiments::Compiler::Benchmarks::BSendMoreMoney::main);
  //run("BTemplate", experiments::Compiler::Benchmarks::BTemplate::main);
  run("BWhile", experiments::Compiler::Benchmarks::BWhile::main);
  report();
}