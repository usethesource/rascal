module experiments::CoreRascal::RVM::Examples::Fib

import Prelude;
import util::Benchmark;

int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

void main(){
	n = 35;
	t1 = getMilliTime();
	println("fib(<n>) = <fib(n)>");
	t2 = getMilliTime();
	println("RASCAL: average elapsed time in msecs: <t2 - t1>");
}