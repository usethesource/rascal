module lang::rascalcore::check::Test4


syntax Sym
    = iter: Sym symbol "+" 
    ;
 
data Symbol
     = \iter(Symbol symbol) // <13>
     | \iter-star(Symbol symbol)  // <14>
     ;
     
private Symbol getTargetSymbol(Symbol sym) {
  switch(sym) {
    case \iter(s1) : return s1;
    //case \iter-star(s) : return s;  
  } 
}