module lang::rascal::tests::functionality::ConcretePatternTests1

import ParseTree;

syntax OptTestGrammar = A? a B b;

syntax A = "a";
syntax As0 = A* as0;
syntax As1 = A+ as1;

syntax B = "b";
syntax Bs = B* bs0;

syntax AB = (A | B)*;
syntax ABs = (As0 | Bs)*;
syntax A2 = [A]+ !>> [A];
syntax B2 = [B]+ !>> [B];
syntax AB2 = (A2 | B2)*;

syntax C = "c";
syntax Cs0 = {C ","}* cs0;
syntax Cs1 = {C ","}+ cs1;

syntax AroundCs0 = "OPENCs0" {C ","}* "CLOSE";
syntax AroundCs1 = "OPENCs1" {C ","}+ "CLOSE";

syntax D = "d";
syntax Ds = {D ","}* ds;

lexical E = "e";
lexical Es = {E ","}* es;

lexical F = "f";
lexical Fs = F* fs;

lexical EF = (E | F)*;
lexical EFs = (Es | Fs)*;

start syntax XorY = x : "x" | y : "y";

lexical Layout = [.;];
layout L = Layout* !>> [.;];

lexical MyName = ([A-Z a-z _] !<< [A-Z _ a-z] [0-9 A-Z _ a-z]* !>> [0-9 A-Z _ a-z]) ;
lexical Mies = ([ab] [cd]);
syntax Noot  = (("a"|"b") ("c"|"d"));

syntax M[&T] = "[[" &T "]]";

syntax MA = M[A];
syntax MB = M[B];

syntax N[&T,&U] = "\<\<" &T "," &U "\>\>";

syntax NAB = N[A,B];

test bool concreteMatch01() = (A) `<A a>` := [A] "a";
test bool concreteMatch01a() = (A) `a` := [A] "a";

test bool concreteMatch02() = (As1) `<A+ as>` := [As1] "a" && "<as>" == "a";

test bool concreteMatch03() = (As1) `<A+ as>` := [As1] "aa" && "<as>" == "aa";
test bool concreteMatch04() = (As1) `<A+ as>` := [As1] "aaa" && "<as>" == "aaa";
test bool concreteMatch05() = (As1) `a<A+ as>` := [As1] "aa" && "<as>" == "a";
test bool concreteMatch06() = (As1) `aa<A+ as>` := [As1] "aaa" && "<as>" == "a";

test bool concreteMatch07() = (As1) `<A+ as>a` := [As1] "aa" && "<as>" == "a";
test bool concreteMatch08() = (As1) `<A+ as>aa` := [As1] "aaa" && "<as>" == "a";

test bool concreteMatch09() = (As1) `a<A+ as>a` := [As1] "aaa" && "<as>" == "a";
test bool concreteMatch10() = (As1) `a<A+ as>a` := [As1] "aaaa" && "<as>" == "aa";

test bool concreteMatch11() = (As1) `<A+ as1>a<A+ as2>a` := [As1] "aaaa" && "<as1>" == "a" && "<as1>" == "a" ;
test bool concreteMatch12() = (As1) `<A+ as1>a<A+ as2>a` := [As1] "aaaaa" && "<as1>" == "a" && "<as2>" == "aa" ;

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


test bool concreteMatch26() = (Cs1) `<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c,c";
test bool concreteMatch27() = (Cs1) `c,<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c";
test bool concreteMatch28() = (Cs1) `c,c,<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";

test bool concreteMatch29() = (Cs1) `<{C ","}+ cs>,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c";
test bool concreteMatch30() = (Cs1) `<{C ","}+ cs>,c,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";
test bool concreteMatch31() = (Cs1) `<{C ","}+ cs>,c,c,c` := [Cs1] "c,c,c,c" && "<cs>" == "c";
test bool concreteMatch32() = (Cs1) `<{C ","}+ cs>,c,c,c,c` !:= [Cs1] "c,c,c,c";

test bool concreteMatch33() = (Cs1) `c,<{C ","}+ cs>,c` := [Cs1] "c,c,c" && "<cs>" == "c";
test bool concreteMatch34() = (Cs1) `c,<{C ","}+ cs>,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";

test bool concreteMatch35() = (Cs1) `<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs1] "c,c" && "<cs1>" == "c" && "<cs2>" == "c";
test bool concreteMatch36() = (Cs1) `c,<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs1] "c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";

