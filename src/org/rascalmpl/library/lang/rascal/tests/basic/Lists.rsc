module lang::rascal::tests::basic::Lists

import List;
import ListRelation;
import Set;
import Boolean;
import util::Math;
import Type;
import String;

// is A + B == C?
bool isConcat(list[&T] A, list[&T] B, list[&T] C) =
     isEmpty(A) ? C == B
                : (isEmpty(B) ? C == A
                              : (slice(C, 0, size(A)) == A && slice(C, size(A), size(C) - size(A)) == B));

test bool concat1(list[&T] A, list[&T] B) = isConcat(A, B, A + B);
test bool concat2(     &T  A, list[&T] B) = isConcat([A], B, [A] + B);
test bool concat3(list[&T] A,      &T  B) = isConcat(A, [B], A + [B]);

bool isElemProperlyRemoved(&T x, list[&T] A, list[&T] B)
    = x in A && (x notin B || freq(x, A) > freq(x, B));
    
// is A - B == C?
bool isDiff(list[&T] A, list[&T] B, list[&T] C) =
     isEmpty(A) ? isEmpty(C)
                : (isEmpty(B) ? C == A
                              : (isEmpty(C) ? (isEmpty(B) || all(x <- B, freq(x, A) <= freq(x, B)))
                                            : all(x <- C, isElemProperlyRemoved(x, A, B))));

test bool diff(list[&T] A, list[&T] B) = isDiff(A, B, A - B);

// A == B?

/*TODO: reducer inside Boolean expression does not work in interpreter*/

bool isEqual(list[&T] A, list[&T] B) { 
    if(size(A) == size(B)){
        for(int i <-[0 .. size(A)]){
            if(!eq(A[i],B[i])) 
                return false;
        }
        return true;
    }
    return false;
}    

//bool isEqual(list[&T] A, list[&T] B) = 
//     (size(A) == size(B)) ? (true | (it && (elementAt(A,i) == elementAt(B,i))) | int i <- index(A)) : false;


//bool isEqual(list[&T] A, list[&T] B) = 
//     size(A) == size(B) && (true | (it && (A[i] == B[i])) | int i <- index(A));

test bool equal1(list[&T] A) = A == A;
test bool equal2(list[&T] A, list[&T] B) = (A == B) ? isEqual(A,B) : !isEqual(A, B);

test bool notEqual1(list[&T] A) = !(A != A);
test bool notEqual2(list[&T] A, list[&T] B) = (A != B) ? !isEqual(A,B) : isEqual(A,B);
      
// x in L?
bool isIn(&T x, list[&T] L) = (false | it || eq(x,e) | e <- L);

// Frequency of x in L
int freq(&T x, list[&T] L) = (0 | eq(e, x) ? it + 1 : it | e <- L);

// Merge two lists, keeping their order
public list[&T] mergeOrdered(list[&T] A, list[&T] B) {
   int i = 0;
   int j = 0;
   list[&T] res = [];
   while(i < size(A) || j < size(B)){
            if(i < size(A) && arbBool()){
               res = res + [elementAt(A,i)];
               i += 1;
            } else if(j < size(B)) {
              res = res + [elementAt(B,j)];
              j += 1;
            };
           };
   return res;
}

// Merge two lists without keeping their order.
public list[&T] mergeUnOrdered(list[&T] A, list[&T] B) {
   list[&T] res = [];
   while(!(isEmpty(A) && isEmpty(B))){
            if(arbBool() && size(A) > 0){
               <x, A> = takeOneFrom(A);
               res = res + [x];
            } else if(size(B) > 0){
               <x, B> = takeOneFrom(B);
               res = res + [x];
            };
           }
    return res;
}

test bool inList(list[&T] A, list[&T] B) {
  C =  mergeUnOrdered(A, B);
  return (true | it && b in C | b <- B);
}
    
test bool notInList(list[&T] A, &T x) =
     x in A ==> isIn(x, A) ||
     x notin A ==> !isIn(x, A);
     
test bool intersection(list[&T] A, list[&T] B) = isEmpty(A & B) || all(x <- A & B, x in A, x in B);

test bool lesseq(list[int] A, list[int] B)  = A <= mergeOrdered(A, B);
test bool less(list[int] A, list[int] B) = isEmpty(B) ||  A < mergeOrdered(A, B);

