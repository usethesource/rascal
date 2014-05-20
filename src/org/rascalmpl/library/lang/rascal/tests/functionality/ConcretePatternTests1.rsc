module tests::functionality::ConcretePatternTests1

import ParseTree;

syntax A = "a";
syntax As = A+;

syntax B = "b";
syntax Bs = B*;

syntax C = "c";
syntax Cs = {C ","}+;

syntax D = "d";
syntax Ds = {D ","}*;

public test bool concreteMatch01() = (A) `<A a>` := [A] "a";

public test bool concreteMatch02() = (As) `<A+ as>` := [As] "a" && "<as>" == "a";

public test bool concreteMatch03() = (As) `<A+ as>` := [As] "aa" && "<as>" == "aa";
public test bool concreteMatch04() = (As) `<A+ as>` := [As] "aaa" && "<as>" == "aaa";
public test bool concreteMatch05() = (As) `a<A+ as>` := [As] "aa" && "<as>" == "a";
public test bool concreteMatch06() = (As) `aa<A+ as>` := [As] "aaa" && "<as>" == "a";

public test bool concreteMatch07() = (As) `<A+ as>a` := [As] "aa" && "<as>" == "a";
public test bool concreteMatch08() = (As) `<A+ as>aa` := [As] "aaa" && "<as>" == "a";

public test bool concreteMatch09() = (As) `a<A+ as>a` := [As] "aaa" && "<as>" == "a";
public test bool concreteMatch10() = (As) `a<A+ as>a` := [As] "aaaa" && "<as>" == "aa";

public test bool concreteMatch11() = (As) `<A+ as1>a<A+ as2>a` := [As] "aaaa" && "<as1>" == "a" && "<as1>" == "a" ;
public test bool concreteMatch12() = (As) `<A+ as1>a<A+ as2>a` := [As] "aaaaa" && "<as1>" == "a" && "<as2>" == "aa" ;

public test bool concreteMatch13() = (Bs) `<B* bs>` := [Bs] "" && "<bs>" == "";  		
public test bool concreteMatch14() = (Bs) `<B* bs>` := [Bs] "b" && "<bs>" == "b";
public test bool concreteMatch15() = (Bs) `<B* bs>` := [Bs] "bb" && "<bs>" == "bb";

public test bool concreteMatch16() = (Bs) `b<B* bs>` := [Bs] "b" && "<bs>" == ""; 		
public test bool concreteMatch17() = (Bs) `b<B* bs>` := [Bs] "bb" && "<bs>" == "b";

public test bool concreteMatch18() = (Bs) `<B* bs>b` := [Bs] "bbbb" && "<bs>" == "bbb";
public test bool concreteMatch19() = (Bs) `<B* bs>bb` := [Bs] "bbbb" && "<bs>" == "bb";
public test bool concreteMatch20() = (Bs) `<B* bs>bbb` := [Bs] "bbbb" && "<bs>" == "b";
public test bool concreteMatch21() = (Bs) `<B* bs>bbbb` := [Bs] "bbbb" && "<bs>" == "";	

public test bool concreteMatch22() = (Bs) `<B* bs1><B* bs2>` := [Bs] "" && "<bs1>" == "" && "<bs1>" == "";
public test bool concreteMatch23() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbb" && "<bs1>" == "" && "<bs2>" == "b";
public test bool concreteMatch24() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbbb" && "<bs1>" == "" && "<bs2>" == "bb";
public test bool concreteMatch25() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbb" && "<bs1>" == "" && "<bs2>" == "";


public test bool concreteMatch26() = (Cs) `<{C ","}+ cs>` := [Cs] "c,c,c,c" && "<cs>" == "c,c,c,c";
public test bool concreteMatch27() = (Cs) `c,<{C ","}+ cs>` := [Cs] "c,c,c,c" && "<cs>" == "c,c,c";
public test bool concreteMatch28() = (Cs) `c,c,<{C ","}+ cs>` := [Cs] "c,c,c,c" && "<cs>" == "c,c";

public test bool concreteMatch29() = (Cs) `<{C ","}+ cs>,c` := [Cs] "c,c,c,c" && "<cs>" == "c,c,c";
public test bool concreteMatch30() = (Cs) `<{C ","}+ cs>,c,c` := [Cs] "c,c,c,c" && "<cs>" == "c,c";
public test bool concreteMatch31() = (Cs) `<{C ","}+ cs>,c,c,c` := [Cs] "c,c,c,c" && "<cs>" == "c";
public test bool concreteMatch32() = (Cs) `<{C ","}+ cs>,c,c,c,c` !:= [Cs] "c,c,c,c";

public test bool concreteMatch33() = (Cs) `c,<{C ","}+ cs>,c` := [Cs] "c,c,c" && "<cs>" == "c";
public test bool concreteMatch34() = (Cs) `c,<{C ","}+ cs>,c` := [Cs] "c,c,c,c" && "<cs>" == "c,c";

public test bool concreteMatch35() = (Cs) `<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs] "c,c" && "<cs1>" == "c" && "<cs2>" == "c";
public test bool concreteMatch36() = (Cs) `c,<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs] "c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";

public test bool concreteMatch37() = (Cs) `<{C ","}+ cs1>,c,<{C ","}+ cs2>` := [Cs] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";
public test bool concreteMatch38() = (Cs) `<{C ","}+ cs1>,<{C ","}+ cs2>,c` := [Cs] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";


public test bool concreteMatch39() = (Cs) `c,<{C ","}+ cs1>,c,<{C ","}+ cs2>,c` := [Cs] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";

public test bool concreteMatch40() = (Ds) `<{D ","}* ds>` := [Ds] "" && "<ds>" == "";
public test bool concreteMatch41() = (Ds) `<{D ","}* ds>` := [Ds] "d,d,d,d" && "<ds>" == "d,d,d,d";
public test bool concreteMatch42() = (Ds) `<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d,d";
public test bool concreteMatch43() = (Ds) `<{D ","}* ds>,d,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
public test bool concreteMatch44() = (Ds) `d,<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
public test bool concreteMatch45() = (Ds) `d,d,<{D ","}* ds>` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
public test bool concreteMatch46() = (Ds) `d,d,<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d";

public test bool concreteMatch47() = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "" && "<ds1>" == "" && "<ds2>" == "";
public test bool concreteMatch48() = (Ds) `d,<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
public test bool concreteMatch49() = (Ds) `<{D ","}* ds1>,d,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
public test bool concreteMatch50() = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>,d` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
public test bool concreteMatch51() = (Ds) `<{D ","}* ds1>,d,d,<{D ","}* ds2>,d` := [Ds] "d,d,d,d,d" && "<ds1>" == "" && "<ds2>" == "d,d";
public test bool concreteMatch52() = (Ds) `<{D ","}* ds1>,d,d,d,<{D ","}* ds2>` := [Ds] "d,d,d,d,d" && "<ds1>" == "" && "<ds2>" == "d,d";

 