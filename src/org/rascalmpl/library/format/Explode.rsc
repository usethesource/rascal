@doc{Explode maps abstract data types back to syntax trees by looking up rules which fit in a grammar and by calling parsers where necessary.}
module \format::Explode

import Type;
import Node;
import ParseTree;

@doc{
  Explode transforms an abstract data type to a concrete parse tree by looking up rules 
  which have a related constructor name in a grammar. The map from node names to constructor
  names is defined by the `cons` function which you can extend if necessary.
  
  Also the explode function itself can be extended to provide user-defined language specific
  behavior for special cases. The goal is however, to do as much of this mapping automatically
  using grammar reflection.
}
default (&T<:Tree) explode(node t, (&T<:Tree) grammar) {
  m = hash(grammar.definitions);
  n = getName(t);
  s = grammar.symbol;
  k = getChildren(t);
  
  for (r:prod(label(t, s), args, _) <- m[cons(t)], s is sort, size(args) / 2 == size(k)) {
     try {
        if (&T r := appl(r, [explode(ch, grammar[symbol=ch.prod.symbol]), emptyLayout() | ch <- args][..-1])) {
          return r; // found a match, and converted each child, done!
        }
        // wrong tree type? try next
     }
     catch value _: continue; // this alternative failed somehow, try the next
  }
    
  // this propagates failure back up
  throw ParseError("No match for ~t constructor in grammar for ~(grammar.symbol)");
}

// we parse str values with the symbol we are given
(&T<:Tree) explode(str s, (&T<:Tree) grammar) = parse(grammar, s);

// other values are first formatted (using any formatter in scope) and then parsed using the expected grammar symbol:
default (&T<:Tree) explode(value v:!node _, (&T<:Tree) grammar) = parse(grammar, "~s");

@doc{override this cons function for specific node types to map node names to syntax constructor names}
default str cons(node t) = getName(t);

@memo
private rel[str cons, Production rule] hash(map[Symbol sym, Production rule] grammar)
  = {<cons,rule> | /rule:prod(label(cons,_),_,_) := grammar};
  
private Tree emptyLayout() = appl(prod(layouts("$explode$"),[],{}),[]); 