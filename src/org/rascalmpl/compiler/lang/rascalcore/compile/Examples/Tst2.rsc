module lang::rascalcore::compile::Examples::Tst2
    
    value main() = {<X,Y> |<int X, int Y> <- []};
// 
//import List;
//import IO;
                  
//data T1 = xint() ;
//data T2 = xint() ;
//T1 t1 = T1::xint();  
 
//test bool tstQNameInPatterns() {
//    //T1 t1 = T1::\int();
//    //T2 t2 = T2::\int();
//    
//    //bool fT1(T1::\int()) = true;
//    //bool fT1(T1::\void()) = true;
//    //bool fT1(T1::string(str _)) = true;
//    //default bool fT1(value _) = false;
//    //
//    //bool fT2(T2::\int()) = true;
//    //bool fT2(T2::\void()) = true;
//    //bool fT2(T2::string(str _)) = true;
//    //default bool fT2(value _) = false;
//    //
//    bool tst = true;
//    //bool tstSwitch = true;
//    //bool tstFuncCalls = true;
//    
//  //  tst = tst && T1::\int() := t1 && T2::\int() := t2;
//    //switch(t1) { case T1::\int(): ; default: tstSwitch = false; }
//    //switch(t2) { case T2::\int(): ; default: tstSwitch = false; }
//    //
//    //tstFuncCalls = tstFuncCalls && fT1(t1) && fT2(t2);
//    //
//    //t1 = T1::\void();
//    //t2 = T2::\void();
//    //
//    //tst = tst && T1::\void() := t1 && T2::\void() := t2;
//    //switch(t1) { case T1::\void(): ; default: tstSwitch = false; }
//    //switch(t2) { case T2::\void(): ; default: tstSwitch = false; }
//    //tstFuncCalls = tstFuncCalls && fT1(t1) && fT2(t2);
//    //
//    //t1 = T1::string("t1");
//    //t2 = T2::string("t2");
//    //
//    //tst = tst && T1::string(str _) := t1 && T2::string(str _) := t2;
//    //switch(t1) { case T1::string(str _): ; default: tstSwitch = false; }
//    //switch(t2) { case T2::string(str _): ; default: tstSwitch = false; }
//    //tstFuncCalls = tstFuncCalls && fT1(t1) && fT2(t2);
//    
//    return tst 
//            //&& tstSwitch && tstFuncCalls
//            ;
//}
//   
//test bool deepMatchKeywordParameter() = /int i := "f"("f"(x=[1]));
 
//value main() = {ig()} := { ig() };
//  
//data IG = ig(int x = 1); 
//
//test bool ignoreKeywordParameter1() = ig() := ig(x=1);
//test bool ignoreKeywordParameter2() = ig(x=1) := ig();
//test bool ignoreKeywordParameter3() = "bla"() := "bla"(y=1);
//test bool ignoreKeywordParameter4() = {ig()} := {ig(x=1)};
//test bool ignoreKeywordParameter5() = {{ig()}} := {{ig(x=1)}};
//test bool ignoreKeywordParameter6() = <ig(),_> := <ig(x=1),2>;
//test bool ignoreKeywordParameter7() = [ig(),_] := [ig(x=1),2];
//test bool ignoreKeywordParameter8() = "fiets"(ig()) := "fiets"(ig(x=1));
//
//@ignore{Not yet operational}
//test bool ignoreKeywordParameter9() { A = ig(x = 1); return A := ig(); }
//
//@ignore{Not yet operational}
//test bool ignoreKeywordParameter10() { L = [ ig(x = 1) ]; return L := [ ig() ]; }
//
//@ignore{Not yet operational}
//test bool ignoreKeywordParameter11() { S = { ig(x = 1) }; return S := { ig() }; }
//
//@ignore{Not yet operational}
//test bool ignoreKeywordParameter12() { M = (ig(x = 1): ig(x = 1)); return M := (ig(): ig()); }
//
//@ignore{Not yet operational}
//test bool ignoreKeywordParameter13() { M = ([ig(x = 1)]: ig(x = 1)); return M := ([ig()]: ig()); }
//
//@ignore{Not yet operational}
//test bool ignoreKeywordParameter14() { T = <ig(x = 1), ig(x = 1)>; return T := <ig(), ig()>; }
// 
 //////   
 
 
  //import String;