test bool greatereq(list[int] A, list[int] B)  = mergeOrdered(A, B) >= A;
test bool greater(list[int] A, list[int] B)  = isEmpty(B) || mergeOrdered(A, B) > A;
   
test bool splicing(list[&T] A, list[&T] B) = [*A, *B] == A + B && [A, *B] == [A] + B && [*A, B] == A + [B];

test bool subscription(list[int] L){
  R = L;
  for(int i <- index(L)){
      if(head(R) != L[i])
         return false;
       R = tail(R);
  }
  return true;  
}

test bool subscriptionWrapped(list[int] L){
  for(int i <- index(L)){
      if(L[i] != L[i - size(L)]){
      	 return false;
      }
  }
  return true;
}

test bool sliceFirst(list[int] L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  S = L[f .. e];
  return S == makeSlice(L, f, f + 1, e);
}

test bool sliceFirst2(list[&T] L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  S = L[f..];
  return S == [elementAt(L,i) | i <- [f .. size(L) ] ];
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
        res = res + [elementAt(L,i)];
        i += delta;
     }
  } else {
    while(i >= 0 && i < size(L) && i > e){
        res = res + [elementAt(L,i)];
        i += delta;
     }
  }
  return res;
}

test bool sliceFirstSecond(list[int] L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  return L[f, f + incr..] == makeSlice(L, f, f + incr, size(L));
}

test bool sliceEmpty(int from, int to) = [][from % 32768 .. to % 32768] == [];

test bool sliceOverEnd() = [0][1..] == [];

test bool sliceEnd(list[int] L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return L[..e] == makeSlice(L, 0, 1, e);
}

test bool sliceSecondEnd(list[int] L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  incr = 2;
  first = incr > e ? size(L)-1 : 0;
  return L[,incr..e] == makeSlice(L, first, incr, e);
}

public tuple[int,int] arbFirstEnd(list[int] L){
  if(isEmpty(L)) throw "No beging/end indices possible";
  if(size(L) == 1) return <0,0>;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  return <f, e>;
}
test bool sliceFirstSecondEnd(list[int] L) {
  if(isEmpty(L)) return true;
  <f, e> = arbFirstEnd(L);
  incr = 2;
  return L[f, f + incr .. e] == makeSlice(L, f, f + incr, e);
}

test bool sliceFirstNegative(list[int] L) {
  if(isEmpty(L)) return true;
  f = 1;
  return L[-f..] == makeSlice(L, size(L) - f,  size(L) - f + 1, size(L));
}

test bool sliceEndNegative(list[int] L) {
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return L[..-e] == makeSlice(L, 0, 1, e == 0 ? e : size(L) - e);
}

test bool sliceFirstNegativeSecondNegative(list[int] L) {
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  if(f == 0)
     return L[0, -incr..] == makeSlice(L, 0, size(L) - incr, size(L));
  else
     return L[-f, -(f + incr)..] == makeSlice(L, size(L) - f, size(L) - (f + incr), -1);
}

test bool sliceSecondNegative(list[int] L) {
  if(isEmpty(L)) return true;
  incr = 2;
  S = L[, -incr ..];
  return S == makeSlice(L, 0, size(L) - incr, size(L));
}

test bool assignSlice1() { L = [0,1,2,3,4,5,6,7,8,9]; L[..] = [10,20]; return L == [10,20,10,20,10,20,10,20,10,20];}
test bool assignSlice2() { L = [0,1,2,3,4,5,6,7,8,9]; L[2..] = [10,20]; return   L == [0,1,10,20,10,20,10,20,10,20];}
test bool assignSlice3() { L = [0,1,2,3,4,5,6,7,8,9]; L[2..6] = [10,20]; return L == [0,1,10,20,10,20,6,7,8,9];}
test bool assignSlice4() { L = [0,1,2,3,4,5,6,7,8,9]; L[8..3] = [10,20]; return L == [0,1,2,3,10,20,10,20,10,9];}

