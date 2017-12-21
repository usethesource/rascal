@doc{
  This module implements the semantics of the ... and :cons notations in Rascal grammars.
  
  To give the proper semantics to priorities, this must be run before the priority relation
  is expanded.
}
module lang::rascal::grammar::definition::References

extend Grammar;
extend ParseTree;

Grammar references(Grammar g) = visit (g) {
  case priority(s, ps) => priority(s, [ lookup(p, g.rules) | p <- ps ]) 
  case associativity(s, a, ps) => associativity(s, a, { lookup(p, g.rules) | p <- ps })
  case choice(s, ps) => choice(s, { lookup(p, g.rules) | p <- ps })
};

private Production lookup(others(Symbol s), map[Symbol, Production] rules) = choice(s, rules[s]);

private Production lookup(reference(Symbol s, str name), map[Symbol, Production] rules) {
  if (/Production a:associativity(s,_,/prod(label(name, s), _, _)) := rules[s])
    return a; // a single labeled rule represents an entire associativity group
  else if (/Production p:prod(label(name, s), _, _) := rules[s]) {
    return p; // otherwise a labeled rule simply represents itself
  }
  else fail; // reference not found
}

private default Production lookup(Production p, map[Symbol, Production] rules) = p;

