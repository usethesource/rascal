module experiments::Compiler::Examples::Extension1

extend experiments::Compiler::Examples::Base1;

bool isSubtype(a1(), a2()) = true;

value main() { return bug1(); }

value dobug1() { return bug1(); }