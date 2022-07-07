module lang::rascal::tests::extend_function1::M3

import lang::rascal::tests::extend_function1::M2;

import Exception;

int f(3) = 30;

test bool t1() {
    try {
       f(1);
       return false;
    } catch CallFailed(_): return true;
}
test bool t2() = f(2) == 20;
test bool t3() = f(3) == 30;