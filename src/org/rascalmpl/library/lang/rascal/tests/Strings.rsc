module lang::rascal::tests::Strings

import String;
import util::Math;

public test bool sliceFirst(str L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  S = L[f .. e];
  return S == makeSlice(L, f, f + 1, e);
}

public test bool sliceFirst(str L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  S = L[f..];
  return S == makeSlice(L, f, f + 1, size(L));
}

// In an ideal world, this should work, but we have to adapt ranges first ...

//public list[int] makeSlice(list[int] L, int b, int s, int e){
//  return
//    for(int i <- [b, s .. e])
//      append L[i];
//}

public str makeSlice(str L, int f, int s, int e){
  res = "";
  int i = f;
  int delta = s - f;
  if(delta == 0 || f == e)
     return res;
  if(f <= e){
     while(i >= 0 && i < size(L) && i < e ){
        res += L[i];
        i += delta;
     }
  } else {
    while(i >= 0 && i < size(L) && i > e){
        res += L[i];
        i += delta;
     }
  }
  return res;
}

public test bool sliceFirstSecond(str L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  return L[f, f + incr..] == makeSlice(L, f, f + incr, size(L));
}


public test bool sliceEnd(str L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return L[..e] == makeSlice(L, 0, 1, e);
}

public test bool sliceSecondEnd(str L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  incr = 2;
  return L[,incr..e] == makeSlice(L, 0, incr, e);
}

public tuple[int,int] arbFirstEnd(str L){
  if(isEmpty(L)) throw "No beging/end indices possible";
  if(size(L) == 1) return <0,0>;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  return <f, e>;
}
public test bool sliceFirstSecondEnd(str L) {
  if(isEmpty(L)) return true;
  <f, e> = arbFirstEnd(L);
  incr = 2;
  return L[f, f + incr .. e] == makeSlice(L, f, f + incr, e);
}

public test bool sliceFirstNegative(str L) {
  if(isEmpty(L)) return true;
  f = 1;
  return L[-f..] == makeSlice(L, size(L) - f,  size(L) - f + 1, size(L));
}

public test bool sliceEndNegative(str L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return L[..-e] == makeSlice(L, 0, 1, e == 0 ? e : size(L) - e);
}

public test bool sliceFirstNegativeSecondNegative(str L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  if(f == 0)
     return L[0, -incr..] == makeSlice(L, 0, size(L) - incr, size(L));
  else
     return L[-f, -(f + incr)..] == makeSlice(L, size(L) - f, size(L) - (f + incr), -1);
}

public test bool sliceSecondNegative(str L) {
  if(isEmpty(L)) return true;
  incr = 2;
  S = L[, -incr ..];
  return S == makeSlice(L, 0, size(L) - incr, size(L));
}

