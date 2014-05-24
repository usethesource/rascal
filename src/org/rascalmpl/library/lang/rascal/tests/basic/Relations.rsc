module lang::rascal::tests::basic::Relations

import Set;
import Relation;

// Operators

test bool product(set[&A]X, set[&B] Y) =
  isEmpty(X) ==> isEmpty(X * Y) ||
  isEmpty(Y) ==> isEmpty(X * Y) ||
  all(<x, y> <- X * Y, z <- range(X * Y), <x, z> in X, <z, y> in Y);
  
test bool composition(rel[&A, &B]X, rel[&B, &C] Y) =
  isEmpty(X) ==> isEmpty(X o Y) ||
  isEmpty(Y) ==> isEmpty(X o Y) ||
  all(<x, y> <- X o Y, z <- range(X o Y), <x, z> in X, <z, y> in Y);
  
test bool selection(rel[&A fa, &B fb] X) =
  X.fa == domain(X) && X.fb == range(X) && X.fa == X<0> && X.fb == X<1>;
  
test bool \join(rel[&A, &B]X, rel[&B, &C, &D] Y) =
  isEmpty(X) ==> size(X join Y) == size(Y) ||			// Note X join Y and Y cannot be compared in type system.
  isEmpty(Y) ==> size(X join Y) == size(X) ||
  (X join Y)<0, 1> == X && (X join Y)<2,3,4> == Y;  
  
test bool subscription(rel[&A, &B, &C] X) =
  isEmpty(X) ||
  all(&A a <- domain(X), any(<&B b, &C c> <- X[a], <a, b, c> in X)) &&
  all(<&A a, &B b, &C c> <- X, <b, c> in X[a]);
  
/*TODO:  
  &A => set[list[list[loc]]]
  rel[&A,&A] =>{<{[],[[]]},{[],[[]]}>,<{[],[[],[]]},{}>}
*/

test bool tclosure(rel[&A, &A] X) = 
  isEmpty(X) ||
  X <= (X+) && (X+) + (X+) o X == (X+);
  
test bool rtclosure(rel[int, int] X) =
  isEmpty(X) ||
  X <= X* && (X*) + (X*) o X == X* && all(x <- carrier(X), y <- carrier(X), <x, x> in X*, <y, y> in X*);
  
// Library functions

private set[int] sample(rel[int, int] X) {
   c = carrier(X);
   if(size(c) <= 2)
   	  return {};
   <r1, c> = takeOneFrom(c);
   <r2, c> = takeOneFrom(c);
  return {r1, r2};
}
test bool tst_carrier(rel[int, int] X) =
  isEmpty(X) ||
  all(<a, b> <- X, a in carrier(X), b in carrier(X));
  
test bool tst_carrierR(rel[int, int] X) {
   s = sample(X);
   XR = carrierR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a in s, b in s);
}

test bool tst_carrierX(rel[int, int] X) {
   s = sample(X);
   XR = carrierX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a notin s, b notin s);
}

test bool tst_complement(rel[int, int] X) = 
   isEmpty(complement(X)) || 
   complement(X) <= domain(X) * range(X) && all(<a, b> <- complement(X), <a, b> notin X);
   
test bool tst_domain(rel[int, int] X) = 
   isEmpty(X) || 
   all(<num a, num b> <- X, a in domain(X)) && all(num c <- domain(X), any(<num x, num y> <- X, x == c));
   
test bool tst_domainR(rel[int, int] X) {
   s = sample(X);
   XR = domainR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a in s);
}

test bool tst_domainX(rel[int, int] X) {
   s = sample(X);
   XR = domainX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a notin s);
}

test bool tst_ident(set[int] X) = isEmpty(X) || all(<a, b> <- ident(X), a == b, a in X);

test bool tst_invert(rel[int, int] X) = invert(invert(X)) == X;

test bool tst_range(rel[int, int] X) = 
   isEmpty(X) || 
   all(<num a, num b> <- X, b in range(X)) && all(num c <- range(X), any(<num x, num y> <- X, y == c));
   
test bool tst_rangeR(rel[int, int] X) {
   s = sample(X);
   XR = rangeR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, b in s);
}

test bool tst_rangeX(rel[int, int] X) {
   s = sample(X);
   XR = rangeX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, b notin s);
}
