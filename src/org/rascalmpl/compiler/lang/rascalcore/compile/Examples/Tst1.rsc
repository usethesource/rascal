module lang::rascalcore::compile::Examples::Tst1

bool main(){

    x = 2;
    for(1 := x){
        x = 1;
    }
    return true;
}

//data D = d1(int) | d1(str s);
//
//public bool intersect(set[D] u1, set[D] u2) {
//  if ({d1(_)} := u1 
//      && 
//      {d1(_)} := u2) {
//    return true;
//  }
//  return false;
//}

//public str unescape(str name) {
//  if (/\\<rest:.*>/ := name) return rest; 
//  return name;
//}


//data Symbol     // <2>
//     = \label(str name, Symbol symbol);
     
//import ParseTree;
////
////bool isManual(set[Attr] as) = (\tag("manual"()) in as);


//import List;
//layout Whitespace = [\ \t\n]*;
//start syntax E = "e";
//start syntax ES = {E ","}+ args;
//
////int cntES({E ","}+ es) = size([e | e <- es ]);
//
//value main() { //test bool cntES1() = 
//    return ((ES) `e`).args;
//}

//import ParseTree;
//value main(){
//    Symbol sym;
//    map[Symbol, Production] rules = ();
//    str input = "";
//    tree = parse(type(sym, rules), input, |todo:///|);
//    return tree;
//}

//
//import IO;
//import List;
//import Node;
//import Set;
//import String;

//import lang::rascal::\syntax::Rascal;
//
//value getAllNames(Pattern p) =
//    (Pattern) `<Type tp> <Name name>` := p;
//    
//test bool lexicalMatch3() = (QualifiedName) `a` := [QualifiedName] "a";
//
//test bool matchTP() = (Pattern) `<Type tp><Name name>` := [Pattern] "int x";

//value main() { switch(0) {case Tree tree:  return tree@\loc;} return false;}

//start syntax D = "d";
//start syntax DS = D+ ds;
//
//value main() = //test bool parseD1() = 
//    (D)`d` := parse(#D, "d");
//
//
//public &T<:Tree parse(type[&T<:Tree] begin, str input, bool allowAmbiguity=false, bool hasSideEffects=false, set[Tree(Tree)] filters={})
//  = parser(begin, allowAmbiguity=allowAmbiguity, hasSideEffects=hasSideEffects, filters=filters)(input, |unknown:///|);
//
//@javaClass{org.rascalmpl.library.Prelude}
//public java &T (value input, loc origin) parser(type[&T] grammar, bool allowAmbiguity=false, bool hasSideEffects=false, bool firstAmbiguity=false, set[Tree(Tree)] filters={}); 

//
//start syntax D = "d";
//start syntax DS = D+ ds;
//
//value main() = //test bool parseD1() = 
//    (D)`d` := parse(#D, "d");

// value main() { //test bool voidMaybeShouldShouldThrowException() {
//
//  &T testFunction(Maybe[&A] m) = m.val; 
//   // 
//   //try {
//   //   Maybe[value] m = nothing();
//   //   value x = testFunction(m);
//   //   return x != 123; // this comparison never happens
//   //}
//   //catch NoSuchField(_) :
//   //  return true;
//}
 
//@javaClass{org.rascalmpl.library.Prelude}
//public java bool isEmpty(list[&T] lst);
//
//@javaClass{org.rascalmpl.library.Prelude}
//public java &T elementAt(list[&T] lst, int index);
//
//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(list[&T] lst);







//layout Whitespace = [\ \t\n]*;
//syntax A = "a";
//syntax B = "b";
//
//syntax P[&T] = "{" &T ppar "}";
//syntax PA = P[A] papar;
//
//value main() //test bool PA9() 
//    = "<(P[A]) `{a}`>" == "<[P[A]] "{a}">";
   
