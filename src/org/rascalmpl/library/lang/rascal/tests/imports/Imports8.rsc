module lang::rascal::tests::imports::Imports8

import lang::rascal::tests::imports::IMP1;
import lang::rascal::tests::imports::IMP2;

test bool import1() = main_imp1([]) == "IMP1;IMP1;_imp1";
test bool import2() = main_imp2([]) == ["IMP1;IMP1;_imp1","IMP2IMP2_imp1","IMP2;IMP2;_imp2"];
test bool import3() = dup_imp1("IMP3") == "IMP3IMP3_imp1";
test bool import4() = dup_imp2("IMP3") == "IMP3IMP3_imp2";
