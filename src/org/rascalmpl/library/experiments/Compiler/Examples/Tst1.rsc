module experiments::Compiler::Examples::Tst1
import lang::rascal::tests::types::StaticTestingUtils;

// Sanity check on the testing utilities themselves

value main(list[value] args) = checkOK("13;");