//value main(){ //test bool returnOfAnInstantiatedGenericFunction() {
//    &S(&U) curry(&S(&T, &U) f, &T t) = &S (&U u) { 
//      return f(t, u); 
//    };
//
//    int add(int i, int j) = i + j;
//
//    // issue #1467 would not allow this assignment because the 
//    // returned closure was not instantiated properly from `&S (&U)` to `int(int)`
//    int (int) f = curry(add, 1); 
//
//    return f(1) == 2 && (f o f)(1) == 3;
//}


//value main()  
    // /!4 := [1,2,3,2];
    // /!4 := 3;
    // /!4 !:= 4; 
    // /!4 := 3; 
    // /int N := 1 && N == 1;
       ///!4 := [1,2,3,4];
       //({e(int _)} !:= {a()});
  
    
//data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
// 
//test bool not1() = !(1 == 2);
//test bool not2() = !!(1 == 1);
//test bool not3() = !(1 != 1);
//
//test bool list0() = ([int N, 2, N] := [1,2,1]) && (N == 1);
//test bool list1() = !([] !:= []);
//test bool list2() = !([1, *_, 5] := [1,2,4]);
//test bool list3() = !([1, *_, 5] !:= [1,2,5]);
//
//public bool isDuo3(list[int] L)
//{
//    return [*int L1, *L1] := L;
//}
//
//test bool list4() = isDuo3([1]) == false;
//
//
//test bool set1() = !({} !:= {});
//test bool set2() = !({1, *_, 5} := {1,2,4});
//test bool set3() = !({1, *_, 5} !:= {1,2,5});
//test bool set4() = {*str _, int _} !:= {"a", true};
//test bool set5() = ({e(int _)} !:= {a()});
//
//test bool enum1() = !(int n <- []);
//test bool enum2() = !(bool b <- ["a", 1]);
//test bool enum3() = !(int n <- [1,2,3,4,5,6,7,8,9,10] && n > 11);
//
//test bool range1() = !(int n <- [1..10] && n > 11);
//test bool range2() = !(int n <- [1,3..10] && n > 11); 
//
//test bool descendant1() = /!4 := 3; 
//test bool descendant2() = /!4 !:= 4; 
//test bool descendant3() = /!4 := 3;
//test bool descendant4() = /!4 := [1,2,3,2];
//test bool descendant5() = /!4 !:= [1,2,3,4];
//test bool descendant6() = !/!4 := [1,2,3,4];
//test bool descendant7() = /int N := 1 && N == 1;
//test bool descendant8() = /[int _, *int _] := [[], [1, 1]];
//
//public (&T <:num) sum(set[(&T <:num)] _:{}) {
//    throw "ArithmeticException"(
//        "For the emtpy set it is not possible to decide the correct precision to return.\n
//        'If you want to call sum on empty set, use sum({0.000}+st) or sum({0r} +st) or sum({0}+st) 
//        'to make the set non-empty and indicate the required precision for the sum of the empty set 
//        ");
//}
//
//public default (&T <:num) sum({(&T <: num) e, *(&T <: num) r})
//    = (e | it + i | i <- r);
//    
//test bool sum1()  = sum({0}) == 0;



//value main(){ //test bool compositeImplCntBTLast2() {
//    n = 0;
//    ten = 10;
//    if( ten > 9 ==> [*int _, int  _, *int _] := [1,2,3] )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 3;
//}


//!({*int _, int N} := {1,2,3,4}) && (N == 1);

//!(bool b <- ["a", 1]);

//int i <- [1,4] && i >= 4;

//!(bool b <- ["a", 1]);
    
 //value main () {
 //   L = [<1,10>, <2,20>];
 //   return <[a | <a,_> <- L], [b | <_,b> <- L]>;
