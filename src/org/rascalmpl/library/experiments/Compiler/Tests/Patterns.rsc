module experiments::Compiler::Tests::Patterns

extend experiments::Compiler::Tests::TestUtils;

//import  experiments::Compiler::Compile;
//
//value run(str exp, bool listing=false, bool debug=false) = 
//   execute("module TMP data D = d1(int n) | d2(str s); value main(list[value] args) = <exp>;",listing=listing,debug=debug);
//
//data D = d1(int n) | d2(str s);

// Literals

// Boolean

test bool tst() = run("true := true") == true := true;
test bool tst() = run("true := false") == true := false;

// Integer

test bool tst() = run("1 := 1") == 1 := 1;
test bool tst() = run("1 := 2") == 1 := 2;

// Real
test bool tst() = run("2.3 := 2.3") == (2.3 := 2.3);
test bool tst() = run("2.5 := 2.3") == (2.5 := 2.3);


// Rational
test bool tst() = run("2r3 := 2r3") == (2r3 := 2r3);
test bool tst() = run("2r5 := 2r3") == (2r5 := 2r3);

// String

test bool tst() = run("\"a\" := \"a\"") == "a" := "a";
test bool tst() = run("\"a\" := \"b\"") == "a" := "b";

// Datetime
// The following two tests fail, since theinterpreter does not support datetime patterns. We are ahead :-)
test bool tst() = run("$2012-01-01T08:15:30.055+0100$ := $2012-01-01T08:15:30.055+0100$") == ($2012-01-01T08:15:30.055+0100$ := $2012-01-01T08:15:30.055+0100$);
test bool tst() = run("$2013-01-01T08:15:30.055+0100$ := $2012-01-01T08:15:30.055+0100$") == ($2013-01-01T08:15:30.055+0100$ := $2012-01-01T08:15:30.055+0100$);

// Location

test bool tst() = run("|http://www.rascal-mpl.org| := |http://www.rascal-mpl.org|") == (|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|);
test bool tst() = run("|http://www.rascal-mpl.org| := |std://demo/basic/Hello.rsc|") == (|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|);

// Basic Patterns

test bool tst() = run("x := 2") == x := 2;
test bool tst() = run("x := 2") == x := 2 && x == 2;

test bool tst() = run("int x := 2") == int x := 2;
test bool tst() = run("int x := 2") == int x := 2 && x == 2;

test bool tst() = run("x:1 := 1") == x:1 := 1;
test bool tst() = run("x:1 := 1") == x:1 := 1 && x == 1;

test bool tst() = run("int x:1 := 1") == int x:1 := 1;
test bool tst() = run("int x:1 := 1") == int x:1 := 1 && x == 1;

test bool tst() = run("[int] 1 := 1") == [int] 1 := 1;

test bool tst() = run("! 1 := 2") == ! 1 := 2;
test bool tst() = run("! 1 := 1") == ! 1 := 1;

// List matching
test bool tst() = run("[1] := [1]") == [1] := [1];
test bool tst() = run("[1] := [2]") == [1] := [2];
test bool tst() = run("[1] := [1,2]") == [1] := [1,2];

test bool tst() = run("[1, x*, 5] := [1,2,3,4,5]") == [1, x*, 5] := [1,2,3,4,5];
test bool tst() = run("[1, x*, 5] := [1,2,3,4,5]") == [1, x*, 5] := [1,2,3,4,5] && x == [2,3,4];

test bool tst() = run("[1, *x, 5] := [1,2,3,4,5]") == [1, *x, 5] := [1,2,3,4,5];
test bool tst() = run("[1, *x, 5] := [1,2,3,4,5]") == [1, *x, 5] := [1,2,3,4,5] && x == [2,3,4];

test bool tst() = run("[1, *int x, 5] := [1,2,3,4,5]") == [1, *int x, 5] := [1,2,3,4,5];
test bool tst() = run("[1, *int x, 5] := [1,2,3,4,5]") == [1, *int x, 5] := [1,2,3,4,5] && x == [2,3,4];

// This seems to be an error in the typechecker:
test bool tst() = run("[*int x, 3, *x] := [1,2,3,1,2]") == [*int x, 3, x] := [1,2,3,1,2] && x == [1, 2];

// Node/Constructor matching

// Begin of unresolved issues: "PANIC: undefined function name TMP"
test bool tst() = run("d1(1,\"a\") := d1(1, \"a\")") == d1(1,"a") := d1(1, "a");
test bool tst() = run("d1(1,\"a\") := d1(2,\"a\")") == d1(1,"a") := d1(2,"a");
test bool tst() = run("d2(\"a\", 1) := d2(\"a\", 1)") == d2("a", 1) := d2("a", 1);
test bool tst() = run("d2(\"a\", 1) := d2(\"b\", 1)") == d2("a", 1) := d2("b", 1);

test bool tst() = run("d1(x, \"a\") := d1(1, \"a\")") == d1(x, "a") := d1(1, "a") && x == 1;
test bool tst() = run("d1(int x, \"a\") := d1(1, \"a\")") == d1(int x, "a") := d1(1, "a") && x == 1;

// End of unresolved issues.

test bool tst() = run("str f(int x, str s) := d1(1, \"a\")") == str f(int x, str s) := d1(1, "a") && x == 1 && s == "a" && f == "d1";




