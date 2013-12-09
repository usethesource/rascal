module experiments::Compiler::Examples::Tst

public bool main(list[value] args) {
   { int n = 3; return (/<x:<n>>/ := "3" && x == "3");}
}