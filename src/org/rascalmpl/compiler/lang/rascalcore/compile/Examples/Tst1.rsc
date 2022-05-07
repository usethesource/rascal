module lang::rascalcore::compile::Examples::Tst1

syntax Sym
    = iter: Sym symbol "+" 
    ;

data Symbol
     = \iter(Symbol symbol) 
     ;
     
Symbol getTargetSymbol(Symbol sym) {
  switch(sym) {
    case int s : return s;
    default: return sym;
  } 
}