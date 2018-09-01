module lang::rascalcore::check::Test2 
data AType
    = formType(str name) 
    | labelType() 
    | booleanType() 
    | integerType() 
    | stringType() 
    | moneyType() 
    ;

    
AType f() = true ? moneyType() : integerType();