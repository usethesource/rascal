module lang::rascalcore::check::Test2

syntax Type = "t";
syntax Name = "n";
syntax Expression = "e";
syntax KeywordFormal 
    = \default: Type type Name name "=" Expression expression
    ;
    
syntax TypeArg
    = \default: Type type 
    | named: Type type Name name ;