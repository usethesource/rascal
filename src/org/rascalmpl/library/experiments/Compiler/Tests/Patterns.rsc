module experiments::Compiler::Tests::Patterns

import  experiments::Compiler::Compile;

value run(str exp) = execute("module TMP value main(list[value] args) = <exp>;");

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