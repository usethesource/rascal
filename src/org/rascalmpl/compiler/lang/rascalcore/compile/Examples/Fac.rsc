module lang::rascalcore::compile::Examples::Fac
 
import IO;
import util::Benchmark;

@doc{This is factorial
}
int   fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
 
//int main(str n = "24"){
//    return fac(toInt(n));
//} 

int work() { int n = 0; tm = cpuTimeOf( (){ n = fac(950); }); println("<n> (<tm/1000000> msec)"); return n; }


int main() = work();

test bool tfac() = fac(24) == 620448401733239439360000;