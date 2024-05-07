module lang::rascalcore::compile::Examples::Tst6


import ParseTree;

//syntax A = "a";
//syntax As0 = A* as0;
//syntax As1 = A+ as1;
//
//syntax B = "b";
//syntax Bs = B* bs0;
//
//syntax AB = (A | B)*;
//syntax ABs = (As0 | Bs)*;
//syntax A2 = [A]+ !>> [A];
//syntax B2 = [B]+ !>> [B];
//syntax AB2 = (A2 | B2)*;
//
//syntax C = "c";
//syntax Cs0 = {C ","}* cs0;
//syntax Cs1 = {C ","}+ cs1;
//
//syntax AroundCs0 = "OPENCs0" {C ","}* "CLOSE";
//syntax AroundCs1 = "OPENCs1" {C ","}+ "CLOSE";

syntax D = "d";
//syntax Ds = {D ","}* ds;

lexical E = "e";
//lexical Es = {E ","}* es;

syntax DE = D d E e;

//lexical F = "f";
//lexical Fs = F* fs;
//
//lexical EF = (E | F)*;
//lexical EFs = (Es | Fs)*;
//
//start syntax XorY = x : "x" | y : "y";
//
lexical Layout = [.;];
layout L = Layout* !>> [.;];
//
//lexical MyName = ([A-Z a-z _] !<< [A-Z _ a-z] [0-9 A-Z _ a-z]* !>> [0-9 A-Z _ a-z]) ;
//lexical Mies = ([ab] [cd]);
//syntax Noot  = (("a"|"b") ("c"|"d"));
//
//syntax M[&T] = "[[" &T "]]";
//
//syntax MA = M[A];
//syntax MB = M[B];
//
//syntax N[&T,&U] = "\<\<" &T "," &U "\>\>";
//
//syntax NAB = N[A,B];


test bool concreteMatchDE3() = (DE) `d.e` := [DE] "d.e";
