module lang::rascalcore::compile::Examples::Tst1

extend lang::rascalcore::compile::Examples::Tst0;

data AProduction 
     = prod(AType def, list[AType] atypes, set[AAttr] attributes={}, loc src=|unknown:///|, str alabel = "") // <1>
     | regular(AType def) // <2>
     ;
     
data AAttr 
     = \aassoc(AAssociativity \assoc)
     | \abracket()
     ;
     
data AAssociativity 
     = aleft()
     | aright() 
     | aassoc() 
     | \a-non-assoc()
     ;