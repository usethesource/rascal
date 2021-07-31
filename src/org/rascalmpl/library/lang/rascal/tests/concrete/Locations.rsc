module lang::rascal::tests::concrete::Locations

import ParseTree;
//import IO;

syntax A = "a";
syntax E =  A | "(" E "+" E ")" | "(" E "*" E ")";

value main() = [A] "a";

test bool concreteExpressionsHaveSourceLocations1a() 
  = (A) `a`.src?;
  
test bool parsedExpressionsHaveSourceLocations1a() 
  = ([A] "a").src?;
  
test bool concreteExpressionsHaveSourceLocations1b() 
  = (E) `(a+a)`.src?;

test bool parsedExpressionsHaveSourceLocations1b() 
  = ([E] "(a+a)").src?;
  
test bool concreteExpressionsHaveSourceLocations1c() 
  = (E) `(a+(a*a))`.src?;

test bool parsedExpressionsHaveSourceLocations1c() 
  = ([E] "(a+(a*a))").src?;

test bool concreteExpressionsHaveSourceLocations2a() 
  = (A) `a`.src.length == 1;

test bool parsedExpressionsHaveSourceLocations2a() 
  = ([A] "a").src.length == 1;
    
test bool concreteExpressionsHaveSourceLocations2b() 
  = (E) `(a+a)`.src.length == 5;
  
test bool parsedExpressionsHaveSourceLocations2b() 
  = ([E] "(a+a)").src.length == 5;
  
test bool concreteExpressionsHaveSourceLocations2c() 
  = (E) `(a+(a*a))`.src.length == 9;

test bool parsedExpressionsHaveSourceLocations2c() 
  = ([E] "(a+(a*a))").src.length == 9;  
  
test bool concreteExpressionsHaveSourceLocationsLegacy1a() 
  = (A) `a`@\loc?;

test bool parsedExpressionsHaveSourceLocationsLegacy1a() 
  = ([A] "a")@\loc?;    

test bool concreteExpressionsHaveSourceLocationsLegacy1b() 
  = (E) `(a+a)`@\loc?; 

test bool parsedExpressionsHaveSourceLocationsLegacy1b() 
  = ([E] "(a+a)")@\loc?; 
    
test bool concreteExpressionsHaveSourceLocationsLegacy1c() 
  = (E) `(a+(a*a))`@\loc?;   
  
test bool parsedExpressionsHaveSourceLocationsLegacy1c() 
  = ([E] "(a+(a*a))")@\loc?;    

test bool concreteExpressionsSourceLocationsLengthLegacy1a() 
  = (A) `a`@\loc.length == 1;  

test bool parsedExpressionsSourceLocationsLengthLegacy1a() 
  = ([A] "a")@\loc.length == 1;  
  
test bool concreteExpressionsSourceLocationsLength2b() 
  = (E) `(a+a)`.src.length == 5;

test bool parsedExpressionsSourceLocationsLength1a() 
  = ([E] "(a+a)").src.length == 5;
  
test bool concreteExpressionsSourceLocationsLength1a() 
  = (E) `(a+(a*a))`.src.length == 9;  

test bool parsedExpressionsSourceLocationsLength2a() 
  = ([E] "(a+(a*a))").src.length == 9; 

test bool concreteExpressionsSourceLocationsLength2b() 
  = (E) `(a+(a*a))`.src.length == 9;  

//test bool concreteExpressionsSourceLocationsCorrect1() 
//  = readFile((A) `a`.src) == "a";
//
//test bool concreteExpressionsSourceLocationsCorrect2() 
//  = readFile((E) `(a+a)`.src) == "(a+a)";
//  
//test bool concreteExpressionsSourceLocationsCorrect3() 
//  = readFile((E) `(a+(a*a))`.src) == "(a+(a*a))";

    
test bool concreteExpressionsHaveSourceLocationsAfterVisitWithMatch()  {
   // assert /int _ := (A) `a`; // Removed assert since not relevant for the test itself
   
   t = visit((A) `a`) { 
     case int i => i + 1 
   }

   return t@\loc?;   
}  

test bool concreteExpressionsHaveSourceLocationsAfterVisitWithNoMatch()  {
   t = visit((A) `a`) { 
     case 1239461234912634 => 123498761234896123 
   }

   return t@\loc?;   
} 
  