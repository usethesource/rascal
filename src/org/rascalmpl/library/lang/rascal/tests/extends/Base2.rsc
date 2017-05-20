module lang::rascal::tests::extends::Base2

import ParseTree;

str default_message = "default use";

default str EXTENDED_FUNCTION(int n, Tree t) = default_message;
       
str apply_EXTENDED_FUNCTION_a() =  
EXTENDED_FUNCTION(5,    // Now follows the expanded version of (A) `a`
appl(
  prod(
    sort("A"),
    [\char-class([range(97,98)])],
    {}),
  [char(97)]));
  
str apply_EXTENDED_FUNCTION_b() =  
EXTENDED_FUNCTION(5,    // Now follows the expanded version of (A) `b`
appl(
  prod(
    sort("A"),
    [\char-class([range(97,98)])],
    {}),
  [char(98)]));