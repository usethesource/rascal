 module experiments::Compiler::Examples::Tst


import ParseTree;
syntax A = "a";
syntax As = A+;

value main(list[value] args) = (As) `<A+ as>a` := [As] "aaaa"? "<as>" : false;