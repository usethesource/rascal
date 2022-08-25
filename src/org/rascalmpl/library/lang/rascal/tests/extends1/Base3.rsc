module lang::rascal::tests::extends1::Base3

import ParseTree;

str default_message = "default use";

default str EXTENDED_FUNCTION(Tree _) = default_message;
       
str apply_EXTENDED_FUNCTION_a() =  
EXTENDED_FUNCTION(    // Now follows the expanded version of (A) `a`
appl(
  prod(
    sort("A"),
    [\char-class([range(97,99)])],
    {}),
  [char(97)]));
  
str apply_EXTENDED_FUNCTION_b() =  
EXTENDED_FUNCTION(    // Now follows the expanded version of (A) `b`
appl(
  prod(
    sort("A"),
    [\char-class([range(97,99)])],
    {}),
  [char(98)]));
  
str apply_EXTENDED_FUNCTION_c() =  
EXTENDED_FUNCTION(    // Now follows the expanded version of (A) `c`
appl(
  prod(
    sort("A"),
    [\char-class([range(97,99)])],
    {}),
  [char(99)]));
