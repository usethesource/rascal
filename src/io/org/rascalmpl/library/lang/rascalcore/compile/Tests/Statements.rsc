module lang::rascalcore::compile::Tests::Statements

extend lang::rascalcore::compile::Tests::TestUtils;

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
                       
test bool tst() = run("{M = (1:10); M[1] ? 0 += 100; M;}") == {M = (1:10); M[1] ? 0 += 100; M;};
test bool tst() = run("{M = (1:10); M[2] ? 0 += 100; M;}") == {M = (1:10); M[2] ? 0 += 100; M;};

test bool tst() = run("{M = (1:10); M[1] ?= 100; M;}") == {M = (1:10); M[1] ?= 100; M;};
test bool tst() = run("{M = (1:10); M[2] ?= 100; M;}") == {M = (1:10); M[2] ?= 100; M;};



// Following tests succeed when executed separately, but fail when executed as part of AllTests,
/*fails*/ //test bool tst() = run("{d = d1(10, \"a\"); d.n = 20; d;}") == { d = d1(10, "a"); d.n = 20; d;};
/*fails*/ //test bool tst() = run("{d = d1(10, \"a\"); d.s = \"b\"; d;}") == { d = d1(10, "a"); d.s = "b"; d;};
/*fails*/ //test bool tst() = run("{d = d1(10, \"a\"); d.n *= 20; d;}") == { d = d1(10, "a"); d.n *= 20; d;};

@ignore{Status of Constructor assignment is unclear, currently not supported}
test bool tst() = run("{d1(x, y) = d1(10, \"a\"); \<x, y\>;}") == { d1(x, y) = d1(10, "a"); <x, y>;};

test bool tst() = run("{ x = [0,1,2,3,4,5]; x[1..3] = [10]; x; }") == { x = [0,1,2,3,4,5]; x[1..3] = [10]; x; };
test bool tst() = run("{ x = [0,1,2,3,4,5,6,7,8]; x[1,3..7] = [10]; x; }") == { x = [0,1,2,3,4,5,6,7,8]; x[1,3..7] = [10]; x; };

// If
test bool tst() = run("{ int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; fail; } n; }")                     == { int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; fail; } n; };
test bool tst() = run("{ int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; fail; } else { n -= 1000; } n; }") == { int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; fail; } else { n -= 1000; } n; };
test bool tst() = run("{ int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; } n; }")                           == { int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; } n; };
test bool tst() = run("{ int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; } else { n -= 1000; } n; }")       == { int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; } else { n -= 1000; } n; };
test bool tst() = run("{ int n = 0; if(false)                          { n += 1; } else { n -= 1000; } n; }")       == { int n = 0; if(false) { n += 1; } else { n -= 1000; } n; };

test bool tst() = run("{ int n = 0; if([*int x, *int y] := [1,2,3,4,5,6]) { n += 1; fail; } else { if(list[int] _ := [1,2,3,4,5,6]) { n += 100; } else { ; } }  n; }") == { int n = 0; if([*int x, *int y] := [1,2,3,4,5,6]) { n += 1; fail; } else { if(list[int] _ := [1,2,3,4,5,6]) { n += 100; } else { ; } }  n; };


// While

test bool tst() = run("i = 10", "while(i \> 0){ append i; i = i - 1;}") == {i = 10; while(i > 0){ append i; i = i - 1;}};
test bool tst() = run("i = 10", "while(i \> 0){ if(i\>5)append i; i = i - 1;}") == {i = 10; while(i > 0){ if(i>5)append i; i = i - 1;}};
test bool tst() = run("i = 10", "while(i \> 0){ i = i - 1; if(i % 2 == 1) continue; append i;}") == {i = 10; while(i > 0){ i = i - 1; if(i % 2 == 1) continue; append i;}};

test bool tst() = run("i = 10", "while(i \> 0){ i = i - 1; if(i == 3) break; append i;}") == {i = 10; while(i > 0){ i = i - 1; if(i == 3) break; append i;}};

// Do


test bool tst() = run("i = 10", "do { append i; i = i - 1;} while(i \> 0);") == {i = 10; do { append i; i = i - 1;} while (i > 0);};
test bool tst() = run("i = 10", "do {i = i - 1; if(i % 2 == 1) continue; append i; } while(i \> 0);") == {i = 10; do {i = i - 1; if(i % 2 == 1) continue; append i; } while(i > 0);};
test bool tst() = run("i = 10", "do {i = i - 1; if(i == 3) break; append i; } while(i \> 0);") == {i = 10; do {i = i - 1; if(i == 3) break; append i; } while(i > 0);};


// Assert

// Not easy to test :-(

// Switch
int sw(int n) { switch(n){case 0: return 0; case 1: return 1; default: return 2;} }
int swb(list[int] l) { int n = 0; switch(l) { case [*int x, *int y]: { n += 1; fail; } case list[int] _ : { n += 100; } } return n; }

test bool tst() = run("x = 7" , "switch(0){case 0: x = 0; case 1: x = 1; default: x = 2;}") == sw(0);
                      
test bool tst() = run("x = 7" , "switch(1){case 0: x = 0; case 1: x = 1; default: x = 2;}") == sw(1);
                      
test bool tst() = run("x = 7" , "switch(2){case 0: x = 0; case 1: x = 1; default: x = 2;}") == sw(2);

test bool tst() = run("{ int n = 0; switch([1,2,3,4,5,6]) { case [*int x, *int y]: { n += 1; fail; } case list[int] _ : { n += 100; } } n; }") == swb([1,2,3,4,5,6]);

                                                                  
// Solve

test bool tst() = run("{rel[int,int] R = {\<1,2\>, \<2,3\>, \<3,4\>}; T = R; solve (T) { T = T + (T o R);} T;}") ==
                       {rel[int,int] R = {<1,2>, <2,3>, <3,4>}; T = R; solve (T) { T = T + (T o R);} T;};       
                       

                   
                                                                  