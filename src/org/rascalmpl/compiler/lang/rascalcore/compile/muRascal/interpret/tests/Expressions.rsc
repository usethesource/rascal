module lang::rascalcore::compile::muRascal::interpret::tests::Expressions

extend  lang::rascalcore::compile::muRascal::interpret::tests::TestUtils;
import Exception;

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
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.justTime") == $2013-01-01T08:15:30.055+0100$.justTime;
test bool tst() = run("$2013-01-01T08:15:30.055+0100$.justDate") == $2013-01-01T08:15:30.055+0100$.justDate;

// field update



test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.year += 1; DT;}") == {DT = $2013-01-01T08:15:30.055+0100$; DT.year += 1; DT;};
test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.month += 1; DT;}") == {DT = $2013-01-01T08:15:30.055+0100$; DT.month += 1; DT;};
test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.day += 1; DT;}") == {DT = $2013-01-01T08:15:30.055+0100$; DT.day += 1; DT;};
test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.hour += 1; DT;}") == {DT = $2013-01-01T08:15:30.055+0100$; DT.hour += 1; DT;};
test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.minute += 1; DT;}") == {DT = $2013-01-01T08:15:30.055+0100$; DT.minute += 1; DT;};
test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.second += 1; DT;}") == {DT = $2013-01-01T08:15:30.055+0100$; DT.second += 1; DT;};
test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.millisecond += 1; DT;}") == {DT = $2013-01-01T08:15:30.055+0100$; DT.millisecond += 1; DT;};
test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.timezoneOffsetHours +=1; DT;}") == {DT = $2013-01-01T08:15:30.055+0100$; DT.timezoneOffsetHours +=1; DT;};
test bool tst() = run("{DT = $2013-01-01T08:15:30.055+0100$; DT.timezoneOffsetMinutes += 1; DT; }") == {DT = $2013-01-01T08:15:30.055+0100$; DT.timezoneOffsetMinutes += 1; DT; };


// Location

// Many issues here where field access should generate an exception

test bool tst() = run("|file:///home/paulk/pico.trm|(0,1,\<2,3\>,\<4,5\>)") == |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
test bool tst() = run("|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|") == (|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|);
test bool tst() = run("|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|") == (|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|);

test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.uri") == |std:///experiments/Compiler/Benchmarks/|.uri;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.scheme") == |std:///experiments/Compiler/Benchmarks/|.scheme;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.authority") == |std:///experiments/Compiler/Benchmarks/|.authority;
test bool tst() = run("|http://www.rascal-mpl.org|.host") == |http://www.rascal-mpl.org|.host;
test bool tst() = run("|http://www.rascal-mpl.org|.port") == |http://www.rascal-mpl.org|.port;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.path") == |std:///experiments/Compiler/Benchmarks/|.path;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.extension") == |std:///experiments/Compiler/Benchmarks/|.extension;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/|.fragment") == |std:///experiments/Compiler/Benchmarks/|.fragment;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks|.parent") == |std:///experiments/Compiler/Benchmarks|.parent;
test bool tst() = run("|std:///experiments/Compiler/Benchmarks/Bottles.rsc|.file") == |std:///experiments/Compiler/Benchmarks/Bottles.rsc|.file;
test bool tst() = run("|home:///|.ls") == |home:///|.ls;
test bool tst() = run("|std:///|(11,37,\<1,11\>,\<1,48\>).offset") == |std:///|(11,37,<1,11>,<1,48>).offset;
test bool tst() = run("|std:///|(11,37,\<1,11\>,\<1,48\>).begin.line") == |std:///|(11,37,<1,11>,<1,48>).begin.line;
test bool tst() = run("|std:///|(11,37,\<1,11\>,\<1,48\>).begin.column") == |std:///|(11,37,<1,11>,<1,48>).begin.column;
test bool tst() = run("|std:///|(11,37,\<1,11\>,\<1,48\>).end.line") == |std:///|(11,37,<1,11>,<1,48>).end.line;
test bool tst() = run("|std:///|(11,37,\<1,11\>,\<1,48\>).end.column") == |std:///|(11,37,<1,11>,<1,48>).end.column;

