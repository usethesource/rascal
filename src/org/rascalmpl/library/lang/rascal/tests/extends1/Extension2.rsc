module lang::rascal::tests::extends1::Extension2

// Extension of a function str(int, Tree) with str(int, A)

extend lang::rascal::tests::extends1::Base2;

syntax A = [ab];      
 
str non_default_message = "*** NON-default use ***";

str EXTENDED_FUNCTION(int n, a: (A) `a`) = non_default_message;

str main() = apply_EXTENDED_FUNCTION_a();

test bool apply1() = apply_EXTENDED_FUNCTION_a() == non_default_message;
test bool apply2() = apply_EXTENDED_FUNCTION_b() == default_message;