test bool concreteMatch37() = (Cs1) `<{C ","}+ cs1>,c,<{C ","}+ cs2>` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";
test bool concreteMatch38() = (Cs1) `<{C ","}+ cs1>,<{C ","}+ cs2>,c` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";


test bool concreteMatch39() = (Cs1) `c,<{C ","}+ cs1>,c,<{C ","}+ cs2>,c` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";

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

test bool concreteListEnum1() = ["<x>" | B x <- ((Bs) ``).bs0] == [];
test bool concreteListEnum2() = ["<x>" | B x <- ((Bs) `b`).bs0] == ["b"];
test bool concreteListEnum3() = ["<x>" | B x <- ((Bs) `bbbbb`).bs0] == ["b", "b", "b", "b", "b"];
test bool concreteListEnum4() = ["<x>" | D x <- ((Ds) ``).ds] == [];
test bool concreteListEnum5() = ["<x>" | D x <- ((Ds) `d`).ds] == ["d"];
test bool concreteListEnum6() = ["<x>" | D x <- ((Ds) `d,d,d,d,d`).ds] == ["d", "d", "d", "d", "d"];

test bool lexicalListEnum1() = ["<x>" | E x <- ((Es) `e,e,e,e,e,e,e`).es] == ["e", "e", "e", "e", "e", "e", "e"];
test bool lexicalListEnum2() = ["<x>" | F x <- ((Fs) `ffffff`).fs] == ["f", "f", "f", "f", "f", "f"];

test bool lexicalSequenceMatch() = (Mies) `ac` !:= (Mies) `ad`;
test bool syntaxSequenceMatch() = (Noot) `ac` !:= (Noot) `ad`;
test bool lexicalTokenMatch1() = (MyName) `location` := (MyName) `location`;
test bool lexicalTokenMatch2() = (MyName) `location` := [MyName] "location";


test bool concreteMatchVisit() {
  result = false;
  visit ([A]"a") {
    case (A)`<A _>`: result = true;
  }
  return result;
}
test bool concreteMatchVisit() {
  result = 0;
  visit ([As1]"aaa") {
    case (A)`<A _>`: result += 1;
  }
  return result == 3;
}

@ignoreInterpreter{While this should work, the fix is to large, and there are workarounds}
test bool concreteMatchVisitLayout() {
  result = false;
  visit ([start[XorY]] ".x.") {
    case (Layout)`.`: result = true;
  }
  return result;
}
test bool concreteReplaceInLayout() 
  = visit([start[XorY]] ".x;") {
    case (Layout)`.` => (Layout)`;`
  } == [start[XorY]] ";x;";

test bool concreteMatchWithStart()
  = /XorY _ := [start[XorY]]";x;";

test bool concreteSwitch1(){
	switch([XorY] "x"){
		case (XorY) `x`: return true;
	}
	return false;
}

test bool concreteSwitch2(){
	switch([XorY] "x"){
		case (XorY) `x`: return true;
		case (XorY) `y`: return false;
	}
	return false;
}

test bool concreteSwitch3(){
	switch([XorY] "y"){
		case (XorY) `x`: return false;
		case (XorY) `y`: return true;
	}
	return false;
}

