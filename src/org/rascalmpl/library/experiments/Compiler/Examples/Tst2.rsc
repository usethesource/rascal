module experiments::Compiler::Examples::Tst2

value main(list[value] args) = visit("abbc"){ case /b+/: insert "B"; }; // == "aBc";

//value main(list[value] args) = visit("abc"){ case /b/: insert "B";}; // == "aBc";