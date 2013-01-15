module lang::rascal::tests::Lists

import List;
import Boolean;
import util::Math;

// is A + B == C?
bool isConcat(list[&T] A, list[&T] B, list[&T] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     (slice(C, 0, size(A) - 1) == A && slice(C, size(A), size(C) - size(A)) == B);

public test bool concat(list[&T] A, list[&T] B) = isConcat(A, B, A + B);
public test bool concat(     &T  A, list[&T] B) = isConcat([A], B, A + B);
public test bool concat(list[&T] A,      &T  B) = isConcat(A, [B], A + B);

// is A - B == C?
bool isDiff(list[&T] A, list[&T] B, list[&T] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     all(x <- C, x in A && (x notin B || freq(x, A) > freq(x, B)));

public test bool diff(list[&T] A, list[&T] B) = isDiff(A, B, A - B);

// A == B?
bool isEqual(list[&T] A, list[&T] B) = 
     size(A) == size(B) ? (true | (it && (A[i] == B[i])) | int i <- index(A)) : false;

// Its seems that a reducer cannot  be nested in a boolean expression.
//bool isEqual(list[&T] A, list[&T] B) = 
//     size(A) == size(B) && (true | (it && (A[i] == B[i])) | int i <- index(A));

public test bool equal(list[&T] A) = A == A;
public test bool equal(list[&T] A, list[&T] B) = (A == B) ? isEqual(A,B) : !isEqual(A, B);

public test bool notEqual(list[&T] A) = !(A != A);
public test bool notEqual(list[&T] A, list[&T] B) = (A != B) ? !isEqual(A,B) : isEqual(A,B);
      
// x in L?
bool isIn(&T x, list[&T] L) = (false | it || (x == e) | e <- L);

// Is L sorted?
public bool isSorted(list[int] L) = !any(int i <- index(L), int j <- index(L), i < j && L[i] > L[j]);

// Frequency of x in L
bool freq(&T x, list[&T] L) = (0 | e == x ? it + 1 : it | e <- L);

// Merge two lists, keeping their order
public list[&T] mergeOrdered(list[&T] A, list[&T] B) {
   int i = 0;
   int j = 0;
   res = [];
   while(i < size(A) || j < size(B)){
            if(i < size(A) && arbBool()){
               res += A[i];
               i += 1;
            } else if(j < size(B)) {
              res += B[j];
              j += 1;
            };
           };
   return res;
}

// Merge two lists without keeping their order.
public list[&T] mergeUnOrdered(list[&T] A, list[&T] B) {
   res = [];
   while(!(isEmpty(A) && isEmpty(B))){
            if(arbBool() && size(A) > 0){
               <x, A> = takeOneFrom(A);
               res += x;
            } else if(size(B) > 0){
               <x, B> = takeOneFrom(B);
               res += x;
            };
           }
    return res;
}

public test bool inList(list[&T] A, list[&T] B) {
  C =  mergeUnOrdered(A, B);
  return (true | it && b in C | b <- B);
}
    
public test bool notInList(list[&T] A, &T x) =
     x in A ==> isIn(x, A) ||
     x notin A ==> !isIn(x, A);
     
public test bool intersection(list[&T] A, list[&T] B) = isEmpty(A & B) || all(x <- A & B, x in A, x in B);

public test bool lesseq(list[int] A, list[int] B)  = A <= mergeOrdered(A, B);
public test bool less(list[int] A, list[int] B) = isEmpty(B) ||  A < mergeOrdered(A, B);

public test bool greatereq(list[int] A, list[int] B)  = mergeOrdered(A, B) >= A;
public test bool greater(list[int] A, list[int] B)  = isEmpty(B) || mergeOrdered(A, B) > A;
   
public test bool splicing(list[&T] A, list[&T] B) = [*A, *B] == A + B && [A, *B] == [A] + B && [*A, B] == A + [B];

public test bool subscription(list[&T] L){
  R = L;
  for(int i <- index(L)){
      if(head(R) != L[i])
         return false;
       R = tail(R);
  }
  return true;  
}

public test bool subscriptionWrapped(list[&T] L){
  for(int i <- index(L)){
      if(L[i] != L[i - size(L)]){
      	 return false;
      }
  }
  return true;
}

public test bool sliceFirst(list[int] L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  S = L[f .. e];
  return S == makeSlice(L, f, f + 1, e);
}

public test bool sliceFirst(list[&T] L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  S = L[f..];
  return S == [L[i] | i <- [f .. size(L) ] ];
}

// In an ideal world, this should work, but we have to adapt ranges first ...

//public list[int] makeSlice(list[int] L, int b, int s, int e){
//  return
//    for(int i <- [b, s .. e])
//      append L[i];
//}

public list[int] makeSlice(list[int] L, int f, int s, int e){
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

public test bool sliceFirstSecond(list[int] L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  return L[f, f + incr..] == makeSlice(L, f, f + incr, size(L));
}


public test bool sliceEnd(list[int] L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return L[..e] == makeSlice(L, 0, 1, e);
}

public test bool sliceSecondEnd(list[int] L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  incr = 2;
  return L[,incr..e] == makeSlice(L, 0, incr, e);
}

public tuple[int,int] arbFirstEnd(list[int] L){
  if(isEmpty(L)) throw "No beging/end indices possible";
  if(size(L) == 1) return <0,0>;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  return <f, e>;
}
public test bool sliceFirstSecondEnd(list[int] L) {
  if(isEmpty(L)) return true;
  <f, e> = arbFirstEnd(L);
  incr = 2;
  return L[f, f + incr .. e] == makeSlice(L, f, f + incr, e);
}

public test bool sliceFirstNegative(list[int] L) {
  if(isEmpty(L)) return true;
  f = 1;
  return L[-f..] == makeSlice(L, size(L) - f,  size(L) - f + 1, size(L));
}

public test bool sliceEndNegative(list[int] L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return L[..-e] == makeSlice(L, 0, 1, e == 0 ? e : size(L) - e);
}

public test bool sliceFirstNegativeSecondNegative(list[int] L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  if(f == 0)
     return L[0, -incr..] == makeSlice(L, 0, size(L) - incr, size(L));
  else
     return L[-f, -(f + incr)..] == makeSlice(L, size(L) - f, size(L) - (f + incr), -1);
}

public test bool sliceSecondNegative(list[int] L) {
  if(isEmpty(L)) return true;
  incr = 2;
  S = L[, -incr ..];
  return S == makeSlice(L, 0, size(L) - incr, size(L));
}
