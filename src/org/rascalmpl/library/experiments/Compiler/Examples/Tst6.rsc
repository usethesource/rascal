module experiments::Compiler::Examples::Tst6

import String;

list[str] removeEmptyLines(str s) =
    [ line | line <- split("\n", s), /^[ \t]*$/ !:= line];
    

value main() = removeEmptyLines("abc\n\ndef\n");

//value main(){ int n = 3; return (/<x:<n>>/ := "3" && x == "3");}