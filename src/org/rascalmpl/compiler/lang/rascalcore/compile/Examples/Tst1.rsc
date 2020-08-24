module lang::rascalcore::compile::Examples::Tst1
import ParseTree;

syntax A = "a";

value main() //test bool concreteExpressionsHaveSourceLocations1() 
  = (A) `a`.src?;
  
//test bool concreteExpressionsHaveSourceLocations2() 
//  = (A) `a`.src.length == 1;
//  
//test bool concreteExpressionsHaveSourceLocationsLegacy1() 
//  = (A) `a`@\loc?;  
//
//test bool concreteExpressionsHaveSourceLocationsLegacy2() 
//  = (A) `a`@\loc.length == 1;  