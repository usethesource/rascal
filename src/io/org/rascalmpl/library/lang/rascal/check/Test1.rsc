module lang::rascal::check::Test1

start syntax A = "a";
syntax B = A fieldA "b";

B bbb;
A aaa = bbb.fieldA;