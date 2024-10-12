@bootstrapParser
module lang::rascalcore::check::tests::AllStaticIssues

import lang::rascalcore::check::tests::StaticTestingUtils;

//test bool comma() = checkOK("or(true);", 
//					initialDecls = ["test bool or(bool b) { if (true || b == true, b || true == true, false || false == false) return true; else return false; }"]); 

//test bool or(bool b) { if (true || b == true, b || true == true, false || false == false) return true; else return false; } 

// https://github.com/cwi-swat/rascal/issues/416

test bool Issue416(){
	return checkOK("true;", initialDecls=["data D = d(int i) | d();", "D d(int i) { if (i % 2 == 0) fail d; else return d();}"]);
} 

// https://github.com/cwi-swat/rascal/issues/430

test bool Issue430() =
	checkOK("true;", initialDecls=["data T1 = \\int() | \\void() | string(str s);",
								   "data T2 = \\int() | \\void() | string(str s);",
								   "bool fT1(T1::\\int()) = true;",
								   "bool fT2(T2::\\int()) = true;"]);


// https://github.com/cwi-swat/rascal/issues/431

// Included in FunctionCompositionTests

// https://github.com/cwi-swat/rascal/issues/432

test bool Issue432() =
	unexpectedType("set[value] s := {} && s\<0\> == {};");
	
// https://github.com/cwi-swat/rascal/issues/435
@ignore{The forward references to `called` are not handled properly}
test bool Issue435() {
	makeModule("MMM", "bool sideEffect1() {
             			void One() { called = called + 1; return; }
             			int called = 0;  
             			One(); 
             			One(); 
            			One(); 
             			return called == 3;
             		}");	
	return checkOK("true;", importedModules=["MMM"]);
}
	
// https://github.com/cwi-swat/rascal/issues/436	
// Is already included in the standard test suite
	
// https://github.com/cwi-swat/rascal/issues/442

test bool Issue442() =
	checkOK("true;", initialDecls=["syntax A = \"a\";",
								   "value my_main() = [A] \"a\" := [A] \"a\";"]);
// https://github.com/cwi-swat/rascal/issues/451
// Is already included in the standard test suite

// https://github.com/cwi-swat/rascal/issues/456

test bool Issue456() =
	checkOK("true;", initialDecls = ["data POINT1 = point1(int x, int y, int z = 3, list[str] colors = []);",
									  "value my_main() =  point1(1,2);"]);

// https://github.com/cwi-swat/rascal/issues/457

test bool Issue457() =
	checkOK("true;", initialDecls = ["data Exp1[&T] = tval(&T tval) | tval2(&T tval1, &T tval2) | ival(int x);", 
									  "value my_main() {m = tval2(\"abc\", \"def\"); str s2 = m.tval2; return s2 == \"def\";}"]);   

	 

// https://github.com/cwi-swat/rascal/issues/458

test bool Issue458a() =
	checkOK("\"f1\"(1, M=10) := \"f1\"(1, M=10);");

data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);

test bool Issue458b() =                                              // TODO
	checkOK("f1(1, M=X) := f1(1, B=false, M=20) && X == 20;", initialDecls=["data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);"]);
		
test bool Issue458c() =                                              // TODO
	checkOK("\"f1\"(1, M=X) := \"f1\"(1, B=false, M=20) && X == 20;", initialDecls=["data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);"]);


// https://github.com/cwi-swat/rascal/issues/465

