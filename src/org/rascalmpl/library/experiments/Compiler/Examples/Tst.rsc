module experiments::Compiler::Examples::Tst

import ParseTree;
layout Whitespace = [\ ]*;
syntax A = "a";
syntax B = "b";
start syntax AB = "x" A B "y";

value main(list[value] args) { A a = [A] "a"; return (AB) `x <A a> by`; }

//value main(list[value] args) =
//appl(
//prod(
//  sort("AB"),
//  [
//    lit("x"),
//    layouts("Whitespace"),
//    sort("A"),
//    layouts("Whitespace"),
//    sort("B"),
//    layouts("Whitespace"),
//    lit("y")
//  ],
//  {}),
//  []);
