module experiments::Compiler::Tests::Expressions

import Exception;
extend  experiments::Compiler::Tests::TestUtils;

// Booleans, see separate files Booleans.rsc


// Integers
test bool tst() = run("6") == 6;
test bool tst() = run("-6") == -6;
test bool tst() = run("2 + 3") == (2 + 3);
test bool tst() = run("2 - 3") == (2 - 3);
test bool tst() = run("2 * 3") == (2 * 3);
test bool tst() = run("6 / 3") == (6 / 3);
test bool tst() = run("5 % 3") == (5 % 3);
test bool tst() = run("5 mod 3") == (5 mod 3);

test bool tst() = run("2 \< 3") == (2 < 3);
test bool tst() = run("2 \<= 3") == (2 <= 3);
test bool tst() = run("2 \> 3") == (2 > 3);
test bool tst() = run("2 \>= 3") == (2 >= 3);
test bool tst() = run("2 == 2") == (2 == 2);
test bool tst() = run("2 == 3") == (2 == 3);
test bool tst() = run("2 != 2") == (2 != 2);
test bool tst() = run("2 != 3") == (2 != 3);

// Real
test bool tst() = run("2.3 == 2.3") == (2.3 == 2.3);
test bool tst() = run("-2.3 == -2.3") == (-2.3 == -2.3);
test bool tst() = run("2.5 == 2.3") == (2.5 == 2.3);


// Rational
test bool tst() = run("2r3 == 2r3") == (2r3 == 2r3);
test bool tst() = run("-2r3 == -2r3") == (-2r3 == -2r3);
test bool tst() = run("2r5 == 2r3") == (2r5 == 2r3);

// String
test bool tst() = run("\"abc\"") == "abc";
test bool tst() = run("\"abc\" + \"def\"") == "abc" + "def";

// Datetime

test bool tst() = run("$2012-01-01T08:15:30.055+0100$ == $2012-01-01T08:15:30.055+0100$") == ($2012-01-01T08:15:30.055+0100$ == $2012-01-01T08:15:30.055+0100$);
test bool tst() = run("$2013-01-01T08:15:30.055+0100$ == $2012-01-01T08:15:30.055+0100$") == ($2013-01-01T08:15:30.055+0100$ == $2012-01-01T08:15:30.055+0100$);
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.isDate") == $2013-01-01T08:15:30.055+0100$.isDate;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.isTime") == $2013-01-01T08:15:30.055+0100$.isTime;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.isDateTime") == $2013-01-01T08:15:30.055+0100$.isDateTime;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.century") == $2013-01-01T08:15:30.055+0100$.century;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.year") == $2013-01-01T08:15:30.055+0100$.year;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.month") == $2013-01-01T08:15:30.055+0100$.month;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.day") == $2013-01-01T08:15:30.055+0100$.day;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.hour") == $2013-01-01T08:15:30.055+0100$.hour;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.minute") == $2013-01-01T08:15:30.055+0100$.minute;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.second") == $2013-01-01T08:15:30.055+0100$.second;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.millisecond") == $2013-01-01T08:15:30.055+0100$.millisecond;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.timezoneOffsetHours") == $2013-01-01T08:15:30.055+0100$.timezoneOffsetHours;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.timezoneOffsetMinutes") == $2013-01-01T08:15:30.055+0100$.timezoneOffsetMinutes;


// Location

// Many issues here where field access should generate an exception

test bool tst() = run("|file:///home/paulk/pico.trm|(0,1,\<2,3\>,\<4,5\>)") == |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
test bool tst() = run("|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|") == (|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|);
test bool tst() = run("|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|") == (|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|);

