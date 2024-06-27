module lang::rascalcore::compile::Examples::Tst4

int f1(int foo, int foo) = foo;         // Error (2x): 'Double declaration of `foo` at ...
int f2(int foo, int foo = 1) = foo;     // No errors; would expect similar to above
int f3(int foo = 1, int foo = 1) = foo; // No errors; would expect similar to above

//int f(int foo = 2) = foo;
//
//int main() {
//  
//  f(foo = 1);
//  return 0;
//}