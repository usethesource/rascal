module experiments::Compiler::Examples::Tst1

int f(/[a-z]+/) = 1;
int f(/[0-9]+/) = 2;
value main() = f("123");