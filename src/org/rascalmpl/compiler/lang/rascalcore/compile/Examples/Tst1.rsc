module lang::rascalcore::compile::Examples::Tst1


//import ParseTree;

//syntax OptTestGrammar = A? a B b;

syntax A = "a";
//syntax As0 = A* as0;
syntax As1 = A+ as1;
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
//
//syntax D = "d";
//syntax Ds = {D ","}* ds;
//
//lexical E = "e";
//lexical Es = {E ","}* es;
//
//lexical F = "f";
//lexical Fs = F* fs;
//
//lexical EF = (E | F)*;
//lexical EFs = (Es | Fs)*;
//
//start syntax XorY = x : "x" | y : "y";
//
//lexical Layout = [.;];
//layout L = Layout* !>> [.;];
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

//test bool concreteMatch01() = (A) `<A _>` := [A] "a";
//test bool concreteMatch01a() = (A) `a` := [A] "a";

value main() //test bool concreteMatch02() 
    = (As1) `aa` := [As1] "aa";
    //= (As1) `<A+ as>` := [As1] "a" ? as : false;//&& "<as>" == "a";

//test bool concreteMatch03() = (As1) `<A+ as>` := [As1] "aa" && "<as>" == "aa";
//test bool concreteMatch04() = (As1) `<A+ as>` := [As1] "aaa" && "<as>" == "aaa";
//test bool concreteMatch05() = (As1) `a<A+ as>` := [As1] "aa" && "<as>" == "a";
//test bool concreteMatch06() = (As1) `aa<A+ as>` := [As1] "aaa" && "<as>" == "a";
//
//test bool concreteMatch07() = (As1) `<A+ as>a` := [As1] "aa" && "<as>" == "a";
//test bool concreteMatch08() = (As1) `<A+ as>aa` := [As1] "aaa" && "<as>" == "a";
//
//test bool concreteMatch09() = (As1) `a<A+ as>a` := [As1] "aaa" && "<as>" == "a";
//test bool concreteMatch10() = (As1) `a<A+ as>a` := [As1] "aaaa" && "<as>" == "aa";
//
//test bool concreteMatch11() = (As1) `<A+ as1>a<A+ _>a` := [As1] "aaaa" && "<as1>" == "a" && "<as1>" == "a" ;
//test bool concreteMatch12() = (As1) `<A+ as1>a<A+ as2>a` := [As1] "aaaaa" && "<as1>" == "a" && "<as2>" == "aa" ;
//
//test bool concreteMatch13() = (Bs) `<B* bs>` := [Bs] "" && "<bs>" == "";        
//test bool concreteMatch14() = (Bs) `<B* bs>` := [Bs] "b" && "<bs>" == "b";
//test bool concreteMatch15() = (Bs) `<B* bs>` := [Bs] "bb" && "<bs>" == "bb";
//
//test bool concreteMatch16() = (Bs) `b<B* bs>` := [Bs] "b" && "<bs>" == "";      
//test bool concreteMatch17() = (Bs) `b<B* bs>` := [Bs] "bb" && "<bs>" == "b";
//
//test bool concreteMatch18() = (Bs) `<B* bs>b` := [Bs] "bbbb" && "<bs>" == "bbb";
//test bool concreteMatch19() = (Bs) `<B* bs>bb` := [Bs] "bbbb" && "<bs>" == "bb";
//test bool concreteMatch20() = (Bs) `<B* bs>bbb` := [Bs] "bbbb" && "<bs>" == "b";
//test bool concreteMatch21() = (Bs) `<B* bs>bbbb` := [Bs] "bbbb" && "<bs>" == "";    
//
//test bool concreteMatch22() = (Bs) `<B* bs1><B* _>` := [Bs] "" && "<bs1>" == "" && "<bs1>" == "";
//test bool concreteMatch23() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbb" && "<bs1>" == "" && "<bs2>" == "b";
//test bool concreteMatch24() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbbb" && "<bs1>" == "" && "<bs2>" == "bb";
//test bool concreteMatch25() = (Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbb" && "<bs1>" == "" && "<bs2>" == "";
//
//
//test bool concreteMatch26() = (Cs1) `<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c,c";
//test bool concreteMatch27() = (Cs1) `c,<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c";
//test bool concreteMatch28() = (Cs1) `c,c,<{C ","}+ cs>` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";
//
//test bool concreteMatch29() = (Cs1) `<{C ","}+ cs>,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c,c";
//test bool concreteMatch30() = (Cs1) `<{C ","}+ cs>,c,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";
//test bool concreteMatch31() = (Cs1) `<{C ","}+ cs>,c,c,c` := [Cs1] "c,c,c,c" && "<cs>" == "c";
//test bool concreteMatch32() = (Cs1) `<{C ","}+ _>,c,c,c,c` !:= [Cs1] "c,c,c,c";
//
//test bool concreteMatch33() = (Cs1) `c,<{C ","}+ cs>,c` := [Cs1] "c,c,c" && "<cs>" == "c";
//test bool concreteMatch34() = (Cs1) `c,<{C ","}+ cs>,c` := [Cs1] "c,c,c,c" && "<cs>" == "c,c";
//
//test bool concreteMatch35() = (Cs1) `<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs1] "c,c" && "<cs1>" == "c" && "<cs2>" == "c";
//test bool concreteMatch36() = (Cs1) `c,<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs1] "c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";
//
//test bool concreteMatch37() = (Cs1) `<{C ","}+ cs1>,c,<{C ","}+ cs2>` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";
//test bool concreteMatch38() = (Cs1) `<{C ","}+ cs1>,<{C ","}+ cs2>,c` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c,c,c";
//
//
//test bool concreteMatch39() = (Cs1) `c,<{C ","}+ cs1>,c,<{C ","}+ cs2>,c` := [Cs1] "c,c,c,c,c" && "<cs1>" == "c" && "<cs2>" == "c";
//
//test bool concreteMatch40() = (Ds) `<{D ","}* ds>` := [Ds] "" && "<ds>" == "";
//test bool concreteMatch41() = (Ds) `<{D ","}* ds>` := [Ds] "d,d,d,d" && "<ds>" == "d,d,d,d";
//test bool concreteMatch42() = (Ds) `<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d,d";
//test bool concreteMatch43() = (Ds) `<{D ","}* ds>,d,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
//test bool concreteMatch44() = (Ds) `d,<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
//test bool concreteMatch45() = (Ds) `d,d,<{D ","}* ds>` := [Ds] "d,d,d,d" && "<ds>" == "d,d";
//test bool concreteMatch46() = (Ds) `d,d,<{D ","}* ds>,d` := [Ds] "d,d,d,d" && "<ds>" == "d";
//
//test bool concreteMatch47() = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "" && "<ds1>" == "" && "<ds2>" == "";
//test bool concreteMatch48() = (Ds) `d,<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
//test bool concreteMatch49() = (Ds) `<{D ","}* ds1>,d,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
//test bool concreteMatch50() = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>,d` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
//test bool concreteMatch51() = (Ds) `<{D ","}* ds1>,d,d,<{D ","}* ds2>,d` := [Ds] "d,d,d,d,d" && "<ds1>" == "" && "<ds2>" == "d,d";
//test bool concreteMatch52() = (Ds) `<{D ","}* ds1>,d,d,d,<{D ","}* ds2>` := [Ds] "d,d,d,d,d" && "<ds1>" == "" && "<ds2>" == "d,d";
//
//test bool concreteListEnum1() = ["<x>" | B x <- ((Bs) ``).bs0] == [];
//test bool concreteListEnum2() = ["<x>" | B x <- ((Bs) `b`).bs0] == ["b"];
//test bool concreteListEnum3() = ["<x>" | B x <- ((Bs) `bbbbb`).bs0] == ["b", "b", "b", "b", "b"];
//test bool concreteListEnum4() = ["<x>" | D x <- ((Ds) ``).ds] == [];
//test bool concreteListEnum5() = ["<x>" | D x <- ((Ds) `d`).ds] == ["d"];
//test bool concreteListEnum6() = ["<x>" | D x <- ((Ds) `d,d,d,d,d`).ds] == ["d", "d", "d", "d", "d"];
//
//test bool lexicalListEnum1() = ["<x>" | E x <- ((Es) `e,e,e,e,e,e,e`).es] == ["e", "e", "e", "e", "e", "e", "e"];
//test bool lexicalListEnum2() = ["<x>" | F x <- ((Fs) `ffffff`).fs] == ["f", "f", "f", "f", "f", "f"];
//
//test bool lexicalSequenceMatch() = (Mies) `ac` !:= (Mies) `ad`;
//test bool syntaxSequenceMatch() = (Noot) `ac` !:= (Noot) `ad`;
//test bool lexicalTokenMatch1() = (MyName) `location` := (MyName) `location`;
//test bool lexicalTokenMatch2() = (MyName) `location` := [MyName] "location";
//
//





