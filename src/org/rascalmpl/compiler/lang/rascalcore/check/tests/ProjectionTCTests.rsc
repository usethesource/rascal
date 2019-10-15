@bootstrapParser
module lang::rascalcore::check::tests::ProjectionTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool empty1() = unexpectedType("{}\<0\> ;");

test bool empty2() = unexpectedType("{}\<1\> ;");

test bool tupleOutOfBounds() = unexpectedType("{\<1,2\>}\<2\> == {2};");
