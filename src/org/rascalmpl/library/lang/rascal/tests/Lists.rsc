module lang::rascal::tests::Lists

import List;
import Boolean;

// is A + B == C?
bool isConcat(list[&T] A, list[&T] B, list[&T] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     (slice(C, 0, size(A) - 1) == A && slice(C, size(A), size(C) - size(A)) == B);

// is A - B == C?
bool isDiff(list[&T] A, list[&T] B, list[&T] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     all(x <- C, x in A && (x notin B || freq(x, A) > freq(x, B)));

// A == B?
bool isEqual(list[&T] A, list[&T] B) = 
     size(A) == size(B) ? (true | (it && (A[i] == B[i])) | int i <- index(A)) : false;
     
// Its seems that a reducer cannot  be nested in a boolean expression.
//bool isEqual(list[&T] A, list[&T] B) = 
//     size(A) == size(B) && (true | (it && (A[i] == B[i])) | int i <- index(A));
     
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
     
public test bool concat(list[&T] A, list[&T] B) = isConcat(A + B, A, B);
public test bool concat(     &T  A, list[&T] B) = isConcat(A + B, [A], B);
public test bool concat(list[&T] A,      &T  B) = isConcat(A + B, A, [B]);

public test bool diff(list[&T] A, list[&T] B) = isDiff(A, B, A - B);

public test bool equal(list[&T] A) = A == A;
public test bool equal(list[&T] A, list[&T] B) = (A == B) ==> isEqual(A,B); 

public test bool notEqual(list[&T] A) = !(A != A);
public test bool notEqual(list[&T] A, list[&T] B) = (A != B) ==> !isEqual(A,B);
 
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