module lang::rascal::tests::functionality::ConcretePatternTests1

import ParseTree;


syntax OptTestGrammar = A? a B b;

syntax A = "a";
syntax As = A+;

syntax B = "b";
syntax Bs = B*;

syntax C = "c";
syntax Cs = {C ","}+;

syntax D = "d";
syntax Ds = {D ","}*;

test bool concreteMatch01() = (A) `<A a>` := [A] "a";

test bool concreteMatch02() = (As) `<A+ as>` := [As] "a" && "<as>" == "a";

test bool concreteMatch03() = (As) `<A+ as>` := [As] "aa" && "<as>" == "aa";
test bool concreteMatch04() = (As) `<A+ as>` := [As] "aaa" && "<as>" == "aaa";
test bool concreteMatch05() = (As) `a<A+ as>` := [As] "aa" && "<as>" == "a";
test bool concreteMatch06() = (As) `aa<A+ as>` := [As] "aaa" && "<as>" == "a";

test bool concreteMatch07() = (As) `<A+ as>a` := [As] "aa" && "<as>" == "a";
test bool concreteMatch08() = (As) `<A+ as>aa` := [As] "aaa" && "<as>" == "a";

test bool concreteMatch09() = (As) `a<A+ as>a` := [As] "aaa" && "<as>" == "a";
test bool concreteMatch10() = (As) `a<A+ as>a` := [As] "aaaa" && "<as>" == "aa";

test bool concreteMatch11() = (As) `<A+ as1>a<A+ as2>a` := [As] "aaaa" && "<as1>" == "a" && "<as1>" == "a" ;
test bool concreteMatch12() = (As) `<A+ as1>a<A+ as2>a` := [As] "aaaaa" && "<as1>" == "a" && "<as2>" == "aa" ;

test bool concreteMatch13() = (Bs) `<B* bs>` := [Bs] "" && "<bs>" == "";  		
test bool concreteMatch14() = (Bs) `<B* bs>` := [Bs] "b" && "<bs>" == "b";
test bool concreteMatch15() = (Bs) `<B* bs>` := [Bs] "bb" && "<bs>" == "bb";

test bool concreteMatch16() = (Bs) `b<B* bs>` := [Bs] "b" && "<bs>" == ""; 		
test bool concreteMatch17() = (Bs) `b<B* bs>` := [Bs] "bb" && "<bs>" == "b";

test bool concreteMatch18() = (Bs) `<B* bs>b` := [Bs] "bbbb" && "<bs>" == "bbb";
test bool concreteMatch19() = (Bs) `<B* bs>bb` := [Bs] "bbbb" && "<bs>" == "bb";
test bool concreteMatch20() = (Bs) `<B* bs>bbb` := [Bs] "bbbb" && "<bs>" == "b";
test bool concreteMatch21() = (Bs) `<B* bs>bbbb` := [Bs] "bbbb" && "<bs>" == "";	

test bool concreteMatch22() = (Bs) `<B* bs1><B* bs2>` := [Bs] "" && "<bs1>" == "" && "<bs1>" == "";
test bool concreteMatch23() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbb" && "<bs1>" == "" && "<bs2>" == "b";
test bool concreteMatch24() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbbb" && "<bs1>" == "" && "<bs2>" == "bb";
test bool concreteMatch25() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbb" && "<bs1>" == "" && "<bs2>" == "";


test bool concreteMatch26() = (Cs) `<{C ","}+ cs>` := [Cs] "c,c,c,c" && "<cs>" == "c,c,c,c";
test bool concreteMatch27() = (Cs) `c,<{C ","}+ cs>` := [Cs] "c,c,c,c" && "<cs>" == "c,c,c";
test bool concreteMatch28() = (Cs) `c,c,<{C ","}+ cs>` := [Cs] "c,c,c,c" && "<cs>" == "c,c";

test bool concreteMatch29() = (Cs) `<{C ","}+ cs>,c` := [Cs] "c,c,c,c" && "<cs>" == "c,c,c";
test bool concreteMatch30() = (Cs) `<{C ","}+ cs>,c,c` := [Cs] "c,c,c,c" && "<cs>" == "c,c";
test bool concreteMatch31() = (Cs) `<{C ","}+ cs>,c,c,c` := [Cs] "c,c,c,c" && "<cs>" == "c";
test bool concreteMatch32() = (Cs) `<{C ","}+ cs>,c,c,c,c` !:= [Cs] "c,c,c,c";

test bool concreteMatch33() = (Cs) `c,<{C ","}+ cs>,c` := [Cs] "c,c,c" && "<cs>" == "c";
test bool concreteMatch34() = (Cs) `c,<{C ","}+ cs>,c` := [Cs] "c,c,c,c" && "<cs>" == "c,c";

test bool concreteMatch35() = (Cs) `<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs] "c,c" && "<cs1>" == "c" && "<cs2>" == "c";
test bool concreteMatch36() = (Cs) `c,<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs] "c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";

test bool concreteMatch37() = (Cs) `<{C ","}+ cs1>,c,<{C ","}+ cs2>` := [Cs] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";
test bool concreteMatch38() = (Cs) `<{C ","}+ cs1>,<{C ","}+ cs2>,c` := [Cs] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";


test bool concreteMatch39() = (Cs) `c,<{C ","}+ cs1>,c,<{C ","}+ cs2>,c` := [Cs] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";

test bool concreteMatch40() = (Ds) `<{D ","}* ds>` := [Ds] "" && "<ds>" == "";
test bool concreteMatch41() = (Ds) `<{D ","}* ds>` := [Ds] "d,d,d,d" && "<ds>" == "d,d,d,d";
test bool concreteMatch42() = (Ds) `<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d,d";
test bool concreteMatch43() = (Ds) `<{D ","}* ds>,d,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
test bool concreteMatch44() = (Ds) `d,<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
test bool concreteMatch45() = (Ds) `d,d,<{D ","}* ds>` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
test bool concreteMatch46() = (Ds) `d,d,<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d";

test bool concreteMatch47() = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "" && "<ds1>" == "" && "<ds2>" == "";
test bool concreteMatch48() = (Ds) `d,<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
test bool concreteMatch49() = (Ds) `<{D ","}* ds1>,d,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
test bool concreteMatch50() = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>,d` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
test bool concreteMatch51() = (Ds) `<{D ","}* ds1>,d,d,<{D ","}* ds2>,d` := [Ds] "d,d,d,d,d" && "<ds1>" == "" && "<ds2>" == "d,d";
test bool concreteMatch52() = (Ds) `<{D ","}* ds1>,d,d,d,<{D ","}* ds2>` := [Ds] "d,d,d,d,d" && "<ds1>" == "" && "<ds2>" == "d,d";

 
/*TODO:TC*/
//test bool optionalNotPresentIsFalse() = !((A)`a` <- ([OptTestGrammar] "b").a);
//test bool optionalPresentIsTrue() = (A)`a` <- ([OptTestGrammar] "ab").a;