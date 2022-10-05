module lang::rascal::tests::basic::Intermix

test bool simpler() {
   l = [1,1,1];
   while ([*pre,a,b,*pst] := l, !(a == 0 || b == 0)) {
      l = [*pre,a,0,b,*pst];
   }
   return l == [1,0,1,0,1];
}

test bool iterOnly() = [ a,0 | [*_,a,_,*_] := [1,2,3]] == [1,0,2,0];