test bool assignStep1() { L = [0,1,2,3,4,5,6,7,8,9]; L[,2..] = [10]; return L == [10,1,10,3,10,5,10,7,10,9];}
test bool assignStep2() { L = [0,1,2,3,4,5,6,7,8,9]; L[,2..] = [10,20]; return L == [10,1,20,3,10,5,20,7,10,9];}
test bool assignStep3() { L = [0,1,2,3,4,5,6,7,8,9]; L[,2..] = [10]; return L == [10,1,10,3,10,5,10,7,10,9];}
test bool assignStep4() { L = [0,1,2,3,4,5,6,7,8,9]; L[,2..] = [10,20]; return L == [10,1,20,3,10,5,20,7,10,9];}
test bool assignStep5() { L = [0,1,2,3,4,5,6,7,8,9]; L[,2..] = [10,20,30]; return L == [10,1,20,3,30,5,10,7,20,9];}
test bool assignStep6() { L = [0,1,2,3,4,5,6,7,8,9]; L[,2..] = [10,20,30,40,50,60,70]; return L == [10,1,20,3,30,5,40,7,50,9,60,70];}

test bool assignStep7() { L = [0,1,2,3,4,5,6,7,8,9]; L[2,4..] = [10]; return L == [0,1,10,3,10,5,10,7,10,9];}
test bool assignStep8() { L = [0,1,2,3,4,5,6,7,8,9]; L[2,4..6] = [10]; return L == [0,1,10,3,10,5,6,7,8,9];}

test bool assignStep9() { L = [0,1,2,3,4,5,6,7,8,9]; L[,6..1] = [10]; return L == [0,1,2,10,4,5,10,7,8,10];}
test bool assignStep10() { L = [0,1,2,3,4,5,6,7,8,9]; L[8,6..] = [10]; return L == [10,1,10,3,10,5,10,7,10,9];}
test bool assignStep11() { L = [0,1,2,3,4,5,6,7,8,9]; L[8,6..3] = [10]; return L == [0,1,2,3,10,5,10,7,10,9];}

test bool assignStep12() { L = [0,1,2,3,4,5,6,7,8,9]; L[-1,-2..] = [10,20,30,40,50]; return L == [50,40,30,20,10,50,40,30,20,10];}
test bool assignStep13() { L = [0,1,2,3,4,5,6,7,8,9]; L[-1,-3..] = [10,20,30,40,50]; return L == [0,50,2,40,4,30,6,20,8,10];}

@ignoreInterpreter{} test bool assignAdd1() { L = [0,1,2,3,4,5,6,7,8,9]; L[..] += [10]; return L == [10,11,12,13,14,15,16,17,18,19]; }
@ignoreInterpreter{} test bool assignAdd2() { L = [0,1,2,3,4,5,6,7,8,9]; L[2..] += [10]; return L == [0,1,12,13,14,15,16,17,18,19]; }
@ignoreInterpreter{} test bool assignAdd3() { L = [0,1,2,3,4,5,6,7,8,9]; L[2..6] += [10]; return L == [0,1,12,13,14,15,6,7,8,9];}
@ignoreInterpreter{} test bool assignAdd4() { L = [0,1,2,3,4,5,6,7,8,9]; L[8..3] += [10]; return L == [0,1,2,3,14,15,16,17,18,9];}

@ignoreInterpreter{} test bool assignSub1() { L = [0,1,2,3,4,5,6,7,8,9]; L[..] -= [10]; return L == [-10,1-10,2-10,3-10,4-10,5-10,6-10,7-10,8-10,9-10]; }
@ignoreInterpreter{} test bool assignSub2() { L = [0,1,2,3,4,5,6,7,8,9]; L[2..] -= [10]; return L == [0,1,2-10,3-10,4-10,5-10,6-10,7-10,8-10,9-10]; }
@ignoreInterpreter{} test bool assignSub3() { L = [0,1,2,3,4,5,6,7,8,9]; L[2..6] -= [10]; return L == [0,1,2-10,3-10,4-10,5-10,6,7,8,9];}
@ignoreInterpreter{} test bool assignSub4() { L = [0,1,2,3,4,5,6,7,8,9]; L[8..3] -= [10]; return L == [0,1,2,3,4-10,5-10,6-10,7-10,8-10,9];}

