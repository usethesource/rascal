module lang::rascalcore::check::Test3

test bool T1() = [1] <= [1, 2];
test bool T2() = [1] <= [1, 1, 2];
test bool T3() = !([1, 1] <= [1, 2]);

data A = a() | b();

test bool T1() = [a()] <= [a(), b()];
test bool T2() = [a()] <= [a(), a(), b()];
test bool T3() = !([a(), a()] <= [a(), b()]);

test bool T4() = [a(), a()] & [a(), b()] == [a()];

