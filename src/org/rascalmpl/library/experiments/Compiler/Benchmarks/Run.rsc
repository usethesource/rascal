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

void report_one(Measurement m){
  comp  = m.compilationTime;
  cexec = m.compiledExec;
  iexec = m.interpretedExec;
  speedup = iexec/cexec;
  println("<m.name>: compiled: (compilation <comp> msec), execution <cexec> msec; interpreted: <iexec> msec; speedup: <speedup>");
}
void report(){
  min_speedup = 100000;
  max_speedup = 0;
  tot_speedup = 0;
  println("\nSummary of Measurements\n");
  for(bm <- sort(domain(measurements))){
      m = measurements[bm];
      report_one(m);
      speedup =  m.interpretedExec/m.compiledExec;
      tot_speedup += speedup;  
      min_speedup = min(min_speedup, speedup);
      max_speedup = max(max_speedup, speedup);
  }
  println("Average speedup: <tot_speedup/size(measurements)>");
  println("Minimal speedup: <min_speedup>");
  println("Maximal speedup: <max_speedup>");
}

void main(){
  measurements = ();
  run("B1", experiments::Compiler::Benchmarks::B1::main);
  run("B2", experiments::Compiler::Benchmarks::B2::main);
  run("B3", experiments::Compiler::Benchmarks::B3::main);
  run("B4", experiments::Compiler::Benchmarks::B4::main);
  run("B5", experiments::Compiler::Benchmarks::B5::main);
  report();

}