module experiments::Compiler::Tests::Patterns

import  experiments::Compiler::Compile;

value run(str exp) = execute("module TMP data D = d1(int n) | d2(str s); value main(list[value] args) = <exp>;");

data D = d1(int n) | d2(str s);

// Basic Patterns

test bool tst() = run("1 := 1") == 1 := 1;
test bool tst() = run("1 := 2") == 1 := 2;

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

test bool tst() = run("[*int x, 3, *x] := [1,2,3,1,2]") == [*int x, 3, x] := [1,2,3,1,2] && x == [1, 2];

// Node/Constructor matching

test bool tst() = run("d1(1) := d1(1)") == d1(1) := d1(1);
test bool tst() = run("d1(1) := d1(2)") == d1(1) := d1(2);
test bool tst() = run("d2(\"a\") := d2(\"a\")") == d2("a") := d2("a");
test bool tst() = run("d2(\"a\") := d2(\"b\")") == d2("a") := d2("b");