//str deescape(str s)  {
//    res = visit(s) { 
//        //case /^\\<c: [\" \' \< \> \\]>/ => c
//        //case /^\\t/ => "\t"
//        case /^\\n/ => "\n"
//        //case /^\\u<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
//        //case /^\\U<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
//        //case /^\\a<hex:[0-7][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
//        }; 
//    return res;  
//}                
//test bool deescape1() = deescape("\\\"") == "\"";
//test bool deescape2() = deescape("\\n") == "\n";
//test bool deescape3() = deescape("\\uAA11") == "\uAA11";
//test bool deescape4() = deescape("\\U012345") == "\U012345";
//test bool deescape5() = deescape("\\a0F") == "\a0f";
 

//value main() = deescape2();

  
//@javaClass{org.rascalmpl.library.Prelude}
//public java map[&K, set[&V]] index(lrel[&K, &V] R);
//   
//public list[int] index(list[&T] lst) = [];

   

//public tuple[list[&T],list[&U]] unzip(list[tuple[&T,&U]] lst) =
//    <[t | <t,_> <- lst], [u | <_,u> <- lst]>;
//
//// Make a triple of lists from a list of triples.
//public tuple[list[&T],list[&U],list[&V]] unzip(list[tuple[&T,&U,&V]] lst) =
//    <[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;

          
//public rel[&T,&T] toRel(list[&T] lst) {
//  return { <from,to> | [_*, from, to, _*] := lst };
//} 
// 
//public rel[&K,&V] toRel(map[&K,set[&V]] M) =  {<k,v> | &K k <- M, &V v <- M[k]};
//public rel[&K,&V] toRel(map[&K,list[&V]] M) = {<k,v> | &K k <- M, &V v <- M[k]};
//@javaClass{org.rascalmpl.library.Prelude}
//public default java rel[&K, &V] toRel(map[&K, &V] M);
  
              
//int f(lrel[str,int] l) = 0;
//int f(lrel[str,str] l) = 0; 
                                     
//int inc(num v) = 3; 
//default int inc(int n) = n + 1;
//        
//value main() = inc(5);
                   
//int inc(0) = -100; 
//default int inc(int n) = n + 1;
//str inc(str s) = s + "x";
//bool inc(int a, int b) = a > b;
                                    
//public set[&T]  carrier (rel[&T,&T] R)
//{ 
//  return R<0> + R<1>;
//}
//
//public set[&T]  carrier (rel[&T,&T,&T] R)
//{
//  return (R<0> + R<1>) + R<2>;
//}

//value main() = inc("a"); 
    
//int size(list[&T] lst) = 0;
//list[int] index(list[&T] lst) = [0..10];
//  
//@doc{
//.Synopsis
//A Symbol represents a Rascal Type.
//
//.Description
//Symbols are values that represent Rascal's types. These are the atomic types.
//We define here:
//
//<1>  Atomic types.
//<2> Labels that are used to give names to symbols, such as field names, constructor names, etc.
//<3>  Composite types.
//<4>  Parameters that represent a type variable.
//
//In <<Prelude-ParseTree>>, see <<ParseTree-Symbol>>, 
//Symbols will be further extended with the symbols that may occur in a ParseTree.
//}  
//data Symbol    // <1>
//     = \int()
//     | \num()
//     | \tuple(list[Symbol] symbols)
//     | \value()
//     ;
// 
//public bool subtype(type[&T] t, type[&U] u) = subtype(t.symbol, u.symbol);
//    
//public bool subtype(Symbol s, s) = true;
//public default bool subtype(Symbol s, Symbol t) = false;
//
//public bool subtype(Symbol s, Symbol::\value()) = true;
//public bool subtype(Symbol::\int(), Symbol::\num()) = true;
//public bool subtype(Symbol::\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = subtype(l, r);
//  
//public bool subtype(list[Symbol] l, list[Symbol] r) = all(i <- index(l), subtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
//public default bool subtype(list[Symbol] l, list[Symbol] r) = size(l) == 0 && size(r) == 0;
// 
// value main() = subtype([\int()], [\int()]);
// 
   
  
//import List;
//import Node; 