//start syntax XorY = x : "x" | y : "y";
//
//value main() = x() := [XorY] "x";


//import lang::rascalcore::compile::Examples::Tst2;


//import IO;
//import ParseTree;
//import lang::rascalcore::compile::Examples::Tst2;

//syntax IdType = Id id ":" XType t;
//
//syntax XType 
//  = natural:"natural" 
//   ;
//syntax YType = "ynatural";
//     
//lexical Id  = [a-z][a-z0-9]* !>> [a-z0-9];
//
//layout Layout = WhitespaceAndComment* !>> [\ \t\n\r%];
//
//lexical WhitespaceAndComment 
//   = [\ \t\n\r]
//   ;
//
//value main() //test bool Pico1()  {
//
//    // = [Program] "begin declare x: natural; x := 10 end";
//    //= [Statement] "if x then a:=1;b:=2 else fi";
//    //= [Expression] "42 + 67";
//    //= [Type] "natural";
//    = [IdType] "x:natural";
//    //= #Program;





//data LIST = lst(list[int] elems);

//@ignoreInterpreter{Interpreter crashes on this test}
//value main(){ //test bool visit19() {
//    return [ visit(lst([1])) {
//                // list[str] <: list[value]; typeOf(subject) <: list[value] 
//                // '+' list[str] <: typeOf(subject)
//                case list[value] _: insert [ "666" ];
//                case list[int] l: { insert l + [ 666 ]; l = l + [ 777 ]; }
//                case int _: insert 999;
//             } ]
//             ;//== 
//             // [lst([999,666])];
//}

