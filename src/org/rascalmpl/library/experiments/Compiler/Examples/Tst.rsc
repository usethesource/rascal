module experiments::Compiler::Examples::Tst

//import ParseTree;
//layout Whitespace = [\ ]*;
//syntax A = "a";
//syntax B = "b";
//start syntax AB = "x" A B "y";
//
//value main(list[value] args) { A a = [A] "a"; return (AB) `x <A a> by`; }

import Exception;
import List;
import ParseTree;

syntax A = a: "a";

syntax As = as: A+ alist;

syntax C = c: A a "x" As as;

test bool tstA(){
    pt = [As] "aaaaaa";
    return pt.alist;
}