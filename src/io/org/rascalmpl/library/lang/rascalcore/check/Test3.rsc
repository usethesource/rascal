module lang::rascalcore::check::Test3

//import lang::rascal::\syntax::Rascal;
    
syntax Sym
// named non-terminals
    = \start: "start"
    ;
    
data Symbol // <1>
     = \start(Symbol symbol)
     | \sort(str name)
     | \parameterized-sort(str name, list[Symbol] parameters) // <6>
     | \parameterized-lex(str name, list[Symbol] parameters)  // <7>
     | \label(str name, Symbol symbol)
     ;

data Attr;

data Grammar;

data Production 
     = prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) // <1>
     ;

list[Symbol] intermix(list[Symbol] syms, Symbol l, set[Symbol] others) {
  if (syms == []) 
    return syms;
    
  //syms = [ sym is layouts ? sym : regulars(sym, l, others) | sym <- syms ];
  others += {l};
  
  // Note that if a user manually put a layouts symbol, then this code makes sure not to override it and
  // not to surround it with new layout symbols  
  while ([*Symbol pre, Symbol sym1, Symbol sym2, *Symbol pst] := syms, !(sym1 in others || sym2 in others)) {
      syms = [*pre, sym1, l, sym2, *pst];
  }
  
  return syms;
}

@doc{intersperses layout symbols in all non-lexical productions}
public Grammar \layouts(Grammar g, Symbol l, set[Symbol] others) {
  return top-down-break visit (g) {
    case prod(\start(Symbol y),[Symbol x], as) => prod(\start(y),[l, x, l],  as)
    //case prod(sort(s),list[Symbol] lhs,as) => prod(sort(s),intermix(lhs, l, others),as)
    //case prod(\parameterized-sort(s,n),list[Symbol] lhs,as) => prod(\parameterized-sort(s,n),intermix(lhs, l, others),as)
    case prod(label(t,sort(s)),list[Symbol] lhs,as) => prod(label(t,sort(s)),intermix(lhs, l, others),as)
    //case prod(label(t,\parameterized-sort(s,n)),list[Symbol] lhs,as) => prod(label(t,\parameterized-sort(s,n)),intermix(lhs, l, others),as) 
  }
} 
