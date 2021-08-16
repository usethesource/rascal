module lang::rascalcore::compile::Examples::Tst1

layout Whitespace = [\ ]* !>> [\ ];
lexical IntegerLiteral = [0-9]+; 
lexical Identifier = [a-z]+;

syntax Exp 
  = IntegerLiteral  
//  | Identifier        
  //| bracket "(" Exp ")"     
  //> left Exp "*" Exp        
  //> left Exp "+" Exp  
  //| Exp "==" Exp      
  ;

syntax Stat 
   = Identifier ":=" Exp
//   | "if" Exp "then" {Stat ";"}* "else" {Stat ";"}* "fi"
   ;

value main()//test bool concreteMatch207() 
    = (Exp) `1` := [Exp] "1";
    //= (Stat) `a:=1` := [Stat] "a:=1";// && "<x>" == "a" && "<e>" == "1";
//import ParseTree;

//syntax OptTestGrammar = A? a B b;

//layout L = [\ ]* !>> [\ ];
//syntax A = "a";
////syntax As0 = A* as0;
//syntax As1 = A+ as1;
//
//syntax B = A;
//
//syntax C = "c";
//syntax AC = A C;
//
//syntax D = "d";
//syntax Ds1 = {D ","}+;


//lexical E = [e];
//
//syntax B = "b";
//syntax Bs = B* bs0;

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
//lexical Mies = ([ab] [cd]);
//syntax Noot  = (("a"|"b") ("c"|"d"));
//
//value main(){//test bool concreteMatchVisit1() {
//  result = false;
//  visit ([A]"a") {
//    case (A)`<A _>`: result = true;
//  }
//  return result;
//}

//value main() //test bool concreteMatch02() 
// = (Ds) `<{D ","}* ds1>,<{D ","}* ds2>,d` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";  


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