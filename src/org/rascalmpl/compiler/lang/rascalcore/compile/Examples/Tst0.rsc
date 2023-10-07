module lang::rascalcore::compile::Examples::Tst0

//module lang::rascal::tests::basic::Equality

import util::Math;
import Set;
import Map;
import Type;
import Node;

// the only way two values can be equal while their run-time types are not is due to conversion between int, real, rat by `==`
test bool canonicalTypes(&T x, &Y y) = x == y ==> (typeOf(x) == typeOf(y)) || size({typeOf(x), typeOf(y)} & {\int(), \real(), \rat()}) > 1;

test bool canonicalTypesRegression1() = canonicalTypes(0.0, 0);
test bool canonicalTypesRegression2() = canonicalTypes(0r, 0);
test bool canonicalTypesRegression3() = canonicalTypes(0r, 0.0);
  
// values have an equivalence relation
test bool reflexEq1(value x) = x == x;
test bool transEq1(value x, value y, value z) = (x == y && y == z) ==> (x == z);
test bool commutativeEq1(value x, value y) = (x == y) <==> (y == x);

// the matching operator is also an equivalence relation on values:
test bool reflexEq2(value x) = x := x;
test bool transEq2(value x, value y, value z) = (x := y && y := z) ==> (x := z);
test bool commutativeEq2(value x, value y) = (x := y) <==> (y := x);

// equality subsumes matching, but we focus on nodes to avoid problems with num coercions of `==`:
test bool allEqualValuesMatch(node a, node b) = a == b ==> a := b;
test bool noMatchImpliesUnequal(node a, node b) = !(a := b) ==> a != b;

test bool matchIsEqualityModuloKeywordFields(node x, node y) 
  = (unsetRec(x) == unsetRec(y)) <==> x := y;

// values have an equivalence relation, and by requiring the arguments to have the same types we may trigger bugs sooner:
test bool transEqSame(&Same x, &Same y, &Same z) = (x == y && y == z) ==> (x == z);
test bool commutativeEqSame(&Same x, &Same y) = (x == y) <==> (y == x);

// values are partially ordered
test bool reflexLTE(value x) = (x <= x);
test bool antiSymmetricLTE(value x, value y) = (x <= y && y <= x) ==> (x == y);
test bool transLTE(value x, value y, value z) = (x <= y && y <= z) ==> x <= z;

// values are partially ordered, and by requiring the arguments to have the same type we may trigger bugs sooner:
@Ignore
test bool antiSymmetricLTESame(&Same <: node x , &Same <: node y) = (x <= y && y <= x) ==> (x == y);
test bool transLTESame(&Same <: node x, &Same <: node y, &Same <: node z) = (x <= y && y <= z) ==> x <= z;

@Ignore
test bool antiSymmetricLTEWithKeywordParamsLt1() = antiSymmetricLTESame(""(), ""(x = 3));
@Ignore 
test bool antiSymmetricLTEWithKeywordParamsLt2() = antiSymmetricLTESame(""(x = 2), ""(x = 3));
@Ignore 
test bool antiSymmetricLTEWithKeywordParamsEq() = antiSymmetricLTESame(""(x = 3), ""(x = 3)); 

// numbers are totally ordered
test bool numTotalLTE1(num x, num y) = x <= y || y <= x;
test bool numAntiSymmetricLTE(num x, num y) = (x <= y && y <= x) ==> (x == y);
test bool numTransLTE(num x, num y, num z) = (x <= y && y <= z) ==> (x <= z);
test bool numValueReflex(num x) { value y = x; return x == y && y == x; }

// ints are totally ordered
test bool intTotalLTE(int x, int y) = x <= y || y <= x;
test bool intAntiSymmetricLTE(int x, int y) = (x <= y && y <= x) ==> (x == y);
test bool intTransLTE(int x, int y, int z) = (x <= y && y <= z) ==> (x <= z);
test bool intValueReflex(int x) { value y = x; return x == y && y == x; }

// reals are totally ordered
test bool realTotalLTE(real x, real y) = x <= y || y <= x;
test bool realAntiSymmetricLTE(real x, real y) = (x <= y && y <= x) ==> (x == y);
test bool realTransLTE(real x, real y, real z) = (x <= y && y <= z) ==> (x <= z);
test bool realValueReflex(real x) { value y = x; return x == y && y == x; }

// rat are totally ordered
test bool ratTotalLTE(rat x, rat y) = x <= y || y <= x;
test bool ratAntiSymmetricLTE(rat x, rat y) = (x <= y && y <= x) ==> (x == y);
test bool ratTransLTE(rat x, rat y, rat z) = (x <= y && y <= z) ==> (x <= z);
test bool ratValueReflex(rat x) { value y = x; return x == y && y == x; }