test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.uri") == |std:///experiments/Compiler/Benchmarks/|.uri;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.scheme") == |std:///experiments/Compiler/Benchmarks/|.scheme;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.authority") == |std:///experiments/Compiler/Benchmarks/|.authority;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.host") == |std:///experiments/Compiler/Benchmarks/|.host;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.port") == |std:///experiments/Compiler/Benchmarks/|.port;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.path") == |std:///experiments/Compiler/Benchmarks/|.path;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.extension") == |std:///experiments/Compiler/Benchmarks/|.extension;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.fragment") == |std:///experiments/Compiler/Benchmarks/|.fragment;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.parent") == |std:///experiments/Compiler/Benchmarks/|.parent;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.file") == |std:///experiments/Compiler/Benchmarks/|.file;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.ls") == |std:///experiments/Compiler/Benchmarks/|.ls;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.offset") == |file://-|(11,37,<1,11>,<1,48>).offset;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.begin.line") == |file://-|(11,37,<1,11>,<1,48>).begin.line;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.begin.column") == |file://-|(11,37,<1,11>,<1,48>).begin.column;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.end.line") == |file://-|(11,37,<1,11>,<1,48>).end.line;
/*fails*/ //test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.end.column") == |file://-|(11,37,<1,11>,<1,48>).end.column;

// List

test bool tst() = run("[1,2,3]") == [1,2,3];
test bool tst() = run("[1,2,3] + [4,5]") == [1,2,3] + [4,5];
test bool tst() = run("[1,2,3] + 4") == [1,2,3] + 4;

// << and >> not supported by type checker
/*fails*/ //test bool tst() = run(" 4 \>\> [1,2,3]") == 4 + [1,2,3];
/*fails*/ //test bool tst() = run(" [1,2,3] \<\< 4") == 4 + [1,2,3];

// not supported by interpreter: test bool tst() = run("[1,2,3] \<\< 4") == [1,2,3] << 4;
test bool tst() = run("0 + [1,2,3]") == [0,1,2,3];
// not supported by interpreter: test bool tst() = run("0 \>\> [1,2,3]") == 0 >> [1,2,3];
test bool tst() = run("[1,2,3] & [1,3]") == [1,2,3] & [1,3];
test bool tst() = run("[1,2,3] - [1,3]") == [1,2,3] - [1,3];
test bool tst() = run("1 in [1,2,3]") == 1 in [1,2,3];
test bool tst() = run("1 notin [1,2,3]") == 1 notin [1,2,3];


test bool tst() = run("[1, 2] \< [1, 2, 3]") == [1, 2] < [1, 2, 3];
test bool tst() = run("[1, 2, 3] \< [1, 2, 3]") == [1, 2, 3] < [1, 2, 3];
test bool tst() = run("[1, 2, 3] \< [1, 2]") == [1, 2, 3] < [1, 2];

test bool tst() = run("[1, 2] \<= [1, 2, 3]") == [1, 2] <= [1, 2, 3];
test bool tst() = run("[1, 2, 3] \<= [1, 2, 3]") == [1, 2, 3] <= [1, 2, 3];
test bool tst() = run("[1, 2, 3] \<= [1, 2]") == [1, 2, 3] <= [1, 2];

test bool tst() = run("[1, 2] \> [1, 2, 3]") == [1, 2] > [1, 2, 3];
test bool tst() = run("[1, 2, 3] \> [1, 2, 3]") == [1, 2, 3] > [1, 2, 3];
test bool tst() = run("[1, 2, 3] \> [1, 2]") == [1, 2, 3] > [1, 2];

test bool tst() = run("[1, 2] \>= [1, 2, 3]") == [1, 2] >= [1, 2, 3];
test bool tst() = run("[1, 2, 3] \>= [1, 2, 3]") == [1, 2, 3] >= [1, 2, 3];
test bool tst() = run("[1, 2, 3] \>= [1, 2]") == [1, 2, 3] >= [1, 2];

test bool tst() = run("[1, 2, 3] * [1, 2, 3]") == [1, 2, 3] * [1, 2, 3];

// Returns the same tuples but in different order:
//  [ <1,1>, <1,2>, <1,3>, <2,1>, <2,2>, <2,3>, <3,1>, <3,2>, <3,3> ]
// versus
// [ <3,3>, <3,2>, <3,1>, <2,3>, <2,2>, <2,1>, <1,3>, <1,2>, <1,1> ]
// I prefer the first one (as given by the compiler)

