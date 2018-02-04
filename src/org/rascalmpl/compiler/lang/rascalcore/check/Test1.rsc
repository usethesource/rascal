module lang::rascalcore::check::Test1

start syntax D = "dddd" | "eee";

data D = d1(int n) | d2(str s);

value main() = #D;