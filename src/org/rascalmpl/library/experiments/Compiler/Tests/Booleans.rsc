module experiments::Compiler::Tests::Booleans

extend  experiments::Compiler::Tests::TestUtils;

// No backtracking cases

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

// Assign of outcome of Boolean operation

test bool tst() = run("{x = true && true; x;}") == {x = true && true; x;};
test bool tst() = run("{x = true && true; y = x; y;}") == {x = true && true; y = x; y;};
test bool tst() = run("{x = true && true; x && false;}") == {x = true && true; x && false;};
test bool tst() = run("{x = true && true; if(x) 10; else 20;}") == {x = true && true; if(x) 10; else 20;};
test bool tst() = run("{x = true && false; if(x) 10; else 20;}") == {x = true && false; if(x) 10; else 20;};


test bool tst() = run("{x = 3 \> 2; x;}") == {x = 3 > 2; x;};
test bool tst() = run("{x = 3 \> 2; y = x; y;}") == {x = 3 > 2; y = x; y;};
test bool tst() = run("{x = 3 \> 2; x && false;}") == {x = 3 > 2; x && false;};
test bool tst() = run("{x = 3 \> 2; if(x) 10; else 20;}") == {x = 3 > 2; if(x) 10; else 20;};
test bool tst() = run("{x = 3 \> 2; if(x) 10; else 20;}") == {x = 3 > 2; if(x) 10; else 20;};

test bool tst() = run("x \<- [1,2] && x == 1") == (x <- [1,2] && x == 1) ;
test bool tst() = run("x \<- [1,2] && x == 2") == (x <- [1,2] && x == 2);
test bool tst() = run("x \<- [1,2] && x == 3") == (x <- [1,2] && x == 3);

test bool tst() = run("x \<- [1,2] && x == 2 && y \<- [5,6] && y == 5") == (x <- [1,2] && x == 2 && y <- [5,6] && y == 5);
test bool tst() = run("x \<- [1,2] && x == 2 && y \<- [5,6] && y == 6") == (x <- [1,2] && x == 2 && y <- [5,6] && y == 6);
test bool tst() = run("x \<- [1,2] && x == 2 && y \<- [5,6] && y == 7") == (x <- [1,2] && x == 2 && y <- [5,6] && y == 7);

test bool tst() = run("(x \<- [1,2] && x == 2) || (y \<- [5,6] && y == 6)") == (x <- [1,2] && x == 2) || (y <- [5,6] && y == 6);
test bool tst() = run("(x \<- [1,2] && x == 3) || (y \<- [5,6] && y == 6)") == (x <- [1,2] && x == 3) || (y <- [5,6] && y == 6);
test bool tst() = run("(x \<- [1,2] && x == 3) || (y \<- [5,6] && y == 7)") == (x <- [1,2] && x == 3) || (y <- [5,6] && y == 7);




test bool tst() = run("[*int x, 3, *x] := [1,2,3,1,2] && x == [1, 2]") == [*int x, 3, x] := [1,2,3,1,2] && x == [1, 2];