@ignoreInterpreter{} test bool assignProd1() { L = [0,1,2,3,4,5,6,7,8,9]; L[..] *= [10]; return L == [0,10,20,30,40,50,60,70,80,90]; }
@ignoreInterpreter{} test bool assignProd2() { L = [0,1,2,3,4,5,6,7,8,9]; L[2..] *= [10]; return L == [0,1,20,30,40,50,60,70,80,90]; }
@ignoreInterpreter{} test bool assignProd3() { L = [0,1,2,3,4,5,6,7,8,9]; L[2..6] *= [10]; return L == [0,1,20,30,40,50,6,7,8,9];}
@ignoreInterpreter{} test bool assignProd4() { L = [0,1,2,3,4,5,6,7,8,9]; L[8..3] *= [10]; return L == [0,1,2,3,40,50,60,70,80,9];}

// TODO: add tests for /= and &= 

@ignoreInterpreter{} test bool AssignFromEnd1(){ L = [0,1,2,3,4,5,6,7,8,9]; L[-1] = 90; return L ==  [0,1,2,3,4,5,6,7,8,90]; }
@ignoreInterpreter{} test bool AssignFromEnd2(){ L = [0,1,2,3,4,5,6,7,8,9]; L[-2] = 80; return L ==  [0,1,2,3,4,5,6,7,80,9]; }
@ignoreInterpreter{} test bool AssignFromEnd3(){ L = [0,1,2,3,4,5,6,7,8,9]; L[-10] = 10; return L == [10,1,2,3,4,5,6,7,8,9]; }

// Library functions

test bool tstDelete(list[&T] L) {
  if(size(L) > 1){
   n = arbInt(size(L));
   return delete(L, n) == L[..n] + ((n == size(L)-1) ? [] : L[n+1 ..]);
 }
 return true;
}
   
// TODO: distribution

/* domain has been removed from List
test bool tstDomain(list[&T] L) = domain(L) == toSet([0..size(L)]);
*/

test bool tstDrop(list[&T] L) {
 if(size(L) > 1){
   n = arbInt(size(L));
   return drop(n, L) == (n < size(L) ? L[n ..] : []);
 }
 return true;
}

test bool tstDup(list[&T] L) {  // L = [{{{[<-121590445r651299473>]}},{},{{[]},{}}},{}];
  seen = {};
  d = for(e <- L) { if(e notin seen){seen = seen + {e}; append e;} };
  return d == dup(d);
}

test bool tstGetOneFrom(list[&T] L) = isEmpty(L) || getOneFrom(L) in L;

test bool tstHead(list[&T] L) = isEmpty(L) || eq(head(L), elementAt(L,0));

test bool tstHeadN(list[&T] L) {
  if(size(L) > 1){
    n = arbInt(size(L));
    return eq(head(L, n),L[..n]);
  }
  return true;
}

test bool tstHeadTail(list[&T] L) = isEmpty(L) || headTail(L) == <elementAt(L,0), size(L) == 1 ? [] : L[1..]>;
   
test bool tstIndex(list[int] L) = (index(L) == [0..size(L)]);

test bool tstIndexOf(list[int] L) {
  int n = -1;
  e = isEmpty(L) ? 0 : getOneFrom(L);
  for(int i <- index(L)){
    if(eq(elementAt(L,i),e)) { n = i; break; }
  }
  return eq(indexOf(L, e), n);
}

test bool tstInsertAt(list[&T] L, &T e){
  if(isEmpty(L))
  	 return insertAt(L, 0, e) == [e];
  n = arbInt(size(L));
  return insertAt(L, n, e) == L[..n] + [e] + L[n..];
}

@ignoreCompiler{FIXME: breaks on the negative match}
test bool simplerIntercalateWithNegativeMatch() {
  str ic(str sep:!"", list[value] l) = "<for (e <- l) {><e><sep><}>"[..-size(sep)];
  
  return ic(",",[1,2,3]) == "1,2,3";
}

test bool tstIntercalate(str sep, list[value] L) {
  if (sep == "" || L == []) return true;
  return intercalate(sep, L) ==  "<L[0]><for(int i <- [1..size(L)]){><sep><L[i]><}>";
}

test bool tstIsEmpty(list[&T] L) = isEmpty(L) ? size(L) == 0 : size(L) > 0;

test bool tstLast(list[&T] L) = isEmpty(L) || eq(last(L),elementAt(L,-1));

test bool tstLastIndexOf(list[int] L) {
  int n = -1;
  e = isEmpty(L) ? 0 : getOneFrom(L);
  for(int i <- reverse(index(L))){
    if(eq(elementAt(L,i),e)){ n = i; break; }
  }
  return lastIndexOf(L, e) == n;
}

