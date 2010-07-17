@doc{
  This module contains normalization rules for abstract grammars.
  
  These rules are an important part of the semantics of Rascal's syntax definition.
  Before generating a parser one should always have these rules applied.
  
  Mainly these rules simplify processing of grammars by grouping definitions by non-terminal,
  and removing some forms of syntactic sugar.
}
module rascal::parser::Normalization

import rascal::parser::Grammar;
import rascal::parser::Regular;
import List;
import String;
import ParseTree;
import IO;  
import Integer;

// join the rules for the same non-terminal
rule merge   grammar(set[Symbol] s,{Production p, Production q,set[Production] a}) => grammar(s,{choice(sort(p), {p,q}), a}) when sort(p) == sort(q);

test grammar({}, {prod([sort("A1")],sort("B"),\no-attrs()), prod([sort("A2")],sort("B"),\no-attrs())}) ==
     grammar({}, {choice(sort("B"), {prod([sort("A1")],sort("B"),\no-attrs()), prod([sort("A2")],sort("B"),\no-attrs())})});
     	
// these rules flatten complex productions and ignore ordering under diff and assoc and restrict
rule or     choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})                    => choice(s,a+b); 
rule single first(Symbol s, [Production p]) => p;  
rule xor    first(Symbol s, [list[Production] a,first(Symbol t, list[Production] b),list[Production] c])  => first(s,a+b+c); 
rule xor    first(Symbol s, [list[Production] a,choice(Symbol t, {Production b}),list[Production] c])     => first(s,a+[b]+c); 
rule \assoc  \assoc(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) => \assoc(s, as, a+b); 
rule \assoc  \assoc(Symbol s, Associativity as, {set[Production] a, first(Symbol t, list[Production] b)}) => \assoc(s, as, a + { e | e <- b}); // ordering does not work under assoc
rule diff   diff(Symbol s, Production p, {set[Production] a, choice(Symbol t, set[Production] b)})   => diff(s, p, a+b);   
rule diff   diff(Symbol s, Production p, {set[Production] a, first(Symbol t, list[Production] b)})   => diff(s, p, a + { e | e <- b});  // ordering is irrelevant under diff
rule diff   diff(Symbol s, Production p, {set[Production] a, \assoc(Symbol t, a, set[Production] b)}) => diff(s, p, a + b);  // assoc is irrelevant under diff
rule restrict restrict(Symbol s, restrict(s, Production p, set[list[Symbol]] q), set[list[Symbol]] r) =>
              restrict(s, p, q + r);

// this rules merges unordered alternatives with the top-most priority
rule or     choice(Symbol s, {set[Production] a, first(Symbol t, [Production p, list[Production] rest])})        => first(t, [choice(s, {a,p}), rest]); 

// this makes sure the ... (others) are merged in at the right place
rule others choice(Symbol s, {set[Production] a, others(s)}) => choice(s, a);
rule others choice(Symbol s, {set[Production] a, first(Symbol s, [list[Production] b, others(s), list[Production] c])}) =>
            first(s, b + [choice(s, a)] + c);
  
// move diff outwards
rule empty  diff(Symbol s,Production p,{})                    => p;
rule or     choice(Symbol s, {set[Production] a, diff(Symbol t, b, set[Production] c)})   => diff(s, choice(s, a+{b}), c);
rule xor    first(Symbol s, [list[Production] a, diff(Symbol t, b, set[Production] c),list[Production] d]) => 
               diff(s, first(a+[b]+d), c);
rule ass    \assoc(Symbol s, Associativity as, {set[Production] a, diff(Symbol t, b, set[Production] c)}) => diff(s, \assoc(s, as, a + {b}), c);
rule diff   diff(Symbol s, Production p, {set[Production] a, diff(Symbol t, Production q, set[Production] b)})   => diff(s, choice(s, {p,q}), a+b); 
rule diff   diff(Symbol s, diff(Symbol t, Production a, set[Production] b), set[Production] c)        => diff(s, a, b+c);

