module lang::rascalcore::compile::Examples::Tst2

test bool tst() = true;   
bool tst() = true;   
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