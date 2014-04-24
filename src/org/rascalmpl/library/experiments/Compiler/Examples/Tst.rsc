module experiments::Compiler::Examples::Tst
import ParseTree;

syntax A = "a";
syntax As = A+;

public bool main(list[value] args) = (As) `aa` := [As] "aa";

//public bool main(list[value] args) = (As) `<A+ as>` := [As] "a" && "<as>" == "a";