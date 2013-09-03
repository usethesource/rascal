module experiments::Compiler::Tests::Expressions

import  experiments::Compiler::Compile;

value run(str exp, bool listing=false, bool debug=false) = 
	execute("module TMP value main(list[value] args) = <exp>;", listing=listing, debug=debug);
	
value run(str before, str exp, bool listing=false, bool debug=false) = 
	execute("module TMP value main(list[value] args) {<before> ; return <exp>;}", listing=listing, debug=debug);

// Booleans

test bool tst() = run("true") == true;
test bool tst() = run("false") == false;

test bool tst() = run("!true") == !true;
test bool tst() = run("!false") == !false;

test bool tst() = run("true && true") == (true && true);
test bool tst() = run("true && false") == (true && false);

test bool tst() = run("true || true") == (true || true);
test bool tst() = run("true || false") == (true || false);

test bool tst() = run("true ==\> true") == (true ==> true);
test bool tst() = run("true ==\> false") == (true ==> false);

test bool tst() = run("true \<==\> true") == (true <==> true);
test bool tst() = run("true \<==\> false") == (true <==> false);

test bool tst() = run("true ? 1 : 2") == (true ? 1 : 2);
test bool tst() = run("false ? 1 : 2") == (false ? 1 : 2);

test bool tst() = run("{b = 2 \> 1; b ? 10 : 20;}") == {b = 2 > 1; b ? 10 : 20;};

// Integers
test bool tst() = run("6") == 6;
test bool tst() = run("2 + 3") == (2 + 3);
test bool tst() = run("2 - 3") == (2 - 3);
test bool tst() = run("2 * 3") == (2 * 3);
test bool tst() = run("6 / 3") == (6 / 3);
test bool tst() = run("5 % 3") == (5 % 3);

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
test bool tst() = run("2.5 == 2.3") == (2.5 == 2.3);


// Rational
test bool tst() = run("2r3 == 2r3") == (2r3 == 2r3);
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

// Various issues here where field access should generate an exception
test bool tst() = run("|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|") == (|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|);
test bool tst() = run("|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|") == (|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|);

test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.uri") == |std:///experiments/Compiler/Benchmarks/|.uri;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.scheme") == |std:///experiments/Compiler/Benchmarks/|.scheme;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.authority") == |std:///experiments/Compiler/Benchmarks/|.authority;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.host") == |std:///experiments/Compiler/Benchmarks/|.host;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.port") == |std:///experiments/Compiler/Benchmarks/|.port;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.path") == |std:///experiments/Compiler/Benchmarks/|.path;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.extension") == |std:///experiments/Compiler/Benchmarks/|.extension;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.fragment") == |std:///experiments/Compiler/Benchmarks/|.fragment;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.parent") == |std:///experiments/Compiler/Benchmarks/|.parent;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.file") == |std:///experiments/Compiler/Benchmarks/|.file;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.ls") == |std:///experiments/Compiler/Benchmarks/|.ls;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.offset") == |file://-|(11,37,<1,11>,<1,48>).offset;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.begin.line") == |file://-|(11,37,<1,11>,<1,48>).begin.line;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.begin.column") == |file://-|(11,37,<1,11>,<1,48>).begin.column;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.end.line") == |file://-|(11,37,<1,11>,<1,48>).end.line;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.end.column") == |file://-|(11,37,<1,11>,<1,48>).end.column;

// List

test bool tst() = run("[1,2,3]") == [1,2,3];
test bool tst() = run("[1,2,3] + [4,5]") == [1,2,3] + [4,5];
test bool tst() = run("[1,2,3] + 4") == [1,2,3] + 4;
// not supported by interpreter: test bool tst() = run("[1,2,3] \<\< 4") == [1,2,3] << 4;
test bool tst() = run("0 + [1,2,3]") == [0,1,2,3];
// not supported by interpreter: test bool tst() = run("0 \>\> [1,2,3]") == 0 >> [1,2,3];
test bool tst() = run("[1,2,3] & [1,3]") == [1,2,3] & [1,3];
test bool tst() = run("[1,2,3] - [1,3]") == [1,2,3] - [1,3];
test bool tst() = run("1 in [1,2,3]") == 1 in [1,2,3];
test bool tst() = run("1 notin [1,2,3]") == 1 notin [1,2,3];

// Set