// move restrict outwards
rule choice choice(Symbol s, {restrict(s, Production p, set[list[Symbol]] r), set[Production] q}) =>
            restrict(s, choice(s,{p,q}), r);
rule empty  restrict(Symbol s, Production p, set[list[Symbol]] r) => p;
// TODO: I think we need more reordering rules for restrict soon

// no attributes
rule simpl  attrs([]) => \no-attrs();  

// character class normalization
private data CharRange = \empty-range();
  
rule empty range(int from, int to) => \empty-range() when to < from;
rule empty \char-class([list[CharRange] a,\empty-range(),list[CharRange] b]) => \char-class(a+b);

rule merge \char-class([list[CharRange] a,range(int from1, int to1),list[CharRange] b,range(int from2, int to2),list[CharRange] c]) =>
           \char-class(a+[range(min(from1,from2),max(to1,to2))]+b+c)
     when (from1 <= from2 && to1 >= from2 - 1) 
       || (from2 <= from1 && to2 >= from1 - 1)
       || (from1 >= from2 && to1 <= to2)
       || (from2 >= from1 && to2 <= to1);
    
rule order \char-class([list[CharRange] a,range(int n,int m),list[CharRange] b, range(int o, int p), list[CharRange] c]) =>
           \char-class(a + [range(o,p)]+b+[range(n,m)]+c)
     when p < n;

public Symbol sort(Production p) {
  switch(p){
    case prod(_,Symbol rhs,_):
    	return rhs;
    case choice(s, alts) :
     	return s;
    case first(s, alts) :
     	return s;
    case \assoc(s, a, alts) :
       	return s;
    case diff(s,p,alts) : 
      	return s;
    case restrict(rhs, _, _):
       	return rhs;
    case others(sym):
      	return sym;
   // if (/prod(_,rhs,_) := p || /regular(rhs,_) := p || /restrict(rhs, _, _) := p) {
   // return rhs;
  }
  throw "weird production <p>";
}

rule compl complement(\char-class(list[CharRange] r1)) => \char-class(complement(r1));
rule diff  difference(\char-class(list[CharRange] r1), \char-class(list[CharRange]r2)) => \char-class(difference(r1,r2));
rule union union(\char-class(list[CharRange] r1), \char-class(list[CharRange]r2)) => \char-class(union(r1,r2));
rule inter intersection(\char-class(list[CharRange] r1), \char-class(list[CharRange]r2)) => \char-class(intersection(r1,r2));

public list[CharRange] complement(list[CharRange] s) {
  return difference([range(0,0xFFFF)],s);
}

public list[CharRange] intersection(list[CharRange] l, list[CharRange] r) {
  return union(difference(l,r),difference(r,l));
} 

public list[CharRange] union(list[CharRange] l, list[CharRange] r) {
 return l + r;
}

public list[CharRange] difference(list[CharRange] l, list[CharRange] r) {
  if (l == [] || r == []) return l;

  <lhead,ltail> = takeOneFrom(l);
  <rhead,rtail> = takeOneFrom(r);

  if (lhead == \empty-range()) 
    return difference(ltail, r);

  if (rhead == \empty-range()) 
    return difference(l, rtail);

  // left beyond right
  // <-right-> --------
  // --------- <-left->
  if (lhead.start > rhead.end) 
    return difference(l,rtail); 

  // left before right
  // <-left-> ----------
  // -------- <-right->
  if (lhead.end < rhead.start) 
    return [lhead] + difference(ltail,r);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.start >= rhead.start && lhead.end <= rhead.end) 
    return difference(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.start >= lhead.start && rhead.end <= lhead.end) 
    return [range(lhead.start,rhead.start-1)] 
         + difference([range(rhead.end+1,lhead.end)]+ltail,rtail);

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return [range(lhead.start,rhead.start-1)] + difference(ltail,r); 
    
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.start > rhead.start)
    return difference([range(rhead.end+1,lhead.end)]+ltail, rtail);

  throw "did not expect to end up here! <l> - <r>";
}

