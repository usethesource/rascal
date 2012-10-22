module lang::rascal::tests::Equality

public test bool selfEq(value x) = x == x;

public test bool transEq(value x, value y, value z) = x == y && y == z ==> x == z;

public test bool reflexEq(value x, value y) = x == y <==> y == z;

public test bool nonReflexLess(value x, value y) = x < y ==> !(y < x);

public test bool transLess(value x, value y, value z) = (x < y && y < z) ==> x < z;

public test bool lessEq(value x, value y) = x <= y ==> (x == y || x < y);

public test bool lessGreater(value x, value y) = x < y <==> y > x;