//data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();
//
//B and(B b1, and(B b2, B b3)) = and(and(b1,b2),b3);

//test bool normalizedCall(B b1, B b2, B b3) = and(b1, and(b2, b3)) == and(and(b1, b2),b3);
 
//test bool normalizedVisit() =
//  /and(_, and(_, _)) !:= visit (or(or(t(),t()),or(t(),t()))) { case or(a,b) => and(a,b) };

//value main() { return  /and(_,_) := t(); }

//visit (or(or(t(),t()),or(t(),t()))) { case or(a,b) => and(a,b) };
//
//
//data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();
//
//B and(B b1, and(B b2, B b3)) = and(and(b1,b2),b3);
//
////value callDelAnnotations() = delAnnotations("f"(1,2,3));
////
////test bool testCallWithTypeParameterBound() = callDelAnnotations() == "f"(1,2,3);
//
//test bool normalizedCall(B b1, B b2, B b3) = and(b1, and(b2, b3)) == and(and(b1, b2),b3);
//   
//value main() = normalizedCall(t(), f(), and(t(), t()));

  
      
//value main(){
//  ten = 10;
//  b = [*int x] := [1] ==> ([*int p] := [4] && int r := 4);
//  return b;
//}
   

//import Type;
//
//test bool nonComparabilityImpliesNonEq(value x, value y) = !comparable(typeOf(x),typeOf(y)) ==> !eq(x,y);
//
//value main() = typeOf("abc");
                                     
//value main() = 
//    {<x, y> | [*x,*y] := [1] };  
            
//value main() {
//    z = [];
//    while( ([*x,*y] := [1]))  {
//        z += <x,y>;
//        fail;
//    }
////    return z;
//}            
 
 
            
//import List;
//            
//import ListRelation; 
//  test bool subscription1(lrel[&A, &B, &C] X) =
//  isEmpty(X) ||
//  all(&A a <- domain(X), any(<&B b, &C c> <- X[{a}], <a, b, c> in X)) &&
//  all(<&A a, &B b, &C c> <- X, <b, c> in X[{a}]);
             
                  
//void f(int n) { }
// 
//void f()  {}
//
//value main(){
//    f(10);
//    f();
//    return true;
//}
    
//import Exception;
                      
//@javaClass{org.rascalmpl.library.Prelude}
//@reflect{For getting IO streams}
//public java void println(value arg);
//
//@javaClass{org.rascalmpl.library.Prelude}
//@reflect{For getting IO streams}
//public java void println();
//
//public &T printlnExp(&T v) {
//    //println("<v>");
//    return v;
//}
// 
//public &T printlnExp(str msg, &T v) {
//    //println("<msg><v>");
//    return v;
//}
 
//@javaClass{org.rascalmpl.library.Prelude}
//public java void appendToFile(loc file, value V...)
//throws PathNotFound, IO;
//
//public void touch(loc file)
//throws PathNotFound, IO{
//  appendToFile(file);
//}

 // ----            
//                                            
//int f() = 3;
//void f() { }
    

//int container1(){
//    int f() = 3;
//      
//    return f();
//}       
//  
//void container2(){
//    void f() {}
//    
//    f();
//}      
    
//import List;
//import util::Math;
//import Boolean;
// 
//test bool tstIntercalate(str sep, list[value] L) { 
//      return sep == (isEmpty(L) ? "" : "<sep>");
//   //   return true;
//  }                                   