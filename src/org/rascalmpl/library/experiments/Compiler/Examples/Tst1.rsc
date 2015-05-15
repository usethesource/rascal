module experiments::Compiler::Examples::Tst1

layout L = [\ ]*;
start syntax P = "program";

test bool topField() = P _ := ([start[P]] " program ").top;

test bool hasTop() = ([start[P]] " program ") has top;
