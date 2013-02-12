module lang::rascal::tests::Matching

import List;
import IO;

syntax A = a: "a";

syntax As = as: A+ alist;

syntax C = c: A a "x" As as;

test bool tstA(){
    pt = parse(#A, "a");
    return a() := pt && pt is a;
}

test bool tstAs(){
    pt = parse(#As, "aaa");
    return as(al) := pt && pt is as && pt.alist == al;
}

test bool tstC(){
    pt = parse(#C, "axaaa");
    return c(A a, As as) := pt && pt.a == a && pt.as == as && size([x | x <- as.alist]) == 3;
}

