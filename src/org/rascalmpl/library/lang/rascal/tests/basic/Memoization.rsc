@doc{
This module test the memoization feature
}
module lang::rascal::tests::basic::Memoization

import Set;

// over the test we can have duplicate random values, so prefix the test run 
private int testCount = 0;

private int callCount;

@memo
private void call(value x) {
	callCount += 1;
}

test bool memoCalledCorrectly(set[value] x) {
	callCount = 0;
	for (v <- x) {
	  call(<testCount, v>);
	}
	testCount += 1;
	return callCount == size(x);
}
test bool memoCalledCorrectly2(set[value] x) {
	callCount = 0;
	for (i <- [0..10]) {
		for (v <- x) {
		  call(<testCount, v>);
		}
	}
	testCount += 1;
	return callCount == size(x);
}