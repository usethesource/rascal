module lang::rascalcore::compile::Examples::Tst1

import util::Memo;
import Set;

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