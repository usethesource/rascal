module lang::rascal::tests::concrete::Locations

syntax A = "a";

test void concreteExpressionsHaveSourceLocations1() 
  = (A) `a`.src?;
  
test void concreteExpressionsHaveSourceLocations2() 
  = (A) `a`.src.length == 1;
  
test void concreteExpressionsHaveSourceLocationsLegacy1() 
  = (A) `a`@\loc?;  

test void concreteExpressionsHaveSourceLocationsLegacy2() 
  = (A) `a`@\loc.length == 1;  
  