module lang::rascalcore::compile::Examples::Tst2
 
import Type;
      
 syntax BasicType
    = \int: "int"
    | \real: "real" 
    ;
    
data Symbol = LUB(Symbol l, Symbol r);
