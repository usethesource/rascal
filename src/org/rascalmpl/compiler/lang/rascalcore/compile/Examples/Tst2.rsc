module lang::rascalcore::compile::Examples::Tst2

lexical Id = [a-z];

syntax A = a:"{" Id x "}";

syntax B = b:"[" A x "]";

//data D = d1(int n) | d2(str s, str n);

int twice(int n) = 2 * n;