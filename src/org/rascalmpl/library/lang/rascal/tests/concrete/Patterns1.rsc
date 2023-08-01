module lang::rascal::tests::concrete::Patterns1

import ParseTree;

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

syntax DE = D d E e;

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

test bool concreteMatchA1() = (A) `a` := [A] "a";
test bool concreteMatchA2() = (A) `<A _>` := [A] "a";

test bool concreteMatchDE1() = (DE) `de` := [DE] "de";
test bool concreteMatchDE2() = (DE) `d.e` := [DE] "de";
test bool concreteMatchDE3() = (DE) `d.e` := [DE] "d.e";
test bool concreteMatchDE4() = (DE) `de` := [DE] "d.e";
test bool concreteMatchDE5() = (DE) `<D d>e` := [DE] "d.e";

test bool concreteMatchAs101() = (As1) `a` := [As1] "a";
test bool concreteMatchAs102() = (As1) `aa` := [As1] "aa";
test bool concreteMatchAs103() = (As1) `a.a` := [As1] "aa";
test bool concreteMatchAs104() = (As1) `a.a` := [As1] "a.a";
test bool concreteMatchAs105() = (As1) `aa` := [As1] "a.a";
test bool concreteMatchAs106() = (As1) `<A a1><A a2>` := [As1] "a.a";

test bool concreteMatchAs107() = (As1) `<A+ as>` := [As1] "a" && "<as>" == "a";
test bool concreteMatchAs108() = (As1) `<A+ as>` := [As1] "aa" && "<as>" == "aa";
test bool concreteMatchAs109() = (As1) `<A+ as>` := [As1] "aaa" && "<as>" == "aaa";
test bool concreteMatchAs110() = (As1) `a<A+ as>` := [As1] "aa" && "<as>" == "a";
test bool concreteMatchAs111() = (As1) `a<A+ as>` := [As1] "a.a" && "<as>" == "a";
test bool concreteMatchAs112() = (As1) `aa<A+ as>` := [As1] "aaa" && "<as>" == "a";
test bool concreteMatchAs113() = (As1) `aa<A+ as>` := [As1] "a.a.a" && "<as>" == "a";

test bool concreteMatchAs114() = (As1) `<A+ as>a` := [As1] "aa" && "<as>" == "a";
test bool concreteMatchAs115() = (As1) `<A+ as>a` := [As1] "a.a" && "<as>" == "a";
test bool concreteMatchAs116() = (As1) `<A+ as>aa` := [As1] "aaa" && "<as>" == "a";
test bool concreteMatchAs117() = (As1) `<A+ as>aa` := [As1] "a.a.a" && "<as>" == "a";

test bool concreteMatchAs118() = (As1) `a<A+ as>a` := [As1] "aaa" && "<as>" == "a";
test bool concreteMatchAs119() = (As1) `a<A+ as>a` := [As1] "a.a.a" && "<as>" == "a";
test bool concreteMatchAs120() = (As1) `a<A+ as>a` := [As1] "aaaa" && "<as>" == "aa";
test bool concreteMatchAs121() = (As1) `a<A+ as>a` := [As1] "a.aa.a" && "<as>" == "aa";

test bool concreteMatchAs122() = (As1) `<A+ as1>a<A+ _>a` := [As1] "aaaa" && "<as1>" == "a" && "<as1>" == "a" ;
test bool concreteMatchAs123() = (As1) `<A+ as1>a<A+ as2>a` := [As1] "aaaaa" && "<as1>" == "a" && "<as2>" == "aa" ;

test bool concreteMatchBs01() = (Bs) `<B* bs>` := [Bs] "" && "<bs>" == "";  		
test bool concreteMatchBs02() = (Bs) `<B* bs>` := [Bs] "b" && "<bs>" == "b";
test bool concreteMatchBs03() = (Bs) `<B* bs>` := [Bs] "bb" && "<bs>" == "bb";

test bool concreteMatchBs04() = (Bs) `b<B* bs>` := [Bs] "b" && "<bs>" == ""; 		
test bool concreteMatchBs05() = (Bs) `b<B* bs>` := [Bs] "bb" && "<bs>" == "b";

test bool concreteMatchBs06() = (Bs) `<B* bs>b` := [Bs] "bbbb" && "<bs>" == "bbb";
test bool concreteMatchBs07() = (Bs) `<B* bs>bb` := [Bs] "bbbb" && "<bs>" == "bb";
test bool concreteMatchBs08() = (Bs) `<B* bs>bbb` := [Bs] "bbbb" && "<bs>" == "b";
test bool concreteMatchBs09() = (Bs) `<B* bs>bbbb` := [Bs] "bbbb" && "<bs>" == "";	