//value main(){ //test bool visit20() {
//    return 
//           [ visit(lst([1])) {
//           case list[value] z => [ "666" ]
//                case list[int] l => l + [ 666 ]
//                
//                
//                case int _ => 999
//             } ]
//           ;//==
//           //[ lst([999,666]) ];
//}
//



// &S(&U) curry(&S(&T, &U) f, &T t) = &S (&U u) { 
//      return f(t, u); 
// };
//
//int addition(int i, int j) = i + j;
//
//value main(){ //test bool selfApplyCurry() {
//   func = curry(curry, addition);
//
//    assert int(int)(int) _ := func;
//
//    func2 = func(1);
//
//    assert int(int) _ := func2;
//
//    assert func2(1) == 2;
//    
//    return true;
//}

//import Grammar;
//import ParseTree;
//
//anno int Symbol@id;
//
//Grammar G0 = grammar(
//  {sort("S")[
//      @id=2
//    ]},
//  (
//    sort("S")[
//      @id=3
//    ]:choice(
//      sort("S")[
//        @id=4
//      ],
//      {prod(
//          sort("S")[
//            @id=5
//          ],
//          [lit("0")[
//              @id=6
//            ]],
//          {})}),
//    lit("0")[
//      @id=7
//    ]:choice(
//      lit("0")[
//        @id=8
//      ],
//      {prod(
//          lit("0")[
//            @id=9
//          ],
//          [\char-class([range(48,48)])[
//              @id=10
//            ]],
//          {})})
//  ));
//
//test bool cntLit()    
//    {cnt = 0; visit(G0){ case lit(_): cnt += 1;}; return cnt == 4; }
//    
//value main() = G0;
    