/*fails*/ //test bool tst() = run("[1, 2, 3] join [1, 2, 3]") == [1, 2, 3] join [1, 2, 3];

test bool tst() = run("[\<1,10\>, \<2,20\>] join [\<300, 2000\>]") == [<1,10>, <2,20>] join [<300, 2000>];


// Set

test bool tst() = run("{1,2,3}") == {1,2,3};
test bool tst() = run("{1,2,3} + {4,5}") == {1,2,3} + {4,5};
test bool tst() = run("{1,2,3} + 4") == {1,2,3} + 4;
test bool tst() = run("0 + {1,2,3}") == 0 + {1,2,3};
test bool tst() = run("{1,2,3} & {1,3}") =={1,2,3} & {1,3};
test bool tst() = run("{1,2,3} - {1,3}") =={1,2,3} - {1,3};
test bool tst() = run("1 in {1,2,3}") == 1 in {1,2,3};
test bool tst() = run("1 notin {1,2,3}") == 1 notin {1,2,3};

test bool tst() = run("{1, 2} \< {1, 2, 3}") == ({1, 2} < {1, 2, 3});
test bool tst() = run("{1, 2} \<= {1, 2, 3}") == ({1, 2} <= {1, 2, 3});
test bool tst() = run("{1, 2} \> {1, 2, 3}") == ({1, 2} > {1, 2, 3});
test bool tst() = run("{1, 2} \>= {1, 2, 3}") == ({1, 2} >= {1, 2, 3});
test bool tst() = run("{1, 2} == {1, 2}") == ({1, 2} == {1, 2});
test bool tst() = run("{1, 2} == {1, 2, 3}") == ({1, 2} == {1, 2, 3});
test bool tst() = run("{1, 2} != {1, 2}") == ({1, 2} != {1, 2});
test bool tst() = run("{1, 2} != {1, 2, 3}") == ({1, 2} != {1, 2, 3});

test bool tst() = run("{1, 2, 3} * {10, 20, 30}") == {1, 2, 3} * {10, 20, 30};

test bool tst() = run("{1, 2, 3} join {10, 20, 30}") == {1, 2, 3} join {10, 20, 30};

test bool tst() = run("{\<1,10\>, \<2,20\>} join {\<300, 2000\>}") == {<1,10>, <2,20>} join {<300, 2000>};

// Map

test bool tst() = run("(1 : 10, 2 : 20)") == (1 : 10, 2 : 20);
test bool tst() = run("(1 : 10, 2 : 20) + (3 : 30)") == (1 : 10, 2 : 20) + (3 : 30);
test bool tst() = run("(1 : 10, 2 : 20) & (2 : 20, 3 : 30)") == (1 : 10, 2 : 20) & (2 : 20, 3 : 30);
test bool tst() = run("(1 : 10, 2 : 20) - (2 : 20, 3 : 30)") == (1 : 10, 2 : 20) - (2 : 20, 3 : 30);
test bool tst() = run("(\"a\" : \"A\", \"b\" : \"B\", \"c\" : \"C\", \"d\" : \"D\", \"e\" : \"E\", \"f\" : \"F\", \"g\" : \"G\")")
                   == ("a" : "A", "b" : "B", "c" : "C", "d" : "D", "e" : "E", "f" : "F", "g" : "G");
test bool tst() = run("1 in (1 : 10, 2 : 20)") == 1 in (1 : 10, 2 : 20);
test bool tst() = run("1 notin (1 : 10, 2 : 20)") == 1 notin (1 : 10, 2 : 20);

// Node
test bool tst() = run("\"abc\"(1, true, 3.5)") == "abc"(1, true, 3.5);

// ADT

test bool tst() = run("d1(3, \"a\") \>= d1(2, \"a\")") == d1(3, "a") >= d1(2, "a");


// Enumerator

test bool tst() = run("x \<- []") == x <- [];
test bool tst() = run("x \<- [1,2,3]") == x <- [1,2,3];

