module lang::rascal::tests::ListRelations

import List;
import ListRelation;

// Operators

public test bool product(list[&A]X, list[&B] Y) =
  isEmpty(X) ==> isEmpty(X * Y) ||
  isEmpty(Y) ==> isEmpty(X * Y) ||
  all(<x, y> <- X * Y, z <- range(X), <a, z> in X, <z, y> in Y);
  
public test bool composition(lrel[int, str]X, lrel[str, int] Y) =
  isEmpty(X) ==> isEmpty(X o Y) ||
  isEmpty(Y) ==> isEmpty(X o Y) ||
  all(<x, y> <- X o Y, z <- range(X), <a, z> in X, <z, y> in Y);
  
public test bool selection(lrel[&A fa, &B fb] X) =
  X.fa == domain(X) && X.fb == range(X) && X.fa == X<0> && X.fb == X<1>;
  
public test bool \join(lrel[&A, &B]X, lrel[&B, &C, &D] Y) =
  isEmpty(X) ==> X join Y == Y ||
  isEmpty(Y) ==> X join Y == X ||
  (X join Y)<0, 1> == X && (X join Y)<2,3,4> == Y;  
  
public test bool subscription(lrel[&A, &B, &C] X) =
  isEmpty(X) ||
  all(&A a <- domain(X), any(<&B b, &C c> <- X[a], <a, b, c> in X)) &&
  all(<&A a, &B b, &C c> <- X, <b, c> in X[a]);
  
public test bool tclosure(lrel[int, int] X) =   // TODO: Fix test framework to handle type parameters
  isEmpty(X) ||
  X <= (X+) && (X+) + (X+) o X == (X+);
  
public test bool rtclosure(lrel[int, int] X) =
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
public test bool tst_carrier(lrel[int, int] X) =
  isEmpty(X) ||
  all(<a, b> <- X, a in carrier(X), b in carrier(X));
  
public test bool tst_carrierR(lrel[int, int] X) {
   s = sample(X);
   XR = carrierR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a in s, b in s);
}

public test bool tst_carrierX(lrel[int, int] X) {
   s = sample(X);
   XR = carrierX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a notin s, b notin s);
}

public test bool tst_complement(lrel[int, int] X) = 
   isEmpty(complement(X)) || 
   complement(X) <= domain(X) * range(X) && all(<a, b> <- complement(X), <a, b> notin X);
   
public test bool tst_domain(lrel[int, int] X) = 
   isEmpty(X) || 
   all(<a, b> <- X, a in domain(X)) && all(a <- domain(X), any(<x, y> <- X, x == a));
   
public test bool tst_domainR(lrel[int, int] X) {
   s = sample(X);
   XR = domainR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a in s);
}

public test bool tst_domainX(lrel[int, int] X) {
   s = sample(X);
   XR = domainX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, a notin s);
}

public test bool tst_ident(list[int] X) = isEmpty(X) || all(<a, b> <- ident(X), a == b, a in X);

public test bool tst_invert(lrel[int, int] X) = invert(invert(X)) == X;

public test bool tst_range(lrel[int, int] X) = 
   isEmpty(X) || 
   all(<a, b> <- X, b in range(X)) && all(a <- range(X), any(<x, y> <- X, y == a));
   
public test bool tst_rangeR(lrel[int, int] X) {
   s = sample(X);
   XR = rangeR(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, b in s);
}

public test bool tst_rangeX(lrel[int, int] X) {
   s = sample(X);
   XR = rangeX(X, s);
   return isEmpty(XR) || all(<a, b> <- XR, b notin s);
}
