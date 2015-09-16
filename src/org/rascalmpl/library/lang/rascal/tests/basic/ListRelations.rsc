module lang::rascal::tests::basic::ListRelations

import List;
import ListRelation;
import IO;
import Type;

// Operators

test bool product(list[&A]X, list[&B] Y) =
  isEmpty(X) ==> isEmpty(X * Y) ||
  isEmpty(Y) ==> isEmpty(X * Y) ||
  all(<x, y> <- X * Y, z <- range(X * Y), <x, z> in X, <z, y> in Y);
  
test bool composition(lrel[int, str]X, lrel[str, int] Y) =
  isEmpty(X) ==> isEmpty(X o Y) ||
  isEmpty(Y) ==> isEmpty(X o Y) ||
  all(<x, y> <- X o Y, z <- range(X), <x, z> in X, <z, y> in Y);

test bool selection(lrel[&A fa, &B fb] X) =
  domain(X) <= X.fa && range(X) <= X.fb && X.fa == X<0> && X.fb == X<1>;
  
test bool \join(lrel[&A, &B]X, lrel[&B, &C, &D] Y) =
  isEmpty(X) ==> size(X join Y) == size(Y) ||
  isEmpty(Y) ==> size(X join Y) == size(X) ||
  (X join Y)<0, 1> == X && (X join Y)<2,3,4> == Y;  
  
// Note that all subscriptions are of the form X[{a}] to avoid that a is interpreted as an integer index.  
test bool subscription1(lrel[&A, &B, &C] X) =
  isEmpty(X) ||
  all(&A a <- domain(X), any(<&B b, &C c> <- X[{a}], <a, b, c> in X)) &&
  all(<&A a, &B b, &C c> <- X, <b, c> in X[{a}]);

// Make sure that a single integer subscript is properly interpreted as a list index
test bool subscription2(lrel[int,str] X) =
   [X[i] | int i <- [0 .. size(X)]] == X;
  
test bool tclosure(lrel[int, int] X) = 
  isEmpty(X) ||
  X <= (X+) && squeeze((X+) + (X+) o X) == squeeze(X+);
  
private list[&T] squeeze(list[&T] xs) = ( [] | (ix in it) ? it : it + [ix] | &T ix <- xs);
   
test bool rtclosure(lrel[int, int] X) =
  isEmpty(X) ||
  X <= X* && all(x <- (X*) o X, x in X*) && all(x <- carrier(X), y <- carrier(X), <x, x> in X*, <y, y> in X*);
  
// Library functions

private set[int] sample(lrel[int, int] X) {
   c = carrier(X);
   if(size(c) <= 2)
   	  return {};
   <r1, c> = takeOneFrom(c);
   <r2, c> = takeOneFrom(c);
  return {r1, r2};
}
test bool tst_carrier(lrel[int, int] X) =
  isEmpty(X) ||
  all(<a, b> <- X, a in carrier(X), b in carrier(X));
  
test bool tst_carrierR(lrel[int, int] X) {
   s = sample(X);
   XR = carrierR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a in s, b in s);
}

test bool tst_carrierX(lrel[int, int] X) {
   s = sample(X);
   XR = carrierX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a notin s, b notin s);
}

test bool tst_complement(lrel[int, int] X) = 
   isEmpty(X) ||
   isEmpty(complement(X)) || 
   complement(X) <= domain(X) * range(X) && all(<a, b> <- complement(X), <a, b> notin X);
   
test bool tst_domain(lrel[int, int] X) = 
   isEmpty(X) || 
   all(<a, b> <- X, a in domain(X)) && all(c <- domain(X), any(<int x, int y> <- X, eq(x,c)));
   
test bool tst_domainR(lrel[int, int] X) {
   s = sample(X);
   XR = domainR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a in s);
}

test bool tst_domainX(lrel[int, int] X) {
   s = sample(X);
   XR = domainX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a notin s);
}

test bool tst_ident(list[int] X) = isEmpty(X) || all(<a, b> <- ident(X), eq(a,b), a in X);

test bool tst_invert(lrel[int, int] X) = invert(invert(X)) == X;

test bool tst_range(lrel[int, int] X) = 
   isEmpty(X) || 
   all(<int a, int b> <- X, b in range(X)) && all(int c <- range(X), any(<int x, int y> <- X, eq(y,c)));
   
test bool tst_rangeR(lrel[int, int] X) {
   s = sample(X);
   XR = rangeR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, b in s);
}

test bool tst_rangeX(lrel[int, int] X) {
   s = sample(X);
   XR = rangeX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, b notin s);
}


