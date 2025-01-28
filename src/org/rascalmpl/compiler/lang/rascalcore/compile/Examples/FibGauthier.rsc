module lang::rascalcore::compile::Examples::FibGauthier
import util::Benchmark;
import IO;

public int fibonacci (int n) {
  int p (int a, int b) {
    int q (int c, int d, int e) {
      int r (int f, int () g, int () h, int () i, int () j, int () k) {
        int s () { f = f + 1; return r (f, s, g , h, i, j); }
        return f <= (n - 2 * (n / 2) == 1 ? d : e) ? 2 * (i () + k ()) - j () - s () : 2 * (i () - k ()) + j ();
      }
      int dd () { return d; }
      int zero () { return 0; }
      return c < n ? 2 * (b - a) : q (c - 1, e, d + e) - r (0, dd, dd, dd, dd, zero) + a - b;
    }
    return q (n + 1, a, b);
  }
  return n < 2 ? n : n == 2 ? 1 : p (fibonacci (n - n / 2 - 1), fibonacci (n - n / 2));
}

void warmup(){
  for(_ <- [0..10]){
    fibonacci(3);
  }
}

int main() {
  warmup();
  return work()/1000000;
}

int work() = cpuTimeOf( (){ fibonacci(14); });