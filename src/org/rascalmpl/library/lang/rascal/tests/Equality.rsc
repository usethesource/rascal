module lang::rascal::tests::Equality

import util::Math;
import Set;
import Map;

// values have an equivalence relation
public test bool reflexEq(value x) = x == x;
public test bool transEq(value x, value y, value z) = (x == y && y == z) ==> (x == z);
public test bool commutativeEq(value x, value y) = (x == y) <==> (y == x);

// values are partially ordered
public test bool reflexLTE(value x) = (x <= x);
public test bool antiSymmetricLTE(value x, value y) = (x <= y && y <= x) ==> (x == y);
public test bool transLTE(value x, value y, value z) = (x <= y && y <= z) ==> x <= z;

// numbers are totally ordered
public test bool numTotalLTE(num x, num y) = x <= y || y <= x;
public test bool numAntiSymmetricLTE(num x, num y) = (x <= y && y <= x) ==> (x == y);
public test bool numTransLTE(num x, num y, num z) = (x <= y && y <= z) ==> (x <= z);

// ints are totally ordered
public test bool intTotalLTE(int x, int y) = x <= y || y <= x;
public test bool intAntiSymmetricLTE(int x, int y) = (x <= y && y <= x) ==> (x == y);
public test bool intTransLTE(int x, int y, int z) = (x <= y && y <= z) ==> (x <= z);

// reals are totally ordered
public test bool realTotalLTE(real x, real y) = x <= y || y <= x;
public test bool realAntiSymmetricLTE(real x, real y) = (x <= y && y <= x) ==> (x == y);
public test bool realTransLTE(real x, real y, real z) = (x <= y && y <= z) ==> (x <= z);

// rat are totally ordered
public test bool ratTotalLTE(rat x, rat y) = x <= y || y <= x;
public test bool ratAntiSymmetricLTE(rat x, rat y) = (x <= y && y <= x) ==> (x == y);
public test bool ratTransLTE(rat x, rat y, rat z) = (x <= y && y <= z) ==> (x <= z);

// strings are totally ordered
public test bool numTotalLTE(str x, str y) = x <= y || y <= x;
public test bool strAntiSymmetricLTE(str x, str y) = (x <= y && y <= x) ==> (x == y);
public test bool strTransLTE(str x, str y, str z) = (x <= y && y <= z) ==> x <= z;

// sets are ordered via sub-set relation
public test bool subsetOrdering1(set[value] x, set[value] y) = x <= x + y;
public test bool subsetOrdering2(set[value] x, set[value] y) = (x <= y) <==> (x == {} || all(e <- x, e in y));

// sets are partially ordered
public test bool setReflexLTE(set[value] x) = (x <= x);
public test bool setAntiSymmetricLTE(set[value] x, set[value] y) = (x <= y && y <= x) ==> (x == y);
public test bool setTransLTE(set[value] x, set[value] y, set[value] z) = (x <= y && y <= z) ==> x <= z;

// map are ordered via sub-map relation
public test bool submapOrdering1(map[value,value] x, map[value,value] y) = x <= x + y;
public test bool submapOrdering2(map[value,value]x, map[value,value] y) = (x <= y) <==> (x == () || all(e <- x, e in y));

// maps are partially ordered
public test bool setReflexLTE(map[value,value] x) = (x <= x);
public test bool setAntiSymmetricLTE(map[value,value] x, map[value,value] y) = (x <= y && y <= x) ==> (x == y);
public test bool setTransLTE(map[value,value] x, map[value,value] y, map[value,value] z) = (x <= y && y <= z) ==> x <= z;

// conversions
public test bool intToReal(int i) = i == toReal(i);
public test bool ratToReal(rat r) = r == toReal(r);
public test bool intToReal(int i) = i <= toReal(i);
public test bool ratToReal(rat r) = r <= toReal(r);
public test bool intToReal(int i) = toReal(i) >= i;
public test bool ratToReal(rat r) = toReal(r) >= r;
public test bool lessIntReal(int i) = !(i < toReal(i));
public test bool lessRatReal(int i) = !(i < toReal(i));

// set containment
public test bool differentElements(int i) = size({i, toReal(i), toRat(i,1)}) == 3; // yes, really 3.
public test bool differentElement2(int i, rat r) = i == r ==> size({i,r}) == 2; // yes, really 2.
public test bool differentElement2(int i, real r) = i == r ==> size({i,r}) == 2; // yes, really 2.

// map keys
public test bool differentKeys(int i,real r) = (i:10,r:20)[toReal(i)]?0 == 0;
public test bool differentKeys2(int i,rat r) = (i:10,r:20)[toRat(i,1)]?0 == 0;
public test bool differentKeys3(int i) = size((i:10) + (toRat(i,1):20) + (toReal(i):30)) == 3;
