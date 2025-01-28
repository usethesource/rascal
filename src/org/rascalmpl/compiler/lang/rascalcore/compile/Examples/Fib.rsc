module lang::rascalcore::compile::Examples::Fib

import IO;
import util::Benchmark;

//@memo
int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

int work() { int n = 0; tm = cpuTimeOf( (){ n = fib(35); }); println("<n> (<tm/1000000> msec)"); return n; }

void warmup()
{
  for(_ <- [0..10])
  {
    fib(10);
  }
}
void main() {
    warmup();
    work();
}

test bool testFib() = fib(25) == 75025;