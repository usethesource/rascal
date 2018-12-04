module lang::rascalcore::compile::muRascal::interpret::tests::Booleans

extend  lang::rascalcore::compile::muRascal::interpret::tests::TestUtils;

// No backtracking cases

test bool btrue() = run("true") == true;
test bool bfalse() = run("false") == false;

test bool bnot1() = run("!true") == !true;
test bool bnot2() = run("!false") == !false;

test bool band1() = run("true && true") == (true && true);
test bool band2() = run("true && false") == (true && false);

test bool bor1() = run("true || true") == (true || true);
test bool bor2() = run("true || false") == (true || false);

test bool bimp1() = run("true ==\> true") == (true ==> true);
test bool bimp2() = run("true ==\> false") == (true ==> false);

test bool beq1() = run("true \<==\> true") == (true <==> true);
test bool beq2() = run("true \<==\> false") == (true <==> false);


test bool bcond1() = run("true ? 1 : 2") == (true ? 1 : 2);
test bool bcond2() = run("false ? 1 : 2") == (false ? 1 : 2);

test bool bcond3() = run("{v = 2 \> 1; v ? 10 : 20;}") == {v = 2 > 1; v ? 10 : 20;};

// Assign outcome of Boolean operation

test bool basg1() = run("{x = true && true; x;}") == {x = true && true; x;};
test bool basg2() = run("{x = true && true; y = x; y;}") == {x = true && true; y = x; y;};
test bool basg3() = run("{x = true && true; x && false;}") == {x = true && true; x && false;};
test bool basg4() = run("{x = true && true; if(x) 10; else 20;}") == {x = true && true; if(x) 10; else 20;};
test bool basg5() = run("{x = true && false; if(x) 10; else 20;}") == {x = true && false; if(x) 10; else 20;};

test bool basg6() = run("{x = 3 \> 2; x;}") == {x = 3 > 2; x;};
test bool basg7() = run("{x = 3 \> 2; y = x; y;}") == {x = 3 > 2; y = x; y;};
test bool basg8() = run("{x = 3 \> 2; x && false;}") == {x = 3 > 2; x && false;};
test bool basg9() = run("{x = 3 \> 2; if(x) 10; else 20;}") == {x = 3 > 2; if(x) 10; else 20;};
test bool basg10() = run("{x = 3 \> 2; if(x) 10; else 20;}") == {x = 3 > 2; if(x) 10; else 20;};

// && with backtrackable arguments

test bool btand1() = run("x \<- [1,2] && x == 1") == (x <- [1,2] && x == 1) ;
test bool btand2() = run("x \<- [1,2] && x == 2") == (x <- [1,2] && x == 2);
test bool btand3() = run("x \<- [1,2] && x == 3") == (x <- [1,2] && x == 3);

test bool btand15() = run("x \<- [1,2] && x == 1 && y \<- [5,6] && y == 5") == (x <- [1,2] && x == 1 && y <- [5,6] && y == 5);
test bool btand16() = run("x \<- [1,2] && x == 1 && y \<- [5,6] && y == 6") == (x <- [1,2] && x == 1 && y <- [5,6] && y == 6);
test bool btand17() = run("x \<- [1,2] && x == 1 && y \<- [5,6] && y == 7") == (x <- [1,2] && x == 1 && y <- [5,6] && y == 7);

test bool btand25() = run("x \<- [1,2] && x == 2 && y \<- [5,6] && y == 5") == (x <- [1,2] && x == 2 && y <- [5,6] && y == 5);
test bool btand26() = run("x \<- [1,2] && x == 2 && y \<- [5,6] && y == 6") == (x <- [1,2] && x == 2 && y <- [5,6] && y == 6);
test bool btand27() = run("x \<- [1,2] && x == 2 && y \<- [5,6] && y == 7") == (x <- [1,2] && x == 2 && y <- [5,6] && y == 7);

test bool btand36() = run("(x \<- [1,2] && x == 3) && (y \<- [5,6] && y == 6)") == ((x <- [1,2] && x == 3) && (y <- [5,6] && y == 6));
test bool btand37() = run("(x \<- [1,2] && x == 3) && (y \<- [5,6] && y == 7)") == ((x <- [1,2] && x == 3) && (y <- [5,6] && y == 7));

//////test bool bt11() = run("{for( ([*int x,*int y] := [1,2,3]) && ([*int p, *int q] := [4,5,6]) ) {append \<x, y, p, q\>;}}") ==
//////    	               {for( ([*int x,*int y] := [1,2,3]) && ([*int p,*int q] := [4,5,6]) ) {append <x, y, p, q>; }};
//////    	               
//////test bool bt12() = run("{for( ([*int x,*int y] := [1,2,3]) , ([*int p, *int q] := [4,5,6]) ) {append \<x, y, p, q\>;}}") ==
//////    	               {for( ([*int x,*int y] := [1,2,3]) , ([*int p,*int q] := [4,5,6]) ) {append <x, y, p, q>; }};
//////
// || with backtrackable arguments

test bool btor1() = run("(x \<- [1,2] && x == 2) || (y \<- [5,6] && y == 6)") == ((x <- [1,2] && x == 2) || (y <- [5,6] && y == 6));
test bool btor2() = run("(x \<- [1,2] && x == 2) || (y \<- [5,6] && y == 7)") == ((x <- [1,2] && x == 2) || (y <- [5,6] && y == 7));
test bool btor3() = run("(x \<- [1,2] && x == 3) || (y \<- [5,6] && y == 6)") == ((x <- [1,2] && x == 3) || (y <- [5,6] && y == 6));
test bool btor4() = run("(x \<- [1,2] && x == 3) || (y \<- [5,6] && y == 7)") == ((x <- [1,2] && x == 3) || (y <- [5,6] && y == 7));