test bool tstMapper(list[int] L) {
  int incr(int x) { return x + 1; };
  return mapper(L, incr) == [x + 1 | x <- L];
}

test bool tstMax(list[int] L) = isEmpty(L) || all(x <- L, max(L) >= x);

test bool tstMerge(list[int] L, list[int] R) = isSorted(merge(sort(L), sort(R)));

test bool tstMin(list[int] L) = isEmpty(L) || all(x <- L, min(L) <= x);

test bool tstMix(list[&T] L, list[&U] R) {
  if(isEmpty(L))
     return mix(L, R) == R;
  if(isEmpty(R))
     return mix(L, R) == L;
  n = min(size(L), size(R));
  res = [elementAt(L,i), elementAt(R,i) | i <- [0..n]] + 
        (size(L) > n ? L[n..] : []) +
        (size(R) > n ? R[n..] : []);
  return mix(L,R) == res;
}

public int factorial(int n) = (n <= 0) ? 1 : n * factorial(n -1);

test bool tstPermutations(list[int] L) =
  (size(L) == size({*L}) && size(L) < 8) ==>
    (size(permutations(L)) <= factorial(size(L)) &&
   all(P <- permutations(L), size(P) == size(L), isEmpty(L - P), isEmpty(P - L)));
  
test bool tstPop(list[value] L) = isEmpty(L) || pop(L) == <elementAt(L,0), size(L) == 1 ? [] : L[1..]>;

test bool tstPrefix(list[value] L) = prefix(L) == (isEmpty(L) ? [] : L[..-1]);

test bool tstPush(value elem, list[value] L) = push(elem, L) == [elem, *L];

test bool tstReverse(list[&T] L) = reverse(reverse(L)) == L;

test bool tstSize(list[&T] L) = size(L) == (0 | it + 1 | _ <- L);

// TODO: slice

test bool tstSort(list[int] L) = isSorted(sort(L));

test bool tstSplit(list[&T] L) {
  <L1, L2> = split(L);
  return L1 + L2 == L;
}

test bool tstSum(list[int] L) = isEmpty(L) || sum(L) == (0 | it + x | x <- L);

test bool tstTail(list[&T] L) = isEmpty(L) || (tail(L) == (size(L) == 1 ? [] : L[1..]));

test bool tstTailN(list[&T] L){
 if(isEmpty(L))
    return true;
  n = arbInt(size(L));
  return tail(L, n) == (n > 0 ? L[-n..] : []);
}

test bool tstTake(list[&T] L){
if(size(L) > 1){
   n = arbInt(size(L));
   return take(n, L) == L[..n];
 }
 return true;
}

test bool tstTakeOneFrom(list[int] L){
 if(size(L) > 1){
  <elem, R> = takeOneFrom(L);
   return elem in L && (size(R) == size(L) - 1) && (toSet(L) == toSet(R) + elem);
 }
 return true;
}

bool isEven(int a) = a mod 2 == 0;

list[int] takeEven(list[int] L) = (isEmpty(L) || !isEven(elementAt(L,0))) ? [] 
                                                                : elementAt(L,0) + takeEven(size(L) == 1 ? [] : L[1..]);

test bool tstTakeWhile(list[int] L){
  return takeWhile(L, isEven) == takeEven(L);
}

test bool tstToMapUnique(list[tuple[&A, &B]] L) =
  (size(L<0>) == size(toSet(domain(L)))) ==> (toMapUnique(L) == toMapUnique(toSet(L)));

test bool tstTop(list[&T] L) = isEmpty(L) || eq(top(L),elementAt(L,0));

test bool tstToRel(list[&T] L) = isEmpty(L) || toRel(L) == {<elementAt(L,i), elementAt(L,i+1)> | i <- [0 .. size(L) - 1]};

test bool tstToSet(list[&T] L) = toSet(L) == {x | x <- L};