test bool concreteMatchBs10() = (Bs) `<B* bs1><B* _>` := [Bs] "" && "<bs1>" == "" && "<bs1>" == "";
test bool concreteMatchBs11() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbb" && "<bs1>" == "" && "<bs2>" == "b";
test bool concreteMatchBs12() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbbb" && "<bs1>" == "" && "<bs2>" == "bb";
test bool concreteMatchBs13() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbb" && "<bs1>" == "" && "<bs2>" == "";

test bool concreteMatchCs101() = (Cs1) `<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c,c";
test bool concreteMatchCs102() = (Cs1) `c,<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c";
test bool concreteMatchCs103() = (Cs1) `c,c,<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";

test bool concreteMatchCs104() = (Cs1) `<{C ","}+ cs>,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c";
test bool concreteMatchCs105() = (Cs1) `<{C ","}+ cs>,c,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";
test bool concreteMatchCs106() = (Cs1) `<{C ","}+ cs>,c,c,c` := [Cs1] "c,c,c,c" && "<cs>" == "c";
test bool concreteMatchCs107() = (Cs1) `<{C ","}+ _>,c,c,c,c` !:= [Cs1] "c,c,c,c";

test bool concreteMatchCs108() = (Cs1) `c,<{C ","}+ cs>,c` := [Cs1] "c,c,c" && "<cs>" == "c";
test bool concreteMatchCs109() = (Cs1) `c,<{C ","}+ cs>,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";

test bool concreteMatchCs110() = (Cs1) `<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs1] "c,c" && "<cs1>" == "c" && "<cs2>" == "c";
test bool concreteMatchCs111() = (Cs1) `c,<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs1] "c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";

test bool concreteMatchCs112() = (Cs1) `<{C ","}+ cs1>,c,<{C ","}+ cs2>` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";
test bool concreteMatchCs113() = (Cs1) `<{C ","}+ cs1>,<{C ","}+ cs2>,c` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";


test bool concreteMatchCs114() = (Cs1) `c,<{C ","}+ cs1>,c,<{C ","}+ cs2>,c` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";

test bool concreteMatchDs01() = (Ds) `<{D ","}* ds>` := [Ds] "" && "<ds>" == "";
test bool concreteMatchDs02() = (Ds) `<{D ","}* ds>` := [Ds] "d,d,d,d" && "<ds>" == "d,d,d,d";
test bool concreteMatchDs03() = (Ds) `<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d,d";
test bool concreteMatchDs04() = (Ds) `<{D ","}* ds>,d,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
test bool concreteMatchDs05() = (Ds) `d,<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
test bool concreteMatchDs06() = (Ds) `d,d,<{D ","}* ds>` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
test bool concreteMatchDs07() = (Ds) `d,d,<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d";

