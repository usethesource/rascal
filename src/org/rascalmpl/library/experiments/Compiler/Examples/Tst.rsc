module experiments::Compiler::Examples::Tst

layout Whitespace = [\ ]*;
syntax A = "a";
syntax B = "b";
start syntax AB = A B;

//syntax AS = A+;

//syntax ASBS = "xx" A+ "yy";

value main(list[value] args) = (AB) `<A XXXXX>b` := [AB] "ab";