// strings are totally ordered
test bool numTotalLTE2(str x, str y) = x <= y || y <= x;
test bool strAntiSymmetricLTE(str x, str y) = (x <= y && y <= x) ==> (x == y);
test bool strTransLTE(str x, str y, str z) = (x <= y && y <= z) ==> x <= z;
test bool strValueReflex(rat x) { value y = x; return x == y && y == x; }

// lists are partially ordered
test bool listReflexLTE(list[value] x) = (x <= x);
test bool listAntiSymmetricLTE(list[value] x, list[value] y) = (x <= y && y <= x) ==> (x == y);
test bool listTransLTE(list[value] x, list[value] y, list[value] z) = (x <= y && y <= z) ==> x <= z;

// sets are ordered via sub-set relation
test bool subsetOrdering1(set[value] x, set[value] y) = x <= x + y; 
test bool subsetOrdering2(set[value] x, set[value] y) = (x <= y) <==> (x == {} || all(e <- x, e in y));

// sets are partially ordered
test bool setReflexLTE1(set[value] x) = (x <= x);
test bool setAntiSymmetricLTE1(set[value] x, set[value] y) = (x <= y && y <= x) ==> (x == y);
test bool setTransLTE1(set[value] x, set[value] y, set[value] z) = (x <= y && y <= z) ==> x <= z;

// map are ordered via sub-map relation

/*TODO:

java.lang.Exception: Test submapOrdering1 failed due to
    io.usethesource.vallang.type.IntegerType cannot be cast to io.usethesource.vallang.util.TrieMap$CompactMapNode

Actual parameters:
    map[value, value] =>(true:("":[]))
    map[value, value] =>("":0.5741726876359169,true:-405555075,639525932r165438573:233378841r1234953134,"M"(true,|tmp:///|):true,|tmp:///|:|tmp:///g7/J|)

*/
test bool submapOrdering1(map[value,value] x, map[value,value] y) = x <= y + x; // remember map join is not commutative

/*TODO:
java.lang.Exception: failed for arguments: (true:"",-1185257414:"1sn"({""()},"冖񓱍资"(|tmp:///|),-304421973r46873778,["R7jZ"()])) 
                                           (true:"",$3632-03-24T14:03:39.476+01:00$:["0Xo","",""],|tmp:///|:$2015-08-06T08:23:51.810+01:00$,|tmp:///R66k|:<"h7"()>) 
*/
test bool submapOrdering2(map[value,value]x, map[value,value] y) = (x <= y) <==> (x == () || all(e <- x, e in y, eq(y[e], x[e])));

// maps are partially ordered
test bool setReflexLTE2(map[value,value] x) = (x <= x);
test bool setAntiSymmetricLTE2(map[value,value] x, map[value,value] y) = (x <= y && y <= x) ==> (x == y);
test bool setTransLTE2(map[value,value] x, map[value,value] y, map[value,value] z) = (x <= y && y <= z) ==> x <= z;

// locs are partially ordered
test bool locReflexLTE(loc x) = (x <= x);
test bool locAntiSymmetricLTE(loc x, loc y) = (x <= y && y <= x) ==> (x == y);
test bool locTransLTE(loc x, loc y, loc z) = (x <= y && y <= z) ==> x <= z;

// conversions
test bool intToReal1(int i) = i == toReal(i);
test bool ratToReal1(rat r) = r == toReal(r);
test bool intToReal2(int i) = i <= toReal(i);
test bool ratToReal2(rat r) = r <= toReal(r);
test bool intToReal3(int i) = toReal(i) >= i;
test bool ratToReal3(rat r) = toReal(r) >= r;
test bool lessIntReal(int i) = !(i < toReal(i));
test bool lessRatReal(int i) = !(i < toReal(i));

// set containment
test bool differentElements(int i) = size({i, toReal(i), toRat(i,1)}) == 3; // yes, really 3.
test bool differentElement2(int i, rat r) = i == r ==> size({i,r}) == 2; // yes, really 2.
test bool differentElement3(int i, real r) = i == r ==> size({i,r}) == 2; // yes, really 2.

// map keys
test bool differentKeys1(int i,real r) = ((i:10,r:20)[toReal(i)]?0) == 0;
test bool differentKeys2(int i,rat r) = ((i:10,r:20)[toRat(i,1)]?0) == 0;
test bool differentKeys3(int i) = size((i:10) + (toRat(i,1):20) + (toReal(i):30)) == 3;

// == vs eq
test bool eqImpliesEquals(value x, value y) = eq(x,y) ==> (x == y);
test bool nonComparabilityImpliesNonEq(value x, value y) = !comparable(typeOf(x),typeOf(y)) ==> !eq(x,y);
test bool comparabilityImpliesEquivalence(value x, value y) = comparable(typeOf(x),typeOf(y)) ==> (eq(x,y) <==> x == y);

