module experiments::Compiler::Examples::Concrete1

import experiments::Compiler::Examples::ParseTreeDataType;

//syntax AB = A B;
syntax A = "a";
//syntax B = "b";

value main(list[value] args) {
return (A) `a`;
//zzzzz = (B) `b`;
//AB y = (AB) `a<B zzzzz>`;
//return y;
}