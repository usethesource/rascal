module experiments::Compiler::Examples::Tst1

str declInfo2Doc(str doc) =
    "----
    '<doc>
    '++++";
str d = "AAA\nBBB\n";

value main() = declInfo2Doc(d);