//}
//value main(){ //test bool exceptionHandling3(){
//
//    value f() { throw "Try to catch me!"; }
//    value f(str s) { throw "Try to catch: <s>!"; }
//    
//    str n = "start";
//        
//    // Example of try/catch blocks followed by each other
//    try {
//        throw 100;
//    // Example of the default catch
//    } catch : {
//        n = n + ", then default";
//    }
//    
//    try {
//        throw 100;
//    // Example of an empty, default catch
//    } catch : {
//        ;
//    }
//    
//    try {
//        try {
//            try {
//                n = n + ", then 1";
//                try {
//                    n = n + ", then 2";
//                    f();
//                    n = n + ", then 3"; // dead code
//                // Example of catch patterns that are incomplete with respect to the 'value' domain
//                } catch 0: {        
//                    n = n + ", then 4";
//                } catch 1: {
//                    n = n + ", then 5";
//                } catch "0": {
//                    n = n + ", then 6";
//                }
//                n = n + ", then 7"; // dead code
//        
//            // Example of overlapping catch patterns and try/catch block within a catch block
//            } catch str _: {
//                n = n + ", then 8";
//                try {
//                    n = n + ", then 9";
//                    f(n); // re-throws
//                } catch int _: { // should not catch re-thrown value due to the type
//                    n = n + ", then 10";
//                }
//                n = n + ", then 11";
//            } catch value _: {
//                n = n + ", then 12";
//            }
//    
//        // The outer try block of the catch block that throws an exception (should not catch due to the type)
//        } catch int _: {
//            n = n + ", then 13";
//        }
//    // The outer try block of the catch block that throws an exception (should catch)
//    } catch value v: {
//        n = "<v>, then last catch and $$$";
//    }
//    
//    return n;// == "Try to catch: start, then default, then 1, then 2, then 8, then 9!, then last catch and $$$";
//}
//@javaClass{org.rascalmpl.library.Prelude}
//public java set[&T] toSet(list[&T] lst);
//
//value main() //test bool testSetMultiVariable9()  
//{   R = for({*int S1, *S2} := {100, "a"})  append <S1, S2>; 
//    return toSet(R) == { <{100}, {"a"}>, <{},{100,"a"}> };
//}


//tuple[&T, list[&T]] headTail(list[&T] l) {
//     if ([&T h, *&T t] := l) {
//       return <h, t>;
//     }
//     
//     fail;
//  }
//  
//value main() { //test bool voidListsShouldThrowException() {
//  try {
//      list[value] m = [];
//      tuple[value,list[value]] x = headTail(m);
//      return x != <123,[456]>; // this comparison never happens
//   }
//   catch _ : //CallFailed([[]]) :
//     return true;
//}

//import lang::rascalcore::compile::Examples::Tst2;
//
//syntax Tag
//    = //@Folded @category="Comment" \default   : "@" Name name TagString contents 
//    //| @Folded @category="Comment" empty     : "@" Name name 
//    | @Folded @category="Comment" expression: "@" Name name "=" Expression expression !>> "@"
//    ;
//    
//lexical TagString
//    = "\\" !<< "{" ( ![{}] | ("\\" [{}]) | TagString)* contents "\\" !<< "}";
//
//syntax Expression = "\"MetaVariable\"";
//syntax Name = "category";
//
//syntax ProdModifier = \tag: Tag tag;
//
////data Attr 
////     = \tag(value \tag);
//
//
//value main() = //[Tag] "@category=\"MetaVariable\"";
//  \tag("category"("MetaVariable"));



//data Tree = char(int character);
//private bool check(type[&T] _, value x) = &T _ := x; //typeOf(x) == t.symbol;
//
//
//value main() //test bool notSingleB() 
//    = !check(#[A], char(66));

//value main(){ //test bool optionalPresentIsTrue() {
//    xxx = [];
//    return _ <- xxx;
//}    

//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(list[&T] lst);
//
//list[int] f([*int x, *int y]) { if(size(x) == size(y)) return x; fail; }
////default list[int] f(list[int] l) = l;
//
//value main(){ //test bool overloadingPlusBacktracking2(){
//    return f([1,2,3,4]);// == [1, 2];
//}