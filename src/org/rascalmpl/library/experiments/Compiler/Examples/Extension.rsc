module experiments::Compiler::Examples::Extension

extend experiments::Compiler::Examples::Base;

syntax A = "a";

void EXTENDED_FUNCTION(int n, Tree a: (A) `a`) { println(":-) NON-default use ***"); }

value main() { bug(); return -1; }
