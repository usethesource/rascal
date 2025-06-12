@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
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