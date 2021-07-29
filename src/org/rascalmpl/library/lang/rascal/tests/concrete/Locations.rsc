module lang::rascal::tests::concrete::Locations

import ParseTree;

syntax A = "a";
syntax E =  A | "(" E "+" E ")" | "(" E "*" E ")";

test bool concreteExpressionsHaveSourceLocations1a() 
  = (A) `a`.src?;
  
test bool concreteExpressionsHaveSourceLocations1b() 
  = (E) `(a+a)`.src?;
  
test bool concreteExpressionsHaveSourceLocations1c() 
  = (E) `(a+(a*a))`.src?;
  
test bool concreteExpressionsHaveSourceLocations2a() 
  = (A) `a`.src.length == 1;
  
test bool concreteExpressionsHaveSourceLocations2b() 
  = (E) `(a+a)`.src.length == 5;
  
test bool concreteExpressionsHaveSourceLocations2c() 
  = (E) `(a+(a*a))`.src.length == 9;
  
test bool concreteExpressionsHaveSourceLocationsLegacy1a() 
  = (A) `a`@\loc?;  

test bool concreteExpressionsHaveSourceLocationsLegacy1b() 
  = (E) `(a+a)`@\loc?; 
  
test bool concreteExpressionsHaveSourceLocationsLegacy1c() 
  = (E) `(a+(a*a))`@\loc?;    

test bool concreteExpressionsHaveSourceLocationsLegacy2a() 
  = (A) `a`@\loc.length == 1;  
  
test bool concreteExpressionsHaveSourceLocationsLegacy2b() 
  = (E) `(a+a)`.src.length == 5;
  
test bool concreteExpressionsHaveSourceLocationsLegacy2c() 
  = (E) `(a+(a*a))`.src.length == 9;  
  
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
  