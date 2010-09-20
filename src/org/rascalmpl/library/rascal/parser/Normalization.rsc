@doc{
  This module contains normalization rules for abstract grammars.
  
  These rules are an important part of the semantics of Rascal's syntax definition.
  Before generating a parser one should always have these rules applied.
  
  Mainly these rules simplify processing of grammars by grouping definitions by non-terminal,
  and removing some forms of syntactic sugar.
}
module rascal::parser::Normalization

import rascal::parser::Grammar;
import rascal::parser::Characters;
import List;
import String;
import ParseTree;
import IO;  
import Integer;
import Set;

rule merge grammar(set[Symbol] starts,set[Production] prods) =>
           grammar(starts, index(prods, Symbol (Production p) { return p.rhs; }));

test grammar({}, {prod([sort("A1")],sort("B"),\no-attrs()), prod([sort("A2")],sort("B"),\no-attrs())}) ==
     grammar({}, {choice(sort("B"), {prod([sort("A1")],sort("B"),\no-attrs()), prod([sort("A2")],sort("B"),\no-attrs())})});
     
// these rules flatten complex productions and ignore ordering under diff and assoc and restrict
rule or     \choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})                    => choice(s,a+b); 
rule single \first(Symbol s, [Production p]) => p;  
rule xor    \first(Symbol s, [list[Production] a,first(Symbol t, list[Production] b),list[Production] c])  => first(s,a+b+c); 
rule xor    \first(Symbol s, [list[Production] a,choice(Symbol t, {Production b}),list[Production] c])     => first(s,a+[b]+c); 
rule \assoc \assoc(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) => \assoc(s, as, a+b); 
rule \assoc \assoc(Symbol s, Associativity as, {set[Production] a, first(Symbol t, list[Production] b)}) => \assoc(s, as, a + { e | e <- b}); // ordering does not work under assoc
rule diff   \diff(Symbol s, Production p, {set[Production] a, choice(Symbol t, set[Production] b)})   => diff(s, p, a+b);   
rule diff   \diff(Symbol s, Production p, {set[Production] a, first(Symbol t, list[Production] b)})   => diff(s, p, a + { e | e <- b});  // ordering is irrelevant under diff
rule diff   \diff(Symbol s, Production p, {set[Production] a, \assoc(Symbol t, a, set[Production] b)}) => diff(s, p, a + b);  // assoc is irrelevant under diff
rule restrict restrict(Symbol s, restrict(Symbol t, Production p, set[Production] q), set[Production] r) =>
              restrict(s, p, q + r);

// this makes sure the ... (others) are merged in at the right place
// TODO: we have problems here because unordered productions will also be given an order...
rule others choice(Symbol s, {set[Production] a, others(Symbol t)}) => choice(s, a) when t == s;
rule others choice(Symbol s, {set[Production] a, first(Symbol t, [list[Production] b, others(Symbol u), list[Production] c])}) =>
            first(s, b + [choice(s, a)] + c) when t == s && t == u;
  
// move diff outwards
// TODO: we have to distribute diff and restrict over the prioritized elements of a first, such that the restrictions and the
// diff's will be applied on all levels of the priority chains
rule empty  diff(Symbol s,Production p,{})                    => p;
rule or     choice(Symbol s, {set[Production] a, diff(Symbol t, Production b, set[Production] c)})   => diff(s, choice(s, a+{b}), c);
rule xor    first(Symbol s, [list[Production] a, diff(Symbol t, Production b, set[Production] c),list[Production] d]) => 
               diff(s, first(a+[b]+d), c);
rule ass    \assoc(Symbol s, Associativity as, {set[Production] a, diff(Symbol t, b, set[Production] c)}) => diff(s, \assoc(s, as, a + {b}), c);
rule diff   diff(Symbol s, Production p, {set[Production] a, diff(Symbol t, Production q, set[Production] b)})   => diff(s, choice(s, {p,q}), a+b); 
rule diff   diff(Symbol s, diff(Symbol t, Production a, set[Production] b), set[Production] c)        => diff(s, a, b+c);
rule restrict restrict(Symbol s, diff(Symbol t, Production p, set[Production] o), set[Production] r) =>
              diff(s, restrict(s, p, r), o);

rule prod   diff(Symbol s, prod(list[Symbol] lhs, Symbol t, Attributes a), set[Production] diffs) =>
            diff(s, choice(s, {prod(lhs,t,a)}), diffs);
                          
rule choice choice(Symbol s, {restrict(Symbol t, Production p, set[Production] r), set[Production] q}) =>
            restrict(s, choice(s,{p,q}), r);
rule first  first(Symbol s, [list[Production] pre, restrict(Symbol t, Production p, set[Production] r), list[Production] post]) =>
            restrict(s, first(s, [pre, p, post]), r);
rule prod   restrict(Symbol s, prod(list[Symbol] lhs, Symbol t, Attributes a), set[Production] r) =>
            restrict(s, choice(s,{prod(lhs,t,a)}), r);
rule restrict restrict(Symbol s, Production p, {restrict(Symbol t, Production q, set[Production] r), set[Production] o})=>
              restrict(s, choice(s, {p,q}), r + o);
rule diff     restrict(Symbol s, Production p, {diff(Symbol t, Production q, set[Production] d), set[Production] o}) =>
              diff(t, restrict(s, choice(t, {p,q}), o), o);
              
rule restricted restricted(restricted(Symbol l)) => restricted(l);

// remove nested assocs (the inner assoc has no meaning after this)
rule nested \assoc(Symbol rhs, Associativity a, {set[Production] rest, \assoc(Symbol rhs2, Associativity b, set[Production] alts)}) =>
            \assoc(rhs, a, rest + alts);
                                                
// no attributes
rule simpl  attrs([]) => \no-attrs();  




