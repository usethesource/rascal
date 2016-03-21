@bootstrapParser
module lang::rascal::tests::types::ProjectionTCTests

import lang::rascal::tests::types::StaticTestingUtils;

test bool empty1() = unexpectedType("{}\<0\> ;");

test bool empty1() = unexpectedType("{}\<1\> ;");

test bool tupleOutOfBounds() = unexpectedType("{\<1,2\>}\<2\> == {2};");