test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x.b;}") == {tuple[int a, str b, int c] x= <1, "x", 2>; x.b;};
test bool tst() = run("{lrel[int a, str b, int c]  x= [ \<1, \"x\", 2\>, \<3, \"y\", 4\> ]; x.b;}") == {lrel[int a, str b, int c]  x= [ <1, "x", 2>, <3, "y", 4> ]; x.b;};


// field update
test bool tst() = run("{L = |std:///experiments/Compiler/Benchmarks/|; L.uri = \"http://www.rascal-mpl.org\"; L;}") == {L = |std:///experiments/Compiler/Benchmarks/|; L.uri = "http://www.rascal-mpl.org"; L;};
test bool tst() = run("{L = |std:///experiments/Compiler/Benchmarks/|; L.scheme= \"xxx\"; L;}") == {L = |std:///experiments/Compiler/Benchmarks/|; L.scheme = "xxx"; L;};
test bool tst() = run("{L = |std:///experiments/Compiler/Benchmarks/|; L.authority= \"xxx\"; L;}") == {L = |std:///experiments/Compiler/Benchmarks/|; L.authority= "xxx"; L;};
test bool tst() = run("{L = |http://www.rascal-mpl.org|; L.host = \"xxx\"; L;}") == {L = |http://www.rascal-mpl.org|; L.host = "xxx"; L;};
test bool tst() = run("{L = |http:///www.rascal-mpl.org|; L.port=123; L;}") =={L = |http:///www.rascal-mpl.org|; L.port=123; L;};
test bool tst() = run("{L = |std:///experiments/Compiler/Benchmarks/|; L.path = \"xxx\"; L;}") == {L = |std:///experiments/Compiler/Benchmarks/|; L.path = "xxx"; L;};
test bool tst() = run("{L = |std:///experiments/Compiler/Benchmarks/|; L.extension=\"xxx\"; L;}") == {L = |std:///experiments/Compiler/Benchmarks/|; L.extension="xxx"; L;};
test bool tst() = run("{L = |std:///experiments/Compiler/Benchmarks/|; L.fragment= \"xxx\"; L;}") == {L = |std:///experiments/Compiler/Benchmarks/|; L.fragment= "xxx"; L;};
test bool tst() = run("{L = |std:///experiments/Compiler/Benchmarks/Bottles.rsc|; L.file = \"xxx\"; L;}") == {L = |std:///experiments/Compiler/Benchmarks/Bottles.rsc|; L.file = "xxx"; L;};

test bool tst() = run("{L = |std:///|(11,37,\<1,11\>,\<1,48\>); L.offset = 100; L;}") == {L = |std:///|(11,37,<1,11>,<1,48>); L.offset = 100; L;};
// Mysterious case: gives true when executed manually.
/*fails*/ //test bool tst() = run("{loc L = |std:///|(11,37,\<1,11\>,\<1,48\>);L.begin = \<1,20\>; L;}") == { loc L =|std:///|(11,37,<1,11>,<1,48>); L.begin= <1,20>; L;};

test bool tst() = run("{L = |std:///|(11,37,\<1,11\>,\<1,48\>); L.end = \<10,20\>; L;}") == {L = |std:///|(11,37,<1,11>,<1,48>); L.end = <10,20>; L;};



// Location templates

test bool tst() = run("{str h = \"home\"; |file:///\<h\>/paulk/pico.trm|;}") == {str h = "home"; |file:///<h>/paulk/pico.trm|;};
test bool tst() = run("{str f = \"file\"; str h = \"home\"; |\<f\>:///\<h\>/paulk/pico.trm|;}") == 	{str f = "file"; str h = "home"; |<f>:///<h>/paulk/pico.trm|;};

// List

test bool tst() = run("[1,2,3]") == [1,2,3];
test bool tst() = run("[1,2,3] + [4,5]") == [1,2,3] + [4,5];
test bool tst() = run("[1,2,3] + 4") == [1,2,3] + 4;


@ignoreInterpreter{Not supported}
test bool tst() = run(" 4 \>\> [1,2,3]") == 4 + [1,2,3];
@ignoreInterpreter{Not supported}
test bool tst() = run(" [1,2,3] \<\< 4") == [1,2,3] + 4;

@ignoreInterpreter{Not supported}
test bool tst() = run("[1,2,3] \<\< 4") == [1,2,3] << 4;
test bool tst() = run("0 + [1,2,3]") == [0,1,2,3];
@ignoreInterpreter{Not supported}
test bool tst() = run("0 \>\> [1,2,3]") == 0 >> [1,2,3];
test bool tst() = run("[1,2,3] & [1,3]") == [1,2,3] & [1,3];
test bool tst() = run("[1,2,3] - [1,3]") == [1,2,3] - [1,3];
test bool tst() = run("1 in [1,2,3]") == 1 in [1,2,3];
test bool tst() = run("1 notin [1,2,3]") == 1 notin [1,2,3];


test bool tst() = run("[1, 2] \< [1, 2, 3]") == [1, 2] < [1, 2, 3];
test bool tst() = run("[1, 2, 3] \< [1, 2, 3]") == [1, 2, 3] < [1, 2, 3];
test bool tst() = run("[1, 2, 3] \< [1, 2]") == [1, 2, 3] < [1, 2];
test bool tst() = run("[\<1,10\>] \< [\<1,10\>,\<2,20\>]") == [<1,10>] < [<1,10>,<2,20>];


test bool tst() = run("[1, 2] \<= [1, 2, 3]") == [1, 2] <= [1, 2, 3];
test bool tst() = run("[1, 2, 3] \<= [1, 2, 3]") == [1, 2, 3] <= [1, 2, 3];
test bool tst() = run("[1, 2, 3] \<= [1, 2]") == [1, 2, 3] <= [1, 2];
test bool tst() = run("[\<1,10\>] \<= [\<1,10\>,\<2,20\>]") == [<1,10>] <= [<1,10>,<2,20>];

test bool tst() = run("[1, 2] \> [1, 2, 3]") == [1, 2] > [1, 2, 3];
test bool tst() = run("[1, 2, 3] \> [1, 2, 3]") == [1, 2, 3] > [1, 2, 3];
test bool tst() = run("[1, 2, 3] \> [1, 2]") == [1, 2, 3] > [1, 2];
test bool tst() = run("[\<1,10\>] \> [\<1,10\>,\<2,20\>]") == [<1,10>] > [<1,10>,<2,20>];

test bool tst() = run("[1, 2] \>= [1, 2, 3]") == [1, 2] >= [1, 2, 3];
test bool tst() = run("[1, 2, 3] \>= [1, 2, 3]") == [1, 2, 3] >= [1, 2, 3];
test bool tst() = run("[1, 2, 3] \>= [1, 2]") == [1, 2, 3] >= [1, 2];
test bool tst() = run("[\<1,10\>] \>= [\<1,10\>,\<2,20\>]") == [<1,10>] >= [<1,10>,<2,20>];

test bool tst() = run("[1, 2, 3] * [1, 2, 3]") == [1, 2, 3] * [1, 2, 3];

// Returns the same tuples but in different order:
//  [ <1,1>, <1,2>, <1,3>, <2,1>, <2,2>, <2,3>, <3,1>, <3,2>, <3,3> ]
// versus
// [ <3,3>, <3,2>, <3,1>, <2,3>, <2,2>, <2,1>, <1,3>, <1,2>, <1,1> ]
// I prefer the first one (as given by the compiler)

//test bool tst() = run("[1, 2, 3] join [1, 2, 3]") == [1, 2, 3] join [1, 2, 3];

test bool tst() = run("[\<1,10\>, \<2,20\>] join [\<300, 2000\>]") == [<1,10>, <2,20>] join [<300, 2000>];


// Set

test bool tst() = run("{1,2,3}") == {1,2,3};
test bool tst() = run("{1,2,3} + {4,5}") == {1,2,3} + {4,5};
test bool tst() = run("{1,2,3} + 4") == {1,2,3} + 4;
test bool tst() = run("{ res = {}; res += 4; }") == { res = {}; res += 4; };
test bool tst() = run("0 + {1,2,3}") == 0 + {1,2,3};
test bool tst() = run("{1,2,3} & {1,3}") =={1,2,3} & {1,3};
test bool tst() = run("{1,2,3} - {1,3}") =={1,2,3} - {1,3};
test bool tst() = run("1 in {1,2,3}") == 1 in {1,2,3};
test bool tst() = run("1 notin {1,2,3}") == 1 notin {1,2,3};

test bool tst() = run("{1, 2} \< {1, 2, 3}") == ({1, 2} < {1, 2, 3});
test bool tst() = run("{1} \< {1}") == ({1} < {1});
test bool tst() = run("{\<1,10\>} \< {\<1,10\>,\<2,20\>}") == {<1,10>} < {<1,10>,<2,20>};

test bool tst() = run("{1, 2} \<= {1, 2, 3}") == ({1, 2} <= {1, 2, 3});
test bool tst() = run("{\<1,10\>} \<= {\<1,10\>,\<2,20\>}") == {<1,10>} <= {<1,10>,<2,20>};

test bool tst() = run("{1, 2} \> {1, 2, 3}") == ({1, 2} > {1, 2, 3});
test bool tst() = run("{\<1,10\>} \> {\<1,10\>,\<2,20\>}") == {<1,10>} > {<1,10>,<2,20>};

test bool tst() = run("{1, 2} \>= {1, 2, 3}") == ({1, 2} >= {1, 2, 3});
test bool tst() = run("{\<1,10\>} \>= {\<1,10\>,\<2,20\>}") == {<1,10>} >= {<1,10>,<2,20>};

test bool tst() = run("{1, 2} == {1, 2}") == ({1, 2} == {1, 2});
test bool tst() = run("{\<1,10\>} == {\<1,10\>,\<2,20\>}") == ({<1,10>} == {<1,10>,<2,20>});

test bool tst() = run("{1, 2} == {1, 2, 3}") == ({1, 2} == {1, 2, 3});

test bool tst() = run("{1, 2} != {1, 2}") == ({1, 2} != {1, 2});
test bool tst() = run("{1, 2} != {1, 2, 3}") == ({1, 2} != {1, 2, 3});
test bool tst() = run("{\<1,10\>} != {\<1,10\>,\<2,20\>}") == ({<1,10>} != {<1,10>,<2,20>});

test bool tst() = run("{1, 2, 3} * {10, 20, 30}") == {1, 2, 3} * {10, 20, 30};

test bool tst() = run("{1, 2, 3} join {10, 20, 30}") == {1, 2, 3} join {10, 20, 30};

test bool tst() = run("{\<1,10\>, \<2,20\>} join {\<300, 2000\>}") == {<1,10>, <2,20>} join {<300, 2000>};

test bool tst() = run("{ [1], [2], [3] } + [4]") == { [1], [2], [3] } + [4];
test bool tst() = run("[ [1], [2], [3] ] + [4]") == [ [1], [2], [3] ] + [4];
test bool tst() = run("[ {1}, {2}, {3} ] + {4}") == [ {1}, {2}, {3} ] + {4};

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
test bool tst() = run("{ x | int x \<- \"a\"(1,2,3) }") == { x | int x <- "a"(1,2,3) };
test bool tst() = run("\"abc\"(1, true, 3.5,kw1=true)") == "abc"(1, true, 3.5, kw1=true);
test bool tst() = run("\"abc\"(1, true, 3.5,kw1=true,kw2=0)") == "abc"(1, true, 3.5, kw2=0, kw1=true);

// ADT

test bool tst() = run("d1(3, \"a\") \>= d1(2, \"a\")") == d1(3, "a") >= d1(2, "a");
test bool tst() = run("{ x | x \<- d1(3, \"a\") }") == { x | x <- d1(3,"a") };


// Enumerator


@ignoreInterpreter{Interpreter and compiler deviate: compiled code gives true, interpreted code gives false. The compiler is right.}
test bool tst() = run("x \<- []") == x <- [];
test bool tst() = run("int x \<- []") == int x <- [];
test bool tst() = run("x \<- [1,2,3]") == x <- [1,2,3];
test bool tst() = run("int x \<- [1,2,3]") == int x <- [1,2,3];

test bool tst() = run("res = []; for(x \<- [1,2,3]) res = res +[x];", "res") == {res = []; for(x <- [1,2,3]) res = res +[x]; res;};
test bool tst() = run("res = []; for(int x \<- [1,2,3]) res = res +[x];", "res") == {res = []; for(int x <- [1,2,3]) res = res +[x]; res;};
test bool tst() = run("res = []; for(x \<- [1,2,3], x != 2) res = res +[x];", "res") == {res = []; for(x <- [1,2,3], x != 2) res = res +[x]; res;};
test bool tst() = run("res = []; for(int x \<- [1,2,3], x != 2) res = res +[x];", "res") == {res = []; for(int x <- [1,2,3], x != 2) res = res +[x]; res;};

test bool tst() = run("res = []; for([int x, 5] \<- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x];", "res") == {res = []; for([int x, 5] <- [[1,5], [2,5], [3, 5]], x != 2) res = res +[x]; res;};
test bool tst() = run("res = []; for([int x, 5] \<- [[1,6], [2,5], [3, 5]], x != 2) res = res +[x];", "res") == {res = []; for([int x, 5] <- [[1,6], [2,5], [3, 5]], x != 2) res = res +[x]; res;};

test bool tst() = run("res = []; for(int x \<- \<1,2,3,4\>) res = res +[x];", "res") == {res = []; for(int x <- <1,2,3,4>) res = res +[x]; res;};

test bool tst() = run("{ res = []; for([*int x,*int y] \<- [ [1,2],[3,4] ]) { res = res + [x,y]; } res; }") == { res = []; for([*int x,*int y] <- [ [1,2],[3,4] ]) { res = res + [x,y]; } res; };
// Both test succeed when executed at the command line
/*fails*///test bool tst() = run("{ res = []; for(d3([*int x,*int y],[*int z,*int w]) \<- [ d3([1,2],[3,4]) ]) { res = res + [x,y,z,w]; } res; }") == { res = []; for(d3([*int x,*int y],[*int z,*int w]) <- [ d3([1,2],[3,4]) ]) { res = res + [x,y,z,w]; } res; };
/*fails*///test bool tst() = run("{ res = []; for(d3([*int x,*int y],[*int z,*int w]) := d3([1,2],[3,4])) { res = res + [x,y,z,w]; } res; }") == { res = []; for(d3([*int x,*int y],[*int z,*int w]) := d3([1,2],[3,4])) { res = res + [x,y,z,w]; } res; };

// Any

test bool tst() = run("any(x \<- [1,2,13,3], x \> 3)") == any(x <- [1,2,13,3], x > 3);
test bool tst() = run("any(int x \<- [1,2,13,3], x \> 3)") == any(int x <- [1,2,13,3], x > 3);
test bool tst() = run("any(x \<- [1,2,13,3], x \> 20)") == any(x <- [1,2,13,3], x > 20);
test bool tst() = run("any(int x \<- [1,2,13,3], x \> 20)") == any(int x <- [1,2,13,3], x > 20);

// All

test bool tst() = run("all(x \<- [1,2,13,3], x \> 0)") == all(x <- [1,2,13,3], x > 0);
test bool tst() = run("all(int x \<- [1,2,13,3], x \> 0)") == all(int x <- [1,2,13,3], x > 0);
test bool tst() = run("all(x \<- [1,2,13,3], x \> 20)") == all(x <- [1,2,13,3], x > 20);
test bool tst() = run("all(int x \<- [1,2,13,3], x \> 20)") == all(int x <- [1,2,13,3], x > 20);
test bool tst() = run("all(int x \<- [1,2,3], x \>= 2 )") == all(int x <- [1,2,3], x >= 2 );
test bool tst() = run("all(int x \<- [1,2,3], x \<= 2 )") == all(int x <- [1,2,3], x <= 2 );

@ignoreCompiler{Remove-after-transtion-to-compiler: incompatibilities interpreter/compiler, note the ! on the right-hand side}
test bool tst() = run("all(int x \<- [])") == !all(int x <- []);
@ignoreInterpreter{incompatibilities interpreter/compiler, when running as compiled code both sides should be equal}
test bool tst() = run("all(int x \<- [])") == all(int x <- []);

@ignoreCompiler{Remove-after-transtion-to-compiler: incompatibilities interpreter/compiler, note the ! on the right-hand side}
test bool tst() = run("all(i \<- [1,2,3], (i % 2 == 0 || i % 2 == 1))") == !all(i <- [1,2,3], (i % 2 == 0 || i % 2 == 1));
@ignoreInterpreter{incompatibilities interpreter/compiler, when running as compiled code both sides should be equal}
test bool tst() = run("all(i \<- [1,2,3], (i % 2 == 0 || i % 2 == 1))") == all(i <- [1,2,3], (i % 2 == 0 || i % 2 == 1));

@ignoreCompiler{Remove-after-transtion-to-compiler: incompatibilities interpreter/compiler, note the ! on the right-hand side}
test bool tst() = run("all([*x, *y] := [1,2,3], true)") == !all([*x, *y] := [1,2,3], true);
@ignoreInterpreter{incompatibilities interpreter/compiler, when running as compiled code both sides should be equal}
test bool tst() = run("all([*x, *y] := [1,2,3], true)") == all([*x, *y] := [1,2,3], true);


test bool tst() = run("all(int M \<- {1,2}, M == 1, true)") == all(int M <- {1,2}, M == 1, true);


// Range

test bool tst() = run("res = []; for(x \<- [1 .. 3]) res = res + [x];", "res") == {res = []; for(x <- [1 .. 3]) res = res + [x]; res;};
test bool tst() = run("res = []; for(x \<- [3 .. 1]) res = res + [x];", "res") == {res = []; for(x <- [3 .. 1]) res = res + [x]; res;};

test bool tst() = run("res = []; for(x \<- [1, 3 .. 10]) res = res + [x];", "res") == {res = []; for(x <- [1, 3 .. 10]) res = res + [x]; res;};
test bool tst() = run("res = []; for(x \<- [1, 0 .. 10]) res = res + [x];", "res") == {res = []; for(x <- [1, 0 .. 10]) res = res + [x]; res;};

test bool tst() = run("res = []; for(x \<- [10, 8 .. 0]) res = res + [x];", "res") == {res = []; for(x <- [10, 8 .. 0]) res = res + [x]; res;};
test bool tst() = run("res = []; for(x \<- [10, 11 .. 0]) res = res + [x];", "res") == {res = []; for(x <- [10, 11 .. 0]) res = res + [x]; res;};
test bool tst() = run("[1 .. 10]") == [1..10];

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
					   
test bool tst() = run("{ \<x[0] + 1, x[1] + 1\> | x \<- { \<1,2\>, \<3,4\> } }") == { <x[0] + 1, x[1] + 1> | x <- { <1,2>, <3,4> } };

test bool tst() = run("{ l | /l := [ [1, [2]], [[3],[4]] ] }") == { l | /l := [ [1, [2]], [[3],[4]] ] };
test bool tst() = run("{ l | /list[int] l := [ [1, [2]], [[3],[4]] ] }") == { l | /list[int] l := [ [1, [2]], [[3],[4]] ] };

test bool tst() = run("{ l | /l := \< [1, [2]], [[3],[4]] \> }") == { l | /l := <[1, [2]], [[3],[4]] > };
test bool tst() = run("{ l | /list[int] l := \< [1, [2]], [[3],[4]] \> }") == { l | /list[int] l := <[1, [2]], [[3],[4]] > };

test bool tst() = run("{ l | /l := (1: [10,100], 2 : [2, 200]) }") == { l | /l := (1: [10,100], 2 : [2, 200]) };
test bool tst() = run("{ l | /list[int]l := (1: [10,100], 2 : [2, 200]) }") == { l | /list[int]l := (1: [10,100], 2 : [2, 200]) };

// Map Comprehension

test bool tst() = run("(x : 10 * x | x \<- [1 .. 10])") == (x : 10 * x | x <- [1 .. 10]);
// Succeeds on the commandline:
/*fails*///test bool tst() = run("{m = (\"first\" : \"String\", \"last\" : \"String\", \"age\" : \"int\", \"married\" : \"boolean\"); lst = []; for(x \<- m) lst += [x]; lst;}") ==
        //              {m = ( "first"  :  "String" ,  "last"  :  "String" ,  "age"  :  "int" ,  "married"  :  "boolean" ); lst = []; for(x  <- m) lst += [x]; lst;};

// Reducer

test bool tst() = run("( 0 | it + x | x \<- [1,2,3])") ==  (0 | it + x | x <- [1,2,3]);
// Not allowed: test bool tst() = run("( 0 | it + x * (0 | it + y | y \<- [10, 20, 30]) | x \<- [1,2,3])") == ( 0 | it + x * (0 | it + y | y <- [10, 20, 30]) | x <- [1,2,3]);

// Splicing

test bool tst() = run("[1, *[2, 3], 4]") == [1, *[2, 3], 4];
test bool tst() = run("[1, *{2, 3}, 4]") == [1, *{2, 3}, 4];
test bool tst() = run("{1, *[2, 3], 4}") == {1, *[2, 3], 4};
test bool tst() = run("{1, *{2, 3}, 4}") == {1, *{2, 3}, 4};

test bool tst() = run("[*x | x \<- [[1,2],[3,4]]]") == [*x | x <- [[1,2],[3,4]]];
test bool tst() = run("{*x | x \<- [[1,2],[3,4]]}") == {*x | x <- [[1,2],[3,4]]};

// Subscript
test bool tst() = run("{x = [1, 2, 3]; x [1];}") ==  {x = [1, 2, 3]; x [1];};
test bool tst() = run("{x = [1, 2, 3]; x [-1];}") ==  {x = [1, 2, 3]; x [-1];};

test bool tst() = run("{x = \<1, 2, 3\>; x [1];}") ==  {x = <1, 2, 3>; x [1];};
test bool tst() = run("{x = \<1, 2, 3\>; x [-1];}") ==  {x = <1, 2, 3>; x [-1];};

test bool tst() = run("{x = \"abc\"; x [1];}") ==  {x = "abc"; x [1];};
test bool tst() = run("{x = \"abc\"; x [-1];}") ==  {x = "abc"; x [-1];};

test bool tst() = run("{x = \"f\"(1, 2, 3); x [1];}") ==  {x = "f"(1, 2, 3); x [1];};
test bool tst() = run("{x = \"f\"(1, 2, 3); x [-1];}") ==  {x = "f"(1, 2, 3); x [-1];};

test bool tst() = run("{x = d1(1, \"a\"); x [1];}") ==  {x = d1(1, "a"); x [1];};
test bool tst() = run("{x = d1(1, \"a\"); x [-1];}") ==  {x = d1(1, "a"); x [-1];};

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

test bool tst() = run("{{\<1, \"x\", 2\>, \<10, \"xx\", 20\>}[10];}") == {<1, "x", 2>, <10, "xx", 20>}[10];
test bool tst() = run("{{\<1, \"x\", 2\>, \<10, \"xx\", 20\>}[7];}") == {<1, "x", 2>, <10, "xx", 20>}[7];
test bool tst() = run("{{\<1, \"x\", 2\>, \<10, \"xx\", 20\>}[10, \"xx\"];}") == {<1, "x", 2>, <10, "xx", 20>}[10, "xx"];
//test bool tst() = run("{{\<1, \"x\", 2\>, \<10, \"x\", 20\>}[_, \"x\"];}") == {<1, "x", 2>, <10, "x", 20>}[_, "x"];

// Error in interpreter
/*fails*/ //test bool tst() = run("{[\<1, \"x\", 2\>, \<10, \"xx\", 20\>][10];}") == [<1, "x", 2>, <10, "xx", 20>][10];
/*fails*///test bool tst() = run("{[\<1, \"x\", 2\>, \<10, \"xx\", 20\>][7];}") == [<1, "x", 2>, <10, "xx", 20>][7];

test bool tst() = run("{[\<1, \"x\", 2\>, \<10, \"xx\", 20\>][10, \"xx\"];}") == [<1, "x", 2>, <10, "xx", 20>][10, "xx"];
test bool tst() = run("{[\<1, \"x\", 2\>, \<10, \"x\", 20\>][_, \"x\"];}") == [<1, "x", 2>, <10, "x", 20>][_, "x"];


// Projection

test bool tst() = run("\<1,2,3,4\>\<1,3\>") == <1,2,3,4><1,3>;
test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x\<2,1\>;}") == {tuple[int a, str b, int c] x= <1, "x", 2>; x<2,1>;};
test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x\<b,1\>;}") == {tuple[int a, str b, int c] x= <1, "x", 2>; x<b,1>;};

test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x\<2\>;}") == {tuple[int a, str b, int c] x= <1, "x", 2>; x<2>;};
test bool tst() = run("{tuple[int a, str b, int c] x= \<1, \"x\", 2\>; x\<b\>;}") == {tuple[int a, str b, int c] x= <1, "x", 2>; x<b>;};

test bool tst() = run("{{\<1, \"x\", 2\>, \<10, \"xx\", 20\>}\<2,1\>;}") == {<1, "x", 2>, <10, "xx", 20>}<2,1>;
test bool tst() = run("{[\<1, \"x\", 2\>, \<10, \"xx\", 20\>]\<2,1\>;}") == [<1, "x", 2>, <10, "xx", 20>]<2,1>;
test bool tst() = run("{{\<1, \"x\", 2\>, \<10, \"xx\", 20\>}\<1\>;}") == {<1, "x", 2>, <10, "xx", 20>}<1>;
test bool tst() = run("{[\<1, \"x\", 2\>, \<10, \"xx\", 20\>]\<1\>;}") == [<1, "x", 2>, <10, "xx", 20>]<1>;

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

// equality

test bool tst() = run("{ value n = 1; n == 1; }") == { value n = 1; n == 1; };
test bool tst() =  run("{ value n = 1; 1 == n; }") == { value n = 1; 1 == n; };

test bool tst() = run(" 1 == 1.0") == (1 == 1.0);

test bool tst() = run("{\<1,2\>} == {}") == ( {<1,2>} == {} );

// Type related

test bool tst() = run("#int") == #int;
test bool tst() = run("#list[int]") == #list[int];


