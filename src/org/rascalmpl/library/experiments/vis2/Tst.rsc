module experiments::vis2::Tst

import Prelude;
import util::Math;

alias dummy = str;

data D(dummy aap="") = a(dummy z, int sX=5)|q(str y, int sY = 7)|r(value \value);

public void main() {
   D d = q("aap");
   switch (d) {
      case q(str v): println(d.sY);
      }
   println(d.sY);
   // for (int i<-[0..10]) {
   //   D d = arbReal()<.5?q(3):r("OK");
   //   if (str v :=d.\value) println(v); else println("Also OK");
   //   }
   }