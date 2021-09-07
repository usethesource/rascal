module lang::rascalcore::compile::Examples::Tst1

//syntax D = "d";
//syntax Ds = {D ","}* ds;
// 
// value main()//test bool concreteMatchDs10() 
//    = (Ds) `<{D ","}* ds1>,d,<{D ","}* ds2>` := [Ds] "d" && "<ds1>" == "" && "<ds2>" == "";
 
    
    
    
syntax OptTestGrammar = A? a B b;
syntax A = "a";
syntax B = "b";

value main() //test bool optionalPresentIsTrue() 
    = (A)`a` <- ([OptTestGrammar] "b").a;
    
    
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