test bool concreteSwitch4(){
	switch([XorY] "y"){
		case x(): 		 throw "fail to due extra match";
		case (XorY) `y`: return true;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch5(){
	switch([XorY] "y"){
		case (XorY) `x`: throw "fail to due extra match"; 
		case y(): 		 return true;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch6(){
	switch([XorY] "y"){
		case x(): 		 throw "fail to due extra match";
		case y(): 		 return true;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch7(){
	switch([As1] "aaa"){
		case (As1) `<A+ as>`: 		
				return "<as>" == "aaa"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch8(){
	switch([As1] "a.a.a"){
		case (As1) `<A+ as>`: 		
				return "<as>" == "a.a.a"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch9(){
	switch([Bs] "bbb"){
		case (Bs) `<B* bs>`: 		
				return "<bs>" == "bbb"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch10(){
	switch([Bs] "b..b..b"){
		case (Bs) `<B* bs>`: 		
				return "<bs>" == "b..b..b"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch11(){
	switch([Cs1] "c,c,c,c"){
		case (Cs1) `<{C ","}+ cs>`: 		
				return "<cs>" == "c,c,c,c"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch12(){
	switch([Cs1] "c.,.c.,.c.,.c"){
		case (Cs1) `<{C ","}+ cs>`: 		
				return "<cs>" == "c.,.c.,.c.,.c"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch13(){
	switch([Ds] "d,d,d,d"){
		case (Ds) `<{D","}* ds>`: 		
				return "<ds>" == "d,d,d,d"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}

test bool concreteSwitch14(){
	switch([Ds] "d.,.d.,.d.,.d"){
		case (Ds) `<{D","}* ds>`: 		
				return "<ds>" == "d.,.d.,.d.,.d"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}

test bool matchInsideLexicalCyclicGrammar1() 
    = /E _ := [EFs]"eefef";

test bool matchInsideLexical() 
    = /E _ := [EF]"eefef";

test bool matchInsideSyntaxCyclicGrammar2()
    = /A _ := [ABs]"bbaab";

test bool matchInsideSyntax()
    = /A _ := [AB]"bbaab";
    
test bool matchInsideSyntax2()
    = /A2 _ := [AB2]"AABBAA";

value main(list[value] args) = ["<x>" | F x <- ((Fs) `ffffff`).fs] ;
 
test bool optionalNotPresentIsFalse() = !((A)`a` <- ([OptTestGrammar] "b").a);
test bool optionalPresentIsTrue() = (A)`a` <- ([OptTestGrammar] "ab").a;

// Calls with concrete parameters

int cntAs0(A* as) = size([a | A a <- as ]);

int cntAs1(A+ as) = cntAs0(as);

test bool callAs1() = cntAs0(([As0] "").as0) == 0;
test bool callAs2() = cntAs0(([As0] "a").as0) == 1;
test bool callAs3() = cntAs0(([As0] "a..a").as0) == 2;

test bool callAs4() = cntAs1(([As1] "a").as1) == 1;
test bool callAs5() = cntAs1(([As1] "a..a").as1) == 2;

int cntCs0({C ","}* cs) = size([c | C c <- cs ]);

int cntCs1({C ","}* cs) = cntCs0(cs);

test bool callCs1() = cntCs0(([Cs0] "").cs0) == 0;
test bool callCs2() = cntCs0(([Cs0] "c").cs0) == 1;
test bool callCs3() = cntCs0(([Cs0] "c..,..c").cs0) == 2;

test bool callCs4() = cntCs1(([Cs1] "c").cs1) == 1;
test bool callCs5() = cntCs1(([Cs1] "c..,..c").cs1) == 2;

int cntAroundCs0((AroundCs0) `OPENCs0<{C ","}* cs0>CLOSE`) = cntCs0(cs0);

test bool around1() = cntAroundCs0([AroundCs0] "OPENCs0CLOSE") == 0;

test bool around2() = cntAroundCs0([AroundCs0] "OPENCs0.c.CLOSE") == 1;

test bool around3() = cntAroundCs0([AroundCs0] "OPENCs0.c.,.c.CLOSE") == 2;

int cntAroundCs1((AroundCs1) `OPENCs1<{C ","}+ cs1>CLOSE`) = cntCs0(cs1);

test bool around4() = cntAroundCs1([AroundCs1] "OPENCs1.c.CLOSE") == 1;

test bool around5() = cntAroundCs1([AroundCs1] "OPENCs1.c.,.c.CLOSE") == 2;

// test for issue #834
int cntTrees(list[A] trees) = ( 0 | it + 1 | A a <- trees);
test bool callNoTree() = cntTrees([]) == 0;
test bool callOneTree() = cntTrees([[A]"a"]) == 1;
test bool callTwoTrees() = cntTrees([[A]"a",[A]"a"]) == 2;
test bool callTreeTrees() = cntTrees([[A]"a",[A]"a",[A]"a"]) == 3;

// Descendant in parameterized concrete sort

test bool descentInParameterizedSort1() = /A a := (M[A]) `[[a]]`;
test bool descentInParameterizedSort2() = /A a !:= (M[B]) `[[b]]`;

test bool descentInParameterizedSort3() = /B b := (M[B]) `[[b]]`;
test bool descentInParameterizedSort4() = /B b !:= (M[A]) `[[a]]`;

test bool descentInParameterizedSort5() = /A a := (N[A,B]) `\<\<a,b\>\>`;
test bool descentInParameterizedSort6() = /B b := (N[A,B]) `\<\<a,b\>\>`;