test bool Issue465a(){			                                     // TODO: not sure									
	makeModule("MMM", "lexical IntegerLiteral = [0-9]+;           
					 start syntax Exp = con: IntegerLiteral;");
	return checkOK("true;", importedModules=["MMM"], initialDecls=["data Exp = con(int n);"]);
}

test bool Issue465b(){			                                     								
	makeModule("MMM", "lexical IntegerLiteral = [0-9]+;           
					 start syntax Exp = con: IntegerLiteral;");
	return checkOK("c = con(5);", importedModules=["MMM"], initialDecls=["data Exp = con(int n);"]);
}

test bool Issue465c(){			                                     								
	makeModule("MMM", "lexical IntegerLiteral = [0-9]+;           
					 start syntax Exp = con: IntegerLiteral;");
	return checkOK("Exp c = con(5);", importedModules=["MMM"], initialDecls=["data Exp = con(int n);"]);
}

test bool Issue465d(){			                                     								
	makeModule("MMM", "lexical IntegerLiteral = [0-9]+;           
					 start syntax Exp = con: IntegerLiteral;");
	return checkOK("MMM::Exp c = [MMM::Exp] \"3\";", importedModules=["MMM"], initialDecls=["data Exp = con(int n);"]);
}

// https://github.com/cwi-swat/rascal/issues/471

test bool Issue471a() =
	checkOK("([A1, f([A1, b(), DATA X8])] := [a(), f([a(),b(),c()])]) && (A1 == a());", 
					initialDecls = ["data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);"]);

test bool Issue471b() =
	checkOK("([f([A1, b(), DATA X8]), A1] := [f([a(),b(),c()]), a()]) && (A1 == a());", 
					initialDecls = ["data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);"]);


test bool Issue471c() =
	checkOK("([DATA A2, f([A2, b(), *DATA SX1]), *SX1] := [a(), f([a(),b(),c()]), c()]) && (A2 == a()) && (SX1 ==[c()]);", 
					initialDecls = ["data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);"]);

test bool Issue471d() =
	checkOK("([DATA A3, f([A3, b(), *DATA SX2]), *SX2] !:= [d(), f([a(),b(),c()]), a()]);", 
					initialDecls = ["data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);"]);

test bool Issue471e() =
	checkOK("([DATA A4, f([A4, b(), *DATA SX3]), *SX3] !:= [c(), f([a(),b(),c()]), d()]);", 
					initialDecls = ["data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);"]);

test bool Issue471f() =
	checkOK("f(_) := f(1);", 
					initialDecls = ["data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);"]);

test bool Issue471g() =
	checkOK("f(_,_):= f(1,2);", 
					initialDecls = ["data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);"]);

test bool Issue471h() =
	checkOK("f(_,_,_):= f(1,2.5,true);", 
					initialDecls = ["data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);"]);

 test bool Issue471i() =
	checkOK("(f(n5) := f(1)) && (n5 == 1);", 
					initialDecls = ["data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);"]);
 
 test bool Issue471j() =
	checkOK("({e(X3), g(X3), h(X3)} := {e(3), h(3), g(3)}) && (X3 == 3);", 
					initialDecls = ["data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
"]);


// https://github.com/cwi-swat/rascal/issues/472

test bool Issue472a() =                                                      // TODO: EmptyList()
	checkOK("[1, /f(/g(2), _), 3] := [1, f(g(1),f(g(2),g(3))), 3];", 
					initialDecls = ["data F = f(F left, F right) | g(int N);"]);
 
test bool Issue472b() =
	checkOK("[1, F outer: /f(/F inner: g(2), _), 3] := [1, f(g(1),f(g(2),g(3))), 3] && outer == f(g(1),f(g(2),g(3))) && inner == g(2);", 
					initialDecls = ["data F = f(F left, F right) | g(int N);"]);

// https://github.com/cwi-swat/rascal/issues/473

test bool Issue473() =
 	unexpectedType("[ \<s,r,L\> | list[int] L:[*str s, *str r] \<- [ [1,2], [\"3\",\"4\"] ]];");


// https://github.com/cwi-swat/rascal/issues/477

// A discussion only

// https://github.com/cwi-swat/rascal/issues/478

test bool Issue478() =
	checkOK("true;", 
					initialDecls = ["data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);",
 									 "public value my_main() = f1(1, M=10)  := f1(1);"]); 

// https://github.com/cwi-swat/rascal/issues/480

test bool Issue480(){
	makeModule("MMM", "data Figure (real shrink = 1.0, str fillColor = \"white\", str lineColor = \"black\")  =  emptyFigure() 
  					| ellipse(Figure inner = emptyFigure()) 
  					| box(Figure inner = emptyFigure());

 				value my_main() = (!(ellipse(inner=emptyFigure(fillColor=\"red\")).fillColor == \"white\"));");
	return checkOK("true;", importedModules=["MMM"]);
} 
	
// https://github.com/cwi-swat/rascal/issues/483
// https://github.com/cwi-swat/rascal/issues/504

test bool Issue504() =
	redeclaredVariable("true;", initialDecls = ["alias INT = int;", "alias INT = int;"]); //DISCUSS, was: checkOK
	
// https://github.com/cwi-swat/rascal/issues/547

test bool Issue547(){												
	makeModule("M1", "import M2;");		 
	makeModule("M2", "import Type;
					  public data MuExp = muCallJava( str name, Symbol parameterTypes);");
	return checkOK("true;", importedModules=["M1", "M2"]);
}
// https://github.com/cwi-swat/rascal/issues/549

// An error in the failing example itself, does not lead to a test

// https://github.com/cwi-swat/rascal/issues/550
// Removed expensive

// https://github.com/cwi-swat/rascal/issues/563

test bool Issue563() = uninitialized("int x; x + 5;");

test bool Issue886() = unexpectedType("[\<[\<19,0,*_\>],false,_\>] := [\<[\<19,0,1\>], true, 1\>];");

// https://github.com/usethesource/rascal/issues/1353
test bool Issue1353() {
   makeModule("MC", "syntax A 
                    '  = \"a\"
                    '  | left two: A lhs A rhs; 
                    '
                    '  A hello() {
                    '    A given_a = (A) `a`;
                    '     return (A) `\<A given_a\> \<A given_a\>`;
                    '  }");
   return checkOK("hello();", importedModules=["MC"]);                 
}

// https://github.com/usethesource/rascal/issues/1800
// Removed expensive test