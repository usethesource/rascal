module lang::rascal::tests::extends1::Extension3

// Extension of a function str(Tree) with str(A)
// Note: A concrete type at position 0.

extend lang::rascal::tests::extends1::Base3;

syntax A = [abc];      
 
str non_default_message_a = "*** NON-default use a ***";
str non_default_message_c = "*** NON-default use c ***";

str EXTENDED_FUNCTION(a: (A) `a`) = non_default_message_a;
str EXTENDED_FUNCTION(a: (A) `c`) = non_default_message_c;

str main() = apply_EXTENDED_FUNCTION_a();

test bool apply1() = apply_EXTENDED_FUNCTION_a() == non_default_message_a;
test bool apply2() = apply_EXTENDED_FUNCTION_b() == default_message;
test bool apply3() = apply_EXTENDED_FUNCTION_c() == non_default_message_c;