test bool tst() = run("{1,2,3}") == {1,2,3};
test bool tst() = run("{1,2,3} + {4,5}") == {1,2,3} + {4,5};
test bool tst() = run("{1,2,3} + 4") == {1,2,3} + 4;
test bool tst() = run("0 + {1,2,3}") == 0 + {1,2,3};
test bool tst() = run("{1,2,3} & {1,3}") =={1,2,3} & {1,3};
test bool tst() = run("{1,2,3} - {1,3}") =={1,2,3} - {1,3};
test bool tst() = run("1 in {1,2,3}") == 1 in {1,2,3};
test bool tst() = run("1 notin {1,2,3}") == 1 notin {1,2,3};

// Map

test bool tst() = run("(1 : 10, 2 : 20)") == (1 : 10, 2 : 20);
test bool tst() = run("(1 : 10, 2 : 20) + (3 : 30)") == (1 : 10, 2 : 20) + (3 : 30);
test bool tst() = run("(1 : 10, 2 : 20) & (2 : 20, 3 : 30)") == (1 : 10, 2 : 20) & (2 : 20, 3 : 30);
test bool tst() = run("(1 : 10, 2 : 20) - (2 : 20, 3 : 30)") == (1 : 10, 2 : 20) - (2 : 20, 3 : 30);
test bool tst() = run("1 in (1 : 10, 2 : 20)") == 1 in (1 : 10, 2 : 20);
test bool tst() = run("1 notin (1 : 10, 2 : 20)") == 1 notin (1 : 10, 2 : 20);

// Enumerator

test bool tst() = run("x \<- []") == x <- [];
test bool tst() = run("x \<- [1,2,3]") == x <- [1,2,3];

test bool tst() = run("res = []; for(x \<- [1,2,3]) res = res +[x];", "res") == {res = []; for(x <- [1,2,3]) res = res +[x]; res;};
test bool tst() = run("res = []; for(x \<- [1,2,3], x != 2) res = res +[x];", "res") == {res = []; for(x <- [1,2,3], x != 2) res = res +[x]; res;};

test bool tst() = run("res = []; for([int x, 5] \<- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x];", "res") == {res = []; for([int x, 5] <- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x]; res;};
test bool tst() = run("res = []; for([int x, 5] \<- [[1,6], [2,5], [3, 5]], x != 2) res = res +[x];", "res") == {res = []; for([int x, 5] <- [[1,6], [2,5], [3, 5]], x != 2) res = res +[x]; res;};

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

// Map Comprehension

test bool tst() = run("(x : 10 * x | x \<- [1 .. 10])") == (x : 10 * x | x <- [1 .. 10]);

// Reducer

test bool tst() = run("( 0 | it + x | x \<- [1,2,3])") ==  (0 | it + x | x <- [1,2,3]);
// Not allowed: test bool tst() = run("( 0 | it + x * (0 | it + y | y \<- [10, 20, 30]) | x \<- [1,2,3])") == ( 0 | it + x * (0 | it + y | y <- [10, 20, 30]) | x <- [1,2,3]);

// Splicing

test bool tst() = run("[1, *[2, 3], 4]") == [1, *[2, 3], 4];
test bool tst() = run("[1, *{2, 3}, 4]") == [1, *{2, 3}, 4];
test bool tst() = run("{1, *[2, 3], 4}") == {1, *[2, 3], 4};
test bool tst() = run("{1, *{2, 3}, 4}") == {1, *{2, 3}, 4};

// Subscript
test bool tst() = run("{x = [[1, 2, 3], [10, 20, 30], [100, 200, 300]]; x[1][0];}") == {x = [[1, 2, 3], [10, 20, 30], [100, 200, 300]]; x[1][0];};
test bool tst() = run("{x = [[1, 2, 3], [10, 20, 30], [100, 200, 300]]; x[1][0] = 1000; x[1][0];}") == {x = [[1, 2, 3], [10, 20, 30], [100, 200, 300]]; x[1][0] = 1000; x[1][0];};
test bool tst() = run("{x = (\"a\" : [0,1,2], \"b\" : [10, 20, 30]); x[\"b\"][1] = 1000; x[\"b\"][1];}") == {x = ("a" : [0,1,2], "b" : [10,20,30]); x["b"][1] = 1000; x["b"][1];};
test bool tst() = run("{x = (\"a\" : [0,1,2]); x[\"b\"] = [1000,2000]; x[\"b\"][1];}") == {x = ("a" : [0,1,2]); x["b"] = [1000,2000]; x["b"][1];};

// Projection

test bool tst() = run("\<1,2,3,4\>\<1,3\>") == <1,2,3,4><1,3>;
test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x\<b,1\>;}") == {tuple[int a, str b, int c] x= <1, "x", 2>; x<b,1>;};
test bool tst() = run("{{\<1, \"x\", 2\>, \<10, \"xx\", 20\>}\<2,1\>;}") == {<1, "x", 2>, <10, "xx", 20>}<2,1>;



