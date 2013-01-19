module lang::rascal::tests::Nodes

import Node;
import List;
import util::Math;
import IO;

// In an ideal world, this should work, but we have to adapt ranges first ...

//public list[int] makeSlice(list[int] L, int b, int s, int e){
//  return
//    for(int i <- [b, s .. e])
//      append L[i];
//}

public test bool subscription(node N){
  R = getChildren(N);
  for(int i <- [0..arity(N)]){
      if(head(R) != N[i])
         return false;
       R = tail(R);
  }
  return true;  
}

public test bool subscriptionWrapped(node N){
  for(int i <- [0 .. arity(N)]){
      if(N[i] != N[i - arity(N)]){
      	 return false;
      }
  }
  return true;
}

public list[value] makeSlice(list[value] L, int f, int s, int e){
  res = [];
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

public test bool sliceFirst(node N) {
   L = getChildren(N);
   if(isEmpty(L)) return true;
   f = arbInt(size(L));
   e = f + arbInt(size(L) - f);
   return N[f .. e] == makeSlice(L, f, f + 1, e);
}

public test bool sliceFirst(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  return N[f..] == makeSlice(L, f, f + 1, size(L) );
}


public test bool sliceFirstSecond(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  return N[f, f + incr..] == makeSlice(L, f, f + incr, size(L));
}


public test bool sliceEnd(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return N[..e] == makeSlice(L, 0, 1, e);
}

public test bool sliceSecondEnd(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  incr = 2;
  first = incr > e ? size(L)-1 : 0;
  return N[,incr..e] == makeSlice(L, first, incr, e);
}

public tuple[int,int] arbFirstEnd(list[value] L){
  if(isEmpty(L)) throw "No beging/end indices possible";
  if(size(L) == 1) return <0,0>;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  return <f, e>;
}
public test bool sliceFirstSecondEnd(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  <f, e> = arbFirstEnd(L);
  incr = 2;
  return N[f, f + incr .. e] == makeSlice(L, f, f + incr, e);
}

public test bool sliceFirstNegative(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  f = 1;
  return N[-f..] == makeSlice(L, size(L) - f,  size(L) - f + 1, size(L));
}

public test bool sliceEndNegative(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return N[..-e] == makeSlice(L, 0, 1, e == 0 ? e : size(L) - e);
}

public test bool sliceFirstNegativeSecondNegative(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  if(f == 0)
     return N[0, -incr..] == makeSlice(L, 0, size(L) - incr, size(L));
  else
     return N[-f, -(f + incr)..] == makeSlice(L, size(L) - f, size(L) - (f + incr), -1);
}

public test bool sliceSecondNegative(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  incr = 2;
  return N[, -incr ..] == makeSlice(L, 0, size(L) - incr, size(L));
}