//test bool btor5() = run("{for( ([*int x,*int y] := [1,2,3]) || ([*int x,*int y] := [4,5,6]) ) {append \<x, y\>;}}") == 
//                       {for( ([*int x,*int y] := [1,2,3]) || ([*int x,*int y] := [4,5,6]) ) {append <x, y>; }};

// ! with backtrackable argument

test bool btnot1() = run("!(x \<- [1,2] && x == 2)") == !(x <- [1,2] && x == 2) ;
test bool btnot2() = run("!(x \<- [1,2] && x == 3)") == !(x <- [1,2] && x == 3) ;

// ==> with backtrackable arguments

test bool btimp1() = run("(x \<- [1,2] && x == 2) ==\> (y \<- [5,6] && y == 6)") == ((x <- [1,2] && x == 2) ==> (y <- [5,6] && y == 6));
test bool btimp2() = run("(x \<- [1,2] && x == 2) ==\> (y \<- [5,6] && y == 7)") == ((x <- [1,2] && x == 2) ==> (y <- [5,6] && y == 7));
test bool btimp3() = run("(x \<- [1,2] && x == 3) ==\> (y \<- [5,6] && y == 6)") == ((x <- [1,2] && x == 3) ==> (y <- [5,6] && y == 6));
test bool btimp4() = run("(x \<- [1,2] && x == 3) ==\> (y \<- [5,6] && y == 7)") == ((x <- [1,2] && x == 3) ==> (y <- [5,6] && y == 7));
//
////@ignore{The semantics of ==> and <==> is not clear yet}
////test bool tst() = run("{for( ([*int x,*int y] := [1,2,3]) ==\> ([*int x,*int y] := [4,5,6]) ) {append \<x, y\>;}}") ==
////    	               {for( ([*int x,*int y] := [1,2,3]) ==> ([*int x,*int y] := [4,5,6]) ) {append <x, y>; }};
//
// <==> with backtrackable arguments

//test bool bteq1() = run("(x \<- [1,2] && x == 2) \<==\> (y \<- [5,6] && y == 6)") == (x <- [1,2] && x == 2) <==> (y <- [5,6] && y == 6);
  test bool bteq2() = run("(x \<- [1,2] && x == 2) \<==\> (y \<- [5,6] && y == 7)") == (x <- [1,2] && x == 2) <==> (y <- [5,6] && y == 7);
  test bool bteq3() = run("(x \<- [1,2] && x == 3) \<==\> (y \<- [5,6] && y == 6)") == (x <- [1,2] && x == 3) <==> (y <- [5,6] && y == 6);
test bool bteq4() = run("(x \<- [1,2] && x == 3) \<==\> (y \<- [5,6] && y == 7)") == (x <- [1,2] && x == 3) <==> (y <- [5,6] && y == 7);

//
//@ignore{The interpreter complains about undefined variable "x"}
//test bool tst() = run("{for( ([*int x,*int y] := [1,2,3]) \<==\> ([*int x,*int y] := [4,5,6]) ) {append \<x, y\>;}}") ==
//    	               {for( ([*int x,*int y] := [1,2,3]) <==> ([*int x,*int y] := [4,5,6]) ) {append <x, y>; }};
//
//// Miscellaneous
//
//test bool tst() = run("[*int x, 3, *x] := [1,2,3,1,2] && x == [1, 2]") == [*int x, 3, *x] := [1,2,3,1,2] && x == [1, 2];
//
// Shortcut evaluation

//test bool scand1() = run("{int n = 0; bool b(bool x) { n += 1; return x;}; b(true) && b(true); n;}") == 
//                       {int n = 0; bool b(bool x) { n += 1; return x;}; b(true) && b(true); n;};
                       
//test bool scand2() = run("{int n = 0; bool b(bool x) { n += 1; return x;}; b(false) && b(true); n;}") == 
//                       {int n = 0; bool b(bool x) { n += 1; return x;}; b(false) && b(true); n;};
//                       
//test bool scor1() = run("{int n = 0; bool b(bool x) { n += 1; return x;}; b(true) || b(true); n;}") == 
//                       {int n = 0; bool b(bool x) { n += 1; return x;}; b(true) || b(true); n;};
//                       
//test bool scor2() = run("{int n = 0; bool b(bool x) { n += 1; return x;}; b(false) || b(true); n;}") == 
//                       {int n = 0; bool b(bool x) { n += 1; return x;}; b(false) || b(true); n;};
//                       
//test bool scimp1() = run("{int n = 0; bool b(bool x) { n += 1; return x;}; b(true) ==\> b(true); n;}") == 
//                       {int n = 0; bool b(bool x) { n += 1; return x;}; b(true) ==> b(true); n;};
//                       
//test bool scimp2() = run("{int n = 0; bool b(bool x) { n += 1; return x;}; b(false) ==\> b(true); n;}") == 
//                       {int n = 0; bool b(bool x) { n += 1; return x;}; b(false) ==> b(true); n;};
//                       
//test bool sceq1() = run("{int n = 0; bool b(bool x) { n += 1; return x;}; b(true) \<==\> b(true); n;}") == 
//                       {int n = 0; bool b(bool x) { n += 1; return x;}; b(true) <==> b(true); n;};
//                       
//test bool sceq2() = run("{int n = 0; bool b(bool x) { n += 1; return x;}; b(false) \<==\> b(true); n;}") == 
//                       {int n = 0; bool b(bool x) { n += 1; return x;}; b(false) <==> b(true); n;};