//syntax A = "a";
//syntax As1 = A+ as1;
//
//value main() // test bool concreteMatch03() 
//    = (As1) `a` := [As1] "a";// ? as : false; //&& "<as>" == "aa";


//value main() = 2 == 3;



//import lang::rascal::tests::functionality::PatternSet2;

// Anastassija's type constraint examples

// Version 4; with overloaded constructor INTERSECT , and non-linear constraints (tset)
        
//public TYPESET INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, *TYPESET rest})), tset, *TYPESET rest1 }) {
//    return INTERSECT({ SUBTYPES(INTERSECT(rest)), tset, *rest1 });
//}
//        
//value main() //test bool testSimplifyA() = 
//   = INTERSECT({ SUBTYPES(INTERSECT({  })), SET("s1") });// ==
//   //INTERSECT({ SUBTYPES(INTERSECT({  })), SET("s1") });





//import ParseTree;
//layout Whitespace = [\ ]* !>> [\ ];
//lexical IntegerLiteral = [0-9]+; 
//lexical Identifier = [a-z]+;
//
//syntax Exp 
//  = IntegerLiteral  
//  | Identifier        
//  | bracket "(" Exp ")"     
//  > left Exp "*" Exp        
//  > left Exp "+" Exp  
//  | Exp "==" Exp      
//  ;
//
//syntax Stat 
//   = Identifier ":=" Exp
//   | "if" Exp "then" {Stat ";"}* "else" {Stat ";"}* "fi"
//   ;
//
//value main() // test bool concreteMatch207() 
//    = (Stat) `<Identifier x> := <Exp e>` := [Stat] "a:=      1" && "<x>" == "a" && "<e>" == "1";

//import ParseTree;
//start syntax XorY = x : "x" | y : "y";
//lexical Layout = [.;];
//layout L = Layout* !>> [.;];
//
//value main() = (XorY) `x`;
//
//test bool concreteSwitch1(){
//    switch([XorY] "x"){
//        case (XorY) `x`: return true;
//    }
//    return false;
//}

//test bool concreteSwitch2(){
//    switch([XorY] "x"){
//        case (XorY) `x`: return true;
//        case (XorY) `y`: return false;
//    }
//    return false;
//}
//
//test bool concreteSwitch3(){
//    switch([XorY] "y"){
//        case (XorY) `x`: return false;
//        case (XorY) `y`: return true;
//    }
//    return false;
//}
//
//test bool concreteSwitch4(){
//    switch([XorY] "y"){
//        case x():        throw "fail to due extra match";
//        case (XorY) `y`: return true;
//    }
//    throw "fail due to missing match";
//}
//
//test bool concreteSwitch5(){
//    switch([XorY] "y"){
//        case (XorY) `x`: throw "fail to due extra match"; 
//        case y():        return true;
//    }
//    throw "fail due to missing match";
//}

////FunctionComposition:
//int twice (int n) = 2 * n;
//int triple (int n) = 3 * n;
//test bool twiceTriple2(){
//    c = twice o triple;
//    return c(5) == twice(triple(5));
//}

//import lang::rascalcore::compile::Examples::Tst2;
//
//
//value main() = x().right;
//
//data D = d(tuple[int i, str s] t);
//
//value main()
//{   x = d(<3, "a">);
//    x.t.s = "b";
//    return x.t.s;
//   // return x;
//}

//value main() //test bool testLocationFieldUpdate4() 
//{ loc l = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
//  l.end.line = 14; 
//  return l;//.end.line;
//  //return l.end.line == 14;
//}


//import ParseTree;
//
//syntax A = "a";
//
////test bool concreteExpressionsHaveSourceLocations1() 
////  = (A) `a`.src?;
//  
//value main() //est bool concreteExpressionsHaveSourceLocations2() 
//  = (A) `a`.\loc;//.length == 1;


//import ParseTree;
//import lang::rascal::\syntax::Rascal;
//
////test bool isThisATuple() = (Expression)`\< <{Expression ","}+ _> \>` := parse(#Expression, "\<1\>");
//
//value main() = [Expression] "\<1\>";