test bool tst() = run("res = []; for(x \<- [1,2,3]) res = res +[x];", "res") == {res = []; for(x <- [1,2,3]) res = res +[x]; res;};
test bool tst() = run("res = []; for(x \<- [1,2,3], x != 2) res = res +[x];", "res") == {res = []; for(x <- [1,2,3], x != 2) res = res +[x]; res;};

test bool tst() = run("res = []; for([int x, 5] \<- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x];", "res") == {res = []; for([int x, 5] <- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x]; res;};
test bool tst() = run("res = []; for([int x, 5] \<- [[1,6], [2,5], [3, 5]], x != 2) res = res +[x];", "res") == {res = []; for([int x, 5] <- [[1,6], [2,5], [3, 5]], x != 2) res = res +[x]; res;};

test bool tst() = run("res = []; for(int x \<- \<1,2,3,4\>) res = res +[x];", "res") == {res = []; for(int x <- <1,2,3,4>) res = res +[x]; res;};
// Any

test bool tst() = run("any(x \<- [1,2,13,3], x \> 3)") == any(x <- [1,2,13,3], x > 3);
test bool tst() = run("any(x \<- [1,2,13,3], x \> 20)") == any(x <- [1,2,13,3], x > 20);

// All

test bool tst() = run("all(x \<- [1,2,13,3], x \> 0)") == all(x <- [1,2,13,3], x > 0);
test bool tst() = run("all(x \<- [1,2,13,3], x \> 20)") == all(x <- [1,2,13,3], x > 20);

// Range

test bool tst() = run("res = []; for(x \<- [1 .. 3]) res = res + [x];", "res") == {res = []; for(x <- [1 .. 3]) res = res + [x]; res;};
test bool tst() = run("res = []; for(x \<- [3 .. 1]) res = res + [x];", "res") == {res = []; for(x <- [3 .. 1]) res = res + [x]; res;};

test bool tst() = run("res = []; for(x \<- [1, 3 .. 10]) res = res + [x];", "res") == {res = []; for(x <- [1, 3 .. 10]) res = res + [x]; res;};
test bool tst() = run("res = []; for(x \<- [1, 0 .. 10]) res = res + [x];", "res") == {res = []; for(x <- [1, 0 .. 10]) res = res + [x]; res;};

test bool tst() = run("res = []; for(x \<- [10, 8 .. 0]) res = res + [x];", "res") == {res = []; for(x <- [10, 8 .. 0]) res = res + [x]; res;};
test bool tst() = run("res = []; for(x \<- [10, 11 .. 0]) res = res + [x];", "res") == {res = []; for(x <- [10, 11 .. 0]) res = res + [x]; res;};

// For now, we do not support ranges outside enumerators.
test bool tst() = run("[1 .. 10];") == [1..10];

// List Comprehension

test bool tst() = run("[ x |x \<- [1 .. 10]]") == [ x |x <- [1 .. 10]];
test bool tst() = run("[ x |x \<- [1, 3 .. 10]]") == [ x |x <- [1, 3 .. 10]];
test bool tst() = run("[ x |x \<- [10 .. 1]]") == [ x |x <- [10 .. 1]];
test bool tst() = run("[ x |x \<- [10, 8 .. 1]]") == [ x |x <- [10, 8 .. 1]];
test bool tst() = run("[ x |x \<- [1 .. 10], x % 2 == 1]") == [ x |x <- [1 .. 10], x % 2 == 1];

// Set Comprehension

test bool tst() = run("{ x |x \<- [1 .. 10]}") == { x |x <- [1 .. 10]};
test bool tst() = run("{ x |x \<- [1, 3 .. 10]}") == { x |x <- [1, 3 .. 10]};
test bool tst() = run("{ x |x \<- [10 .. 1]}") == { x |x <- [10 .. 1]};
test bool tst() = run("{ x |x \<- [10, 8 .. 1]}") == { x |x <- [10, 8 .. 1]};
test bool tst() = run("{ x |x \<- [1 .. 10], x % 2 == 1}") == { x |x <- [1 .. 10], x % 2 == 1};
test bool tst() = run("{ds = {0, 1, 2, 3}; {[S, E] |  int S \<- ds, int E \<- (ds - {S})};}") ==
					   {ds = {0, 1, 2, 3}; {[S, E] |  int S  <- ds, int E  <- (ds - {S})};};

