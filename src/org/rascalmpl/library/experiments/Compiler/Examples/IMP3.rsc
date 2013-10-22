module experiments::Compiler::Examples::IMP3

import experiments::Compiler::Examples::IMP1;
import experiments::Compiler::Examples::IMP2;

value main(list[value] args) = [ main_imp1([]), main_imp2([]), dup_imp1("IMP3"), dup_imp2("IMP3") ];