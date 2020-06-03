@doc{
This module test the memoization feature
}
module lang::rascal::tests::basic::Memoization

import util::Memo;
import Set;

// over the test we can have duplicate random values, so prefix the test run 
private int testCount = 0;

private int callCount;

@memo
private void call(value _) {
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
	for (_ <- [0..10]) {
		for (v <- x) {
		  call(<testCount, v>);
		}
	}
	testCount += 1;
	return callCount == size(x);
}

test bool memoExpire() {
    int callCount2 = 0;

    @memo=maximumSize(10)
    int call2(int i) {
        callCount2 += 1;
        return callCount2;
    }

    for (i <- [0..5]) {
        call2(i);
    }
    
    for (i <- [0..5]) {
        if (call2(i) != i + 1) {
            return false;
        }
    }

    for (i <- [10..10000]) {
        // this should take long as to at least hit the cache limit cleanup
        call2(i);
    }

    @javaClass{org.rascalmpl.library.Prelude}
    java void sleep(int seconds);
    
    sleep(6);

    for (i <- [0..5]) {
        if (call2(i) == i + 1) {
            // should be dropped from cache by now
            return false;
        }
    }
    return true;
}