// Map Comprehension

test bool tst() = run("(x : 10 * x | x \<- [1 .. 10])") == (x : 10 * x | x <- [1 .. 10]);
test bool tst() = run("{m = (\"first\" : \"String\", \"last\" : \"String\", \"age\" : \"int\", \"married\" : \"boolean\"); lst = []; for(x \<- m) lst += [x]; lst;}") ==
                       {m = ( "first"  :  "String" ,  "last"  :  "String" ,  "age"  :  "int" ,  "married"  :  "boolean" ); lst = []; for(x  <- m) lst += [x]; lst;};

// Reducer

test bool tst() = run("( 0 | it + x | x \<- [1,2,3])") ==  (0 | it + x | x <- [1,2,3]);
// Not allowed: test bool tst() = run("( 0 | it + x * (0 | it + y | y \<- [10, 20, 30]) | x \<- [1,2,3])") == ( 0 | it + x * (0 | it + y | y <- [10, 20, 30]) | x <- [1,2,3]);

// Splicing

test bool tst() = run("[1, *[2, 3], 4]") == [1, *[2, 3], 4];
test bool tst() = run("[1, *{2, 3}, 4]") == [1, *{2, 3}, 4];
test bool tst() = run("{1, *[2, 3], 4}") == {1, *[2, 3], 4};
test bool tst() = run("{1, *{2, 3}, 4}") == {1, *{2, 3}, 4};

// Subscript
test bool tst() = run("{x = [1, 2, 3]; x [1];}") ==  {x = [1, 2, 3]; x [1];};
test bool tst() = run("{x = \<1, 2, 3\>; x [1];}") ==  {x = <1, 2, 3>; x [1];};
test bool tst() = run("{x = \"abc\"; x [1];}") ==  {x = "abc"; x [1];};
test bool tst() = run("{x = \"f\"(1, 2, 3); x [1];}") ==  {x = "f"(1, 2, 3); x [1];};
test bool tst() = run("{x = d1(1, \"a\"); x [1];}") ==  {x = d1(1, "a"); x [1];};

// Subscript (IndexOutOfBounds)
test bool tst() = run("{x = [1, 2, 3]; int elem = 0; try { elem = x[5]; } catch IndexOutOfBounds(int index): { elem = 100 + index; } elem; }", ["Exception"]) 
					== {x = [1, 2, 3]; int elem = 0; try { elem = x[5]; } catch IndexOutOfBounds(int index): { elem = 100 + index; } elem; };
test bool tst() = run("{x = \"abc\"; str elem = \"\"; try { elem = x[6]; } catch IndexOutOfBounds(int index): { s = \"\<index\>\"; elem = \"100\" + s; } elem; }", ["Exception"]) 
					== {x = "abc";   str elem = "";   try { elem = x[6]; } catch IndexOutOfBounds(int index): { s = "<index>";     elem = "100" + s; }   elem; };
test bool tst() = run("{x = \"f\"(1, 2, 3); value elem = 0; try { elem = x[7]; } catch IndexOutOfBounds(int index): { elem = 100 + index; } elem; }", ["Exception"]) 
				   ==  {x = "f"(1, 2, 3);   value elem = 0; try { elem = x[7]; } catch IndexOutOfBounds(int index): { elem = 100 + index; } elem; };
test bool tst() = run("{x = d1(1, \"a\"); value elem = 0; try { x[8]; } catch IndexOutOfBounds(int index): { elem = 100 + index; } elem; }", ["Exception"]) 
				   ==  {x = d1(1, "a");   value elem = 0; try { x[8]; } catch IndexOutOfBounds(int index): { elem = 100 + index; } elem; };

