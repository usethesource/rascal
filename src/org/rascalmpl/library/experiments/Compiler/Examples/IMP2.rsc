module experiments::Compiler::Examples::IMP2

import experiments::Compiler::Examples::IMP1;

public str dup_imp2(str s) = s + s + "_imp2";

value main_imp2(list[value] args) = [ main_imp1([]), dup_imp1("IMP2"), dup_imp2("IMP2;") ];
