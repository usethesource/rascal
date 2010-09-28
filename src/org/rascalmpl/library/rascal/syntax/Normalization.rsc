@doc{
  This module contains normalization rules for abstract grammars.
  
  These rules are an important part of the semantics of Rascal's syntax definition.
  Before generating a parser one should always have these rules applied.
}
module rascal::syntax::Normalization

import rascal::syntax::Grammar;
import rascal::syntax::Characters;
import List;
import String;
import ParseTree;
import IO;  
import Integer;
import Set;

// Explanation of rewrite rule names:
// "noop" means nested application of some operator is ignored
// "factor" means nested application of some operator is factored out
// "flat" means unnecessary nestings are removed (like a choice in a choice)
// "fuse" means several productions are merged from different parts (like to implement "...")

rule flat \choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})
             => \choice(s,a+b);
rule flat \first(Symbol s, [list[Production] a, first(Symbol t, list[Production] b),list[Production] c])
             => \first(s,a+b+c); 
rule flat \assoc(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) 
             => \assoc(s, as, a+b); 
rule flat \diff(Symbol s, Production p, {set[Production] a, choice(Symbol t, set[Production] b)})   
             => \diff(s, p, a+b);
rule flat \diff(Symbol s, Production p, {set[Production] a, diff(Symbol t, Production q, set[Production] b)})   
             => \diff(s, choice(s, {p,q}), a+b); 
rule flat \diff(Symbol s, diff(Symbol t, Production a, set[Production] b), set[Production] c)        
             => \diff(s, a, b+c);
rule flat \restrict(Symbol s, restrict(Symbol t, Production p, set[Production] q), set[Production] r) 
             => \restrict(s, p, q + r);
rule flat \restrict(Symbol s, Production p, {restrict(Symbol t, Production q, set[Production] r), set[Production] o})
             => \restrict(s, choice(s, {p,q}), r + o);
rule flat \choice(Symbol s, {others(Symbol t)}) 
             => others(t);
             
rule noop \assoc(Symbol rhs, Associativity a, {\assoc(Symbol rhs2, Associativity b, set[Production] alts), set[Production] rest}) 
             => \assoc(rhs, a, rest + alts);
rule noop \assoc(Symbol s, Associativity as, {set[Production] a, first(Symbol t, list[Production] b)}) 
             => \assoc(s, as, a + { e | e <- b}); 
 
rule fuse \choice(Symbol s, {set[Production] a, others(Symbol t)}) 
               => \choice(s, a) when t == s;
rule fuse \choice(Symbol s, {set[Production] a, first(Symbol t, [list[Production] b, others(Symbol u), list[Production] c])}) 
               => \first(s, b + [choice(s, a)] + c);
  
// factoring out diff  
 rule factor \choice(Symbol s, {diff(Symbol t, Production b, set[Production] c), set[Production] a})   
                => \diff(s, choice(s, a+{b}), c);
 rule factor \first(Symbol s, [list[Production] a, diff(Symbol t, Production b, set[Production] c),list[Production] d]) 
                => \diff(s, first(a+[b]+d), c);
 rule factor \assoc(Symbol s, Associativity as, {set[Production] a, diff(Symbol t, b, set[Production] c)}) 
                => \diff(s, \assoc(s, as, a + {b}), c);
 rule factor \restrict(Symbol s, diff(Symbol t, Production p, set[Production] o), set[Production] r) 
                => \diff(s, restrict(s, p, r), o);
 rule factor \restrict(Symbol s, Production p, {diff(Symbol t, Production q, set[Production] d), set[Production] o}) 
                => \diff(t, restrict(s, choice(t, {p,q}), o), d);

// factoring out restrict                          
 rule factor \choice(Symbol s, {restrict(Symbol t, Production p, set[Production] r), set[Production] q}) 
                => \restrict(s, choice(s,{p,q}), r);
 rule factor \first(Symbol s, [list[Production] pre, restrict(Symbol t, Production p, set[Production] r), list[Production] post])
                => restrict(s, first(s, [pre, p, post]), r);
 rule factor \assoc(Symbol s, Associativity as, {set[Production] a, restrict(Symbol t, Production p, set[Production] r)}) 
                => \restrict(t, \assoc(s,as,a + {p}), r);
     
// restricted is idempotent              
 rule idem restricted(restricted(Symbol l)) => restricted(l);

// \no-attrs is a synonym for attrs([])                                                
 rule synonym  attrs([]) => \no-attrs();  