test bool tst() = run("{x = [[1, 2, 3], [10, 20, 30], [100, 200, 300]]; x[1][0];}") == {x = [[1, 2, 3], [10, 20, 30], [100, 200, 300]]; x[1][0];};
test bool tst() = run("{x = [[1, 2, 3], [10, 20, 30], [100, 200, 300]]; x[1][0] = 1000; x[1][0];}") == {x = [[1, 2, 3], [10, 20, 30], [100, 200, 300]]; x[1][0] = 1000; x[1][0];};
test bool tst() = run("{x = (\"a\" : [0,1,2], \"b\" : [10, 20, 30]); x[\"b\"][1] = 1000; x[\"b\"][1];}") == {x = ("a" : [0,1,2], "b" : [10,20,30]); x["b"][1] = 1000; x["b"][1];};
test bool tst() = run("{x = (\"a\" : [0,1,2]); x[\"b\"] = [1000,2000]; x[\"b\"][1];}") == {x = ("a" : [0,1,2]); x["b"] = [1000,2000]; x["b"][1];};

// Projection

test bool tst() = run("\<1,2,3,4\>\<1,3\>") == <1,2,3,4><1,3>;
test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x\<2,1\>;}") == {tuple[int a, str b, int c] x= <1, "x", 2>; x<2,1>;};
test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x\<b,1\>;}") == {tuple[int a, str b, int c] x= <1, "x", 2>; x<b,1>;};

test bool tst() = run("{{\<1, \"x\", 2\>, \<10, \"xx\", 20\>}\<2,1\>;}") == {<1, "x", 2>, <10, "xx", 20>}<2,1>;
test bool tst() = run("{[\<1, \"x\", 2\>, \<10, \"xx\", 20\>]\<2,1\>;}") == [<1, "x", 2>, <10, "xx", 20>]<2,1>;

test bool tst() = run("{(\"a\" : 1, \"b\" : 2, \"c\" : 3)\<1,0,1\>;}") == ("a" : 1, "b" : 2, "c" : 3)<1,0,1>;

// Slicing

test bool tst() = run("[0,1,2,3,4,5,6,7,8,9][2 .. 7]") == [0,1,2,3,4,5,6,7,8,9][2 .. 7];
test bool tst() = run("[0,1,2,3,4,5,6,7,8,9][2, 4 .. 7]") == [0,1,2,3,4,5,6,7,8,9][2, 4 .. 7];
test bool tst() = run("\"abcdefghijklmnopqrstuvwxyz\"[2 .. 7]") == "abcdefghijklmnopqrstuvwxyz"[2 .. 7];
test bool tst() = run("\"abcdefghijklmnopqrstuvwxyz\"[2, 4 .. 7]") == "abcdefghijklmnopqrstuvwxyz"[2, 4 .. 7];
test bool tst() = run("\"abc\"(1,2,3,4,5,6,7,8,9)[2 .. 7]") == "abc"(1,2,3,4,5,6,7,8,9)[2 .. 7];
test bool tst() = run("\"abc\"(1,2,3,4,5,6,7,8,9)[2, 4 .. 7]") == "abc"(1,2,3,4,5,6,7,8,9)[2, 4 .. 7];

// has
test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x has a;}")  == {tuple[int a, str b, int c] x= <1, "x", 2>; x has a;};
test bool tst() = run("{lrel[int a, str b, int c] x= [\<1, \"x\", 2\>]; x has a;}")  == {lrel[int a, str b, int c] x= [<1, "x", 2>]; x has a;};
test bool tst() = run("{rel[int a, str b, int c] x= {\<1, \"x\", 2\>}; x has a;}")  == {rel[int a, str b, int c] x= {<1, "x", 2>}; x has a;};

// Here is an issue finding the alternatives of an ADT, see TypeUtils, hasField
/*fails*/ //test bool tst() = run("{x = d1(3, \"a\"); x has n;}")  == {x = d1(3, "a"); x has n;};

// is
test bool tst() = run("d1(3, \"a\") is d1")  == d1(3, "a") is d1;
test bool tst() = run("\"abc\"(1,2,3,4,5,6,7,8,9) is abc") == "abc"(1,2,3,4,5,6,7,8,9) is abc;

