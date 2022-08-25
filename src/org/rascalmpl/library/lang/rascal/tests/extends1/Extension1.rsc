module lang::rascal::tests::extends1::Extension1

// Extension of a function str(value) with str(A)

extend lang::rascal::tests::extends1::Base1;

str non_default_message = "*** NON-default use ***";

str EXTENDED_FUNCTION(a()) = non_default_message;

test bool apply1() = apply_EXTENDED_FUNCTION_a() == non_default_message;

test bool apply2() = apply_EXTENDED_FUNCTION_b() == default_message;

str main() = apply_EXTENDED_FUNCTION_a();
