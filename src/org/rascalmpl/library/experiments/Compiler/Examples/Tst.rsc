module experiments::Compiler::Examples::Tst

//layout Whitespace = [\ ]*;
//syntax A = "a";
//syntax B = "b";
//start syntax AB = "x" {A ","}+ "y" {B ","}+;

//syntax AS = A+;

//syntax ASBS = "xx" A+ "yy";

//value main(list[value] args) = (AB) `x <{A ","}+ XXXXX> y<{B ","}+ YYYYY>` := [AB] "xa, a,ayb,b" ? <XXXXX, YYYYY> : "NOMATCH";

value main(list[value] args) = {<1, "a", 10>, <2, "b", 20>, <1, "abc", 100>}[{1,2}];