@ignore{not all values can be read-back after writing to string}
test bool tstToString(list[value] L) = (readTextValueString(#list[value], toString(L)) == L);


test bool tstUnzip2(list[tuple[&A, &B]] L) = unzip2(L) == <[a | <a,_> <- L], [b | <_,b> <- L]>;

test bool tstUnzip3(list[tuple[&A, &B, &C]] L) = 
     isEmpty(L) || unzip3(L) == <[a | <a,_,_> <- L], [b | <_,b,_> <- L], [c | <_,_,c> <- L]>;
     
test bool tstUpTill(int n) = n < 0 || n > 10000 || upTill(n) == [0 .. n];

test bool tstZip(list[&T] L) = zip2(L, L) == [<x, x> | x <- L];

// Tests that check the correctness of the dynamic types of lists produced by the library functions; 
// incorrect dynamic types make pattern matching fail;

test bool dtstSlice(list[&T] lst) {
	if(isEmpty(lst)) return true;
	int b = 0;
	if(size(lst) != 1) b = arbInt(size(lst) - 1);
	int len = arbInt(size(lst) - b);
	if(len == 0) return true;
	lhs = slice(lst, b, len);
	rhs = lst[b..(len + b)];
	return lhs == rhs && typeOf(lhs) == typeOf(rhs);
}

test bool dtstDelete(list[&T] lst) {
	if(isEmpty(lst)) return true;
	int index = 0;
	if(size(lst) != 1) index = arbInt(size(lst) - 1);
	lhs = delete(lst, index);
	rhs = [ lst[i] | int i <- [0..size(lst)], i != index ];
	return lhs == rhs && typeOf(lhs) == typeOf(rhs);
}

test bool dtstDrop(list[&T] lst) {
	if(isEmpty(lst)) return true;
	int n = 0;
	if(size(lst) != 1) n = arbInt(size(lst) - 1);
	lhs = drop(n, lst);
	rhs = [ lst[i] | int i <- [n..size(lst)] ];
	return lhs == rhs && typeOf(lhs) == typeOf(rhs);
}

test bool dtstHead(list[&T] lst) {
	if(isEmpty(lst)) return true;
	int n = 0;
	if(size(lst) != 1) n = arbInt(size(lst) - 1);
	if(n == 0) return true;
	lhs = head(lst, n);
	rhs1 = [ lst[i] | int i <- [0..n] ];
	rhs2 = take(n, lst);
	return lhs == rhs1 && lhs == rhs2 && typeOf(lhs) == typeOf(rhs1) && typeOf(lhs) == typeOf(rhs2);
}

test bool dtstTail(list[&T] lst) {
	if(isEmpty(lst)) return true;
	int n = 0;
	if(size(lst) != 1) n = arbInt(size(lst) - 1);
	if(n == 0) return true;
	lhs = tail(lst, n);
	rhs = [ lst[i] | int i <- [(size(lst) - n)..size(lst)] ];
	return lhs == rhs && typeOf(lhs) == typeOf(rhs);
}

test bool dtstPrefix(list[&T] lst) {
	if(isEmpty(lst) || size(lst) == 1) return true;
	lhs = prefix(lst);
	rhs = [ lst[i] | int i <- [0..(size(lst) - 1)] ];
	return lhs == rhs && typeOf(lhs) == typeOf(rhs);
}

test bool dtstDifference(list[&T] lst) {
	if(isEmpty(lst)) 
	  return true;
	  
	for(&T elem <- lst) {
		bool deleted = false;
		lhs = lst - [elem];
		// TODO: the following expression cannot be coorectly translated by the compiler
		//rhs = [ *( (eq(elem,el) && !deleted) ? { deleted = true; []; } : [ el ]) | &T el <- lst ];
		rhs = for(&T el <- lst){
		          if(eq(elem,el) && !deleted) { deleted = true; } else { append el; }
		}
		if (!eq(lhs,rhs) || typeOf(lhs) != typeOf(rhs)) {
		  throw "Removed <elem> from <lst> resulted in <lhs> instead of <rhs>";
		  
		}
	}
	return true;
}

test bool dtstIntersection(list[&T] lst) {
	if(isEmpty(lst)) return true;
	bool check = true;
	for([*l1, *l2] := lst) {
	    lhs1 = lst & l1;
	    rhs1 = [ el | &T el <- lst, el in l1 ];
	    lhs2 = lst & l2;
	    rhs2 = [ el | &T el <- lst, el in l2 ];
        check = check && lhs1 == rhs1 && typeOf(lhs1) == typeOf(rhs1) && lhs2 == rhs2 && typeOf(lhs2) == typeOf(rhs2);
    }
	return check;
}

