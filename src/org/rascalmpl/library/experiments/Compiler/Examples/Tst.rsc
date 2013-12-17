module experiments::Compiler::Examples::Tst

layout Whitespace = [\ ]*;
syntax A = "a";
syntax B = "b";
start syntax AB = "x" A+ "y";

//syntax AS = A+;

//syntax ASBS = "xx" A+ "yy";

value main(list[value] args) = (AB) `x<A+ XXXXX>y` := [AB] "xaaaaaay" ? XXXXX : "NOMATCH";