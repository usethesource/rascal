module lang::rascalcore::check::Test3

//value main(){
//    [*post] := [1] 
//    && z <- post;
//}

//java tuple[&T, set[&T]] takeOneFrom(set[&T] st);
//
//public &T min(set[&T] st) {
//    <h,t> = takeOneFrom(st);
//    return (h | e < it ? e : it | e <- t);
//}

//value main(){
//       list[int] l = [];
//       
//       if([post*] := l)
//           post += 1;
//}

data CharRange = range(int begin, int end);

alias CharClass = list[CharRange];

//private CharRange range(int b, int e) {
//  //switch (r) {
//  //  case character(Char c) : return range(charToInt(c),charToInt(c));
//  //  case fromTo(Char l1, Char r1) : {
//  //    <cL,cR> = <charToInt(l1),charToInt(r1)>;
//  //    // users may flip te ranges, but after this reversed ranges will results in empty ranges
//  //    return cL <= cR ? range(cL, cR) : range(cR, cL);
//  //  } 
//  //  default: throw "range, missed a case <r>";
//  //}
//} 

public bool lessThan(CharRange r1, CharRange r2) {
  if (range(s1,e1) := r1, range(s2,e2) := r2) {
    return e1 < s2;
  }
}