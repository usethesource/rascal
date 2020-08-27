module lang::rascalcore::compile::Examples::Tst1

import lang::rascalcore::compile::Examples::Tst2;
//import ParseTree;

void f(Tree t){
    loc l = t@\loc;
}

data F = f() | f(int n) | g(int n) | deep(F f);
anno int F@pos;
  
// testAnnotations


value main() // test bool testAnnotations6() 
    { X = f(); 
    X@pos = 6; 
    X@pos ?= 3;  
    return X@pos == 6; }

//test bool testAnnotations7() { X = f(); X@pos ?= 3; return X@pos == 3; }


//import ParseTree;
//
//syntax A = "a";
//
//value main() //test bool concreteExpressionsHaveSourceLocations1() 
//  = (A) `a`.src?;
  
//test bool concreteExpressionsHaveSourceLocations2() 
//  = (A) `a`.src.length == 1;
//  
//test bool concreteExpressionsHaveSourceLocationsLegacy1() 
//  = (A) `a`@\loc?;  
//
//test bool concreteExpressionsHaveSourceLocationsLegacy2() 
//  = (A) `a`@\loc.length == 1;  