module experiments::Compiler::Examples::IMP1

public str dup(str s) = s + s;

value main(list[value] args) = dup("abc");
module experiments::Compiler::Examples::IMP1

public str dup_imp1(str s) = s + s + "_imp1";

value main_imp1(list[value] args) = dup_imp1("IMP1;");