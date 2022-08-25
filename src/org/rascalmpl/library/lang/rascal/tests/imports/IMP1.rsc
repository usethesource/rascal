module lang::rascal::tests::imports::IMP1


public str dup_imp1(str s) = s + s + "_imp1";

value main_imp1(list[value] _) = dup_imp1("IMP1;");
