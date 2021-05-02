module lang::rascalcore::compile::Examples::Tst1

import ParseTree;
import Exception;
import IO;
import lang::rascal::tests::concrete::OtherSyntax;

syntax A = "a";
layout WS = [\ \t\n\r]*;

value main() = (A) `a` := parse(#A,"a");
