module experiments::Compiler::Benchmarks::Run

import Prelude;
import util::Benchmark;
import util::Math;
import experiments::Compiler::Compile;

import experiments::Compiler::Benchmarks::B1;
import experiments::Compiler::Benchmarks::B2;
import experiments::Compiler::Benchmarks::B3;
import experiments::Compiler::Benchmarks::B4;
import experiments::Compiler::Benchmarks::B5;
import experiments::Compiler::Benchmarks::B6;

loc base = |std:///experiments/Compiler/Benchmarks/|;

alias Measurement = tuple[str name, num compilationTime, num compiledExec, num interpretedExec];

map[str,Measurement] measurements = ();

void run(str bm,  value(list[value]) bmain) {
  t1 = getMilliTime();
  <v, t2> = execute_and_time(base + (bm + ".rsc"));
  t3 = getMilliTime();
  bmain([]);
  t4 = getMilliTime();
  measurements[bm] =  m = <bm, t3 - t1, t2, t4 - t3>;
  report_one(m);
}

str align(num n) = right(toString(n), 5);
str align2(num n) = right(toString(n), 12);

void report_one(Measurement m){
  comp  = m.compilationTime;
  cexec = m.compiledExec;
  iexec = m.interpretedExec;
  speedup = iexec/cexec;
  saved = 100.0 * (iexec - (comp + cexec)) / iexec;
  println("<m.name>: compiled: (compilation <align(comp)> msec, execution <align(cexec)> msec); interpreted: <align(iexec)> msec; speedup: <align2(speedup)> x; saved: <align2(saved)> %");
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
  println("Average speedup: <tot_speedup/size(measurements)>");
  println("Minimal speedup: <min_speedup>");
  println("Maximal speedup: <max_speedup>");
  println("Total time: compiled: <tot_comp>; interpreted: <tot_inter>; saved: <100 * (tot_inter - tot_comp) / tot_inter>%");
}

void main(){
  measurements = ();
  run("B1", experiments::Compiler::Benchmarks::B1::main);
  run("B2", experiments::Compiler::Benchmarks::B2::main);
  run("B3", experiments::Compiler::Benchmarks::B3::main);
  run("B4", experiments::Compiler::Benchmarks::B4::main);
  run("B5", experiments::Compiler::Benchmarks::B5::main);
  run("B6", experiments::Compiler::Benchmarks::B6::main);
  report();

}