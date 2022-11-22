module lang::rascal::tests::library::Type

import Type;

data D = a(bool b) | a(str s1, str s2) | a(int n, str color = "blue") |  a(str s, int sz = 10);

test bool tstMake1() = make(#D, "a", [true]) ==  a(true);

test bool tstMake2() = make(#D, "a", ["x", "y"]) ==  a("x", "y");

test bool tstMake3() = make(#D, "a", [3], ("color" : "red")) ==  a(3, color="red");

test bool tstMake4() = make(#D, "a", ["x"], ("sz" : 20)) ==  a("x", sz = 20);
