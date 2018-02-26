module lang::rascalcore::check::Test1

lexical Id  = [a-z] \ Reserved;
keyword Reserved = "b";

syntax Module
    = Id
    ;