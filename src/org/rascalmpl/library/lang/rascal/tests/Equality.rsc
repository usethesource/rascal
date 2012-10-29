module lang::rascal::tests::Equality


// values have an equivalence relation
public test bool selfEq(value x) = x == x;

public test bool transEq(value x, value y, value z) = (x == y && y == z) ==> (x == z);

public test bool reflexEq(value x, value y) = (x == y) <==> (y == x);

// values are partially ordered

public test bool reflexLTE(value x) = (x <= x);

public test bool antiSymmetricLTE(value x, value y) = (x <= y && y <= x) ==> (x == y);

public test bool transLTE(value x, value y, value z) = (x <= y && y <= z) ==> x <= z;

// numbers are totally ordered

public test bool numTotalLTE(num x, num y) = x <= y || y <= x;

public test bool numAntiSymmetricLTE(num x, num y) = (x <= y && y <= x) ==> (x == y);

public test bool numTransLTE(num x, num y, num z) = (x <= y && y <= z) ==> x <= z;

// strings are totally ordered

public test bool numTotalLTE(str x, str y) = x <= y || y <= x;

public test bool strAntiSymmetricLTE(str x, str y) = (x <= y && y <= x) ==> (x == y);

public test bool strTransLTE(str x, str y, str z) = (x <= y && y <= z) ==> x <= z;



