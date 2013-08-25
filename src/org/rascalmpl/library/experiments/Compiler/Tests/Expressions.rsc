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

// Strings
test bool tst() = run("\"abc\"") == "abc";

// Datetime

test bool tst() = run("$2012-01-01T08:15:30.055+0100$ == $2012-01-01T08:15:30.055+0100$") == ($2012-01-01T08:15:30.055+0100$ == $2012-01-01T08:15:30.055+0100$);
test bool tst() = run("$2013-01-01T08:15:30.055+0100$ == $2012-01-01T08:15:30.055+0100$") == ($2013-01-01T08:15:30.055+0100$ == $2012-01-01T08:15:30.055+0100$);


// Location

test bool tst() = run("|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|") == (|http://www.rascal-mpl.org| == |http://www.rascal-mpl.org|);
test bool tst() = run("|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|") == (|http://www.rascal-mpl.org| == |std://demo/basic/Hello.rsc|);

// Lists
test bool tst() = run("[1,2,3]") == [1,2,3];

test bool tst() = run("[1,2,3] + [4,5]") == [1,2,3,4,5];
test bool tst() = run("[1,2,3] + 4") == [1,2,3,4];
test bool tst() = run("[1,2,3] \>\> 4") == [1,2,3,4];

test bool tst() = run("0 + [1,2,3]") == [0,1,2,3];
test bool tst() = run("0 \>\> [1,2,3]") == [0,1,2,3];

// Enumerators

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
