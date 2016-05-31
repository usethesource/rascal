module lang::rascal::tests::basic::Intermix

import IO;

@ignoreCompiler{compiled code has a problem with the re-assignment of the loop variable l and skips the final possible match where *pst == {}}
test bool simpler() {
   l = [1,1,1];
   while ([*pre,a,b,*pst] := l, !(a == 0 || b == 0)) {
      println(l);
      l = [*pre,a,0,b,*pst];
   }
   println(l);
   return l == [1,0,1,0,1];
}

test bool iterOnly() = [ a,0 | [*pre,a,b,*pst] := [1,2,3]] == [1,0,2,0];