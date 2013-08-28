module experiments::Compiler::Tests::Statements

import  experiments::Compiler::Compile;

value run(str stats, bool listing=false, bool debug=false) = 
	execute("module TMP data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) { return <stats> }", listing=listing, debug=debug);
	
data D = d1(int n, str s) | d2(str s, bool b); 	

// Assignables	
	
test bool tst() = run("{ x = 1; x; }") == {x = 1; x;};
test bool tst() = run("{ x = 10; x += 5; x; }") == {x = 10; x += 5; x;};
test bool tst() = run("{ x = 10; x -= 5; x; }") == {x = 10; x -= 5; x;};
test bool tst() = run("{ x = 10; x *= 5; x; }") == {x = 10; x *= 5; x;};
test bool tst() = run("{ x = 10; x /= 5; x; }") == {x = 10; x /= 5; x;};
test bool tst() = run("{ x = {1,2,3}; x &= {3,4}; x; }") == { x = {1,2,3}; x &= {3,4}; x; };

test bool tst() = run("{ x = [0,1,2]; x[1] = 10; x; }") == { x = [0,1,2]; x[1] = 10; x; };
test bool tst() = run("{ x = [[0,1,2],[10,20,30],[100,200,300]]; x[0][1] = 7; x; }") == { x = [[0,1,2],[10,20,30],[100,200,300]]; x[0][1] = 7; x; };
test bool tst() = run("{ x = (1:10, 2:20,3:30); x[1] = 100; x; }") == { x = (1:10, 2:20,3:30); x[1] = 100; x; };

test bool tst() = run("{ x = [0,1,2]; x[1] += 10; x; }") == { x = [0,1,2]; x[1] += 10; x; };
test bool tst() = run("{ x = (1:10, 2:20,3:30); x[1] += 100; x; }") == { x = (1:10, 2:20,3:30); x[1] += 100; x; };

test bool tst() = run("{ \<x, y\> = \<1,2\>;  x + y; }") == { <x, y> = <1,2>;  x + y; };
test bool tst() = run("{ z = \<1,2\>; \<x, y\> = z;  x + y; }") == { z = <1,2>; <x, y> = z;  x + y; };

test bool tst() = run("{x = [1,2,3]; z = \<10,20\>; \<x[2], y\> = z; x[2] + y; }") == {x = [1,2,3]; z = <10,20>; <x[2], y> = z;  x[2] + y; };


test bool tst() = run("{d = d1(10, \"a\"); d.n = 20; d;}") == { d = d1(10, "a"); d.n = 20; d;};
test bool tst() = run("{d = d1(10, \"a\"); d.s = \"b\"; d;}") == { d = d1(10, "a"); d.s = "b"; d;};

test bool tst() = run("{d = d1(10, \"a\"); d.n *= 20; d;}") == { d = d1(10, "a"); d.n *= 20; d;};

test bool tst() = run("{d1(x, y) = d1(10, \"a\"); \<x, y\>;}") == { d1(x, y) = d1(10, "a"); <x, y>;};