test bool concreteMatchDs08() = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "" && "<ds1>" == "" && "<ds2>" == "";
test bool concreteMatchDs09() = (Ds) `d,<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
test bool concreteMatchDs10() = (Ds) `<{D ","}* ds1>,d,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
test bool concreteMatchDs11() = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>,d` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
test bool concreteMatchDs12() = (Ds) `<{D ","}* ds1>,d,d,<{D ","}* ds2>,d` := [Ds] "d,d,d,d,d" && "<ds1>" == "" && "<ds2>" == "d,d";
test bool concreteMatchDs13() = (Ds) `<{D ","}* ds1>,d,d,d,<{D ","}* ds2>` := [Ds] "d,d,d,d,d" && "<ds1>" == "" && "<ds2>" == "d,d";

test bool concreteListEnum1() = ["<x>" | B x <- ((Bs) ``).bs0] == [];
test bool concreteListEnum2() = ["<x>" | B x <- ((Bs) `b`).bs0] == ["b"];
test bool concreteListEnum3() = ["<x>" | B x <- ((Bs) `bbbbb`).bs0] == ["b", "b", "b", "b", "b"];
test bool concreteListEnum4() = ["<x>" | D x <- ((Ds) ``).ds] == [];
test bool concreteListEnum5() = ["<x>" | D x <- ((Ds) `d`).ds] == ["d"];
test bool concreteListEnum6() = ["<x>" | D x <- ((Ds) `d,d,d,d,d`).ds] == ["d", "d", "d", "d", "d"];

test bool lexicalListEnum1() = ["<x>" | E x <- ((Es) `e,e,e,e,e,e,e`).es] == ["e", "e", "e", "e", "e", "e", "e"];
test bool lexicalListEnum2() = ["<x>" | F x <- ((Fs) `ffffff`).fs] == ["f", "f", "f", "f", "f", "f"];

test bool lexicalSequenceMatch1() = (Mies) `ac` := (Mies) `ac`;
test bool lexicalSequenceMatch2() = (Mies) `ac` := [Mies] "ac";
test bool lexicalSequenceMatch3() = (Mies) `ac` !:= (Mies) `ad`;
test bool lexicalSequenceMatch4() = (Mies) `ac` !:= [Mies] "ad";


test bool syntaxSequenceMatch1() = (Noot) `ac` := (Noot) `ac`;
test bool syntaxSequenceMatch2() = (Noot) `ac` := [Noot] "ac";
test bool syntaxSequenceMatch3() = (Noot) `ac` !:= (Noot) `ad`;
test bool syntaxSequenceMatch4() = (Noot) `ac` !:= [Noot] "ad";

test bool lexicalTokenMatch1() = (MyName) `location` := (MyName) `location`;
test bool lexicalTokenMatch2() = (MyName) `location` := [MyName] "location";


test bool concreteMatchVisit1() {
  result = false;
  visit ([A]"a") {
    case (A)`<A _>`: result = true;
  }
  return result;
}
test bool concreteMatchVisit2() {
  result = 0;
  visit ([As1]"aaa") {
    case (A)`<A _>`: result += 1;
  }
  return result == 3;
}

@ignoreInterpreter{While this should work, the fix is too large, and there are workarounds}
test bool concreteMatchVisitLayout() {
  result = false;
  visit ([start[XorY]] ".x.") {
    case (Layout)`.`: result = true;
  }
  return result;
}

test bool concreteReplaceInLayout(){
  result = visit([start[XorY]] ".x;") {
    case (Layout)`.` => (Layout)`;`
  } 
  return result := [start[XorY]] ";x;";
}

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
	//throw "fail due to missing match";
}

test bool concreteSwitch8(){
	switch([As1] "a.a.a"){
		case (As1) `<A+ as>`: 		
				return "<as>" == "a.a.a"; 
		default: 		 
				return false;
	}
	//throw "fail due to missing match";
}

test bool concreteSwitch9(){
	switch([Bs] "bbb"){
		case (Bs) `<B* bs>`: 		
				return "<bs>" == "bbb"; 
		default: 		 
				return false;
	}
	//throw "fail due to missing match";
}

test bool concreteSwitch10(){
	switch([Bs] "b..b..b"){
		case (Bs) `<B* bs>`: 		
				return "<bs>" == "b..b..b"; 
		default: 		 
				return false;
	}
	//throw "fail due to missing match";
}

test bool concreteSwitch11(){
	switch([Cs1] "c,c,c,c"){
		case (Cs1) `<{C ","}+ cs>`: 		
				return "<cs>" == "c,c,c,c"; 
		default: 		 
				return false;
	}
	//throw "fail due to missing match";
}

test bool concreteSwitch12(){
	switch([Cs1] "c.,.c.,.c.,.c"){
		case (Cs1) `<{C ","}+ cs>`: 		
				return "<cs>" == "c.,.c.,.c.,.c"; 
		default: 		 
				return false;
	}
	//throw "fail due to missing match";
}

test bool concreteSwitch13(){
	switch([Ds] "d,d,d,d"){
		case (Ds) `<{D","}* ds>`: 		
				return "<ds>" == "d,d,d,d"; 
		default: 		 
				return false;
	}
	//throw "fail due to missing match";
}

test bool concreteSwitch14(){
	switch([Ds] "d.,.d.,.d.,.d"){
		case (Ds) `<{D","}* ds>`: 		
				return "<ds>" == "d.,.d.,.d.,.d"; 
		default: 		 
				return false;
	}
	//throw "fail due to missing match";
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
int cntTrees(list[A] trees) = ( 0 | it + 1 | A _ <- trees);
test bool callNoTree() = cntTrees([]) == 0;
test bool callOneTree() = cntTrees([[A]"a"]) == 1;
test bool callTwoTrees() = cntTrees([[A]"a",[A]"a"]) == 2;
test bool callTreeTrees() = cntTrees([[A]"a",[A]"a",[A]"a"]) == 3;

void f([1,*int L,3]) {}

test bool listArgAndEmptyBody(){
    f([1,2,2,3]);
    return true;
}

// Descendant in parameterized concrete sort

test bool descentInParameterizedSort1() = /A _ := (M[A]) `[[a]]`;
test bool descentInParameterizedSort2() = /A _ !:= (M[B]) `[[b]]`;

test bool descentInParameterizedSort3() = /B _ := (M[B]) `[[b]]`;
test bool descentInParameterizedSort4() = /B _ !:= (M[A]) `[[a]]`;

test bool descentInParameterizedSort5() = /A _ := (N[A,B]) `\<\<a,b\>\>`;
test bool descentInParameterizedSort6() = /B _ := (N[A,B]) `\<\<a,b\>\>`;
