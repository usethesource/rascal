module tests::functionality::ProjectionTCTests

import StaticTestingUtils;

public test bool empty1() = unexpectedType("{}\<0\> ;");

public test bool empty1() = unexpectedType("{}\<1\> ;");

public test bool tupleOutOfBounds() = unexpectedType("{\<1,2\>}\<2\> == {2};");
