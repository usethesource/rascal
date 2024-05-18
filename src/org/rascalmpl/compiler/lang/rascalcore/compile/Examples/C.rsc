module lang::rascalcore::compile::Examples::C

import lang::rascalcore::compile::Examples::B;
int cFunction() = bFunction() + aFunction();

value main() = cFunction();