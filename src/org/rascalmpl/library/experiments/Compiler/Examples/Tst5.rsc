
module experiments::Compiler::Examples::Tst5
import lang::rascal::tests::types::StaticTestingUtils;

// Sanity check on the testing utilities themselves


value main(list[value] args) = checkOK("13;");


//test bool StringVisit4a5()=visit("abcabca"){ case "a": insert "AA"; case /b/: insert "BB";} == "aBBcaBBcAA";
