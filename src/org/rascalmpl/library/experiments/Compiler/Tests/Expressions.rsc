module experiments::Compiler::Tests::Expressions

import  experiments::Compiler::Compile;

value run(str exp) = execute("module TMP value main(list[value] args) = <exp>;");

// Booleans

test bool tst() = run("true") == true;
test bool tst() = run("false") == false;

test bool tst() = run("true && true") == true && true;
test bool tst() = run("true && false") == true && false;

test bool tst() = run("true || true") == true || true;
test bool tst() = run("true || false") == true || false;

test bool tst() = run("true ==\> true") == true ==> true;
test bool tst() = run("true ==\> false") == true ==> false;

test bool tst() = run("true \<==\> true") == true <==> true;
test bool tst() = run("true \<==\> false") == true <==> false;

test bool tst() = run("true ? 1 : 2") == true ? 1 : 2;
test bool tst() = run("false ? 1 : 2") == false ? 1 : 2;

// Integers
test bool tst() = run("6") == 6;
test bool tst() = run("2 + 3") == 2 + 3;
test bool tst() = run("2 - 3") == 2 - 3;
test bool tst() = run("2 * 3") == 2 * 3;
test bool tst() = run("6 / 3") == 6 / 3;
test bool tst() = run("5 % 3") == 5 % 3;

test bool tst() = run("2 \< 3") == 2 < 3;
test bool tst() = run("2 \<= 3") == 2 <= 3;
test bool tst() = run("2 \> 3") == 2 > 3;
test bool tst() = run("2 \>= 3") == 2 >= 3;
test bool tst() = run("2 == 2") == (2 == 2);
test bool tst() = run("2 == 3") == (2 == 3);
test bool tst() = run("2 != 2") == (2 != 2);
test bool tst() = run("2 != 3") == (2 != 3);


// Strings
test bool tst() = run("\"abc\"") == "abc";

// Lists
test bool tst() = run("[1,2,3]") == [1,2,3];

test bool tst() = run("[1,2,3] + [4,5]") == [1,2,3,4,5];
test bool tst() = run("[1,2,3] + 4") == [1,2,3,4];
test bool tst() = run("[1,2,3] \>\> 4") == [1,2,3,4];

test bool tst() = run("0 + [1,2,3]") == [0,1,2,3];
test bool tst() = run("0 \>\> [1,2,3]") == [0,1,2,3];


