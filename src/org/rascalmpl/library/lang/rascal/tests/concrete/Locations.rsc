module lang::rascal::tests::concrete::Locations

import ParseTree;

syntax A = "a";

test bool concreteExpressionsHaveSourceLocations1() 
  = (A) `a`.src?;
  
test bool concreteExpressionsHaveSourceLocations2() 
  = (A) `a`.src.length == 1;
  
test bool concreteExpressionsHaveSourceLocationsLegacy1() 
  = (A) `a`@\loc?;  

test bool concreteExpressionsHaveSourceLocationsLegacy2() 
  = (A) `a`@\loc.length == 1;  
  
test bool concreteExpressionsHaveSourceLocationsAfterVisitWithMatch()  {
   assert /int _ := (A) `a`;
   
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
  