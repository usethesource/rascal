module experiments::Compiler::Examples::Tst1

import IO;

str declInfo2Doc(str doc) =
    "----
    '<doc>
    '++++";
str d = "AAA
        'BBB
        '";

value main() { println(declInfo2Doc(d)); return true; }