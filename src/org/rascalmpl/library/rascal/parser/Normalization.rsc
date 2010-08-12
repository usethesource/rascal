@doc{
  This module contains normalization rules for abstract grammars.
  
  These rules are an important part of the semantics of Rascal's syntax definition.
  Before generating a parser one should always have these rules applied.
  
  Mainly these rules simplify processing of grammars by grouping definitions by non-terminal,
  and removing some forms of syntactic sugar.
}
module rascal::parser::Normalization

import rascal::parser::Grammar;
import List;
import String;
import ParseTree;
import IO;  
import Integer;

// join the rules for the same non-terminal
rule merge   grammar(set[Symbol] s,{Production p, Production q,set[Production] a}) => grammar(s,{choice(sort(p), {p,q}), a}) when sort(p) == sort(q);

// if a non-terminal consists only of prioritized productions we wrap it with a choice node to limit case distinctions
rule wrap    grammar(set[Symbol] s,{set[Production] a, first(Symbol t, list[Production] alts)}) =>  
             grammar(s,{a, choice(t, {first(t, alts)})});
             
rule wrap    grammar(set[Symbol] s,{set[Production] a, \assoc(Symbol t, Associativity b, set[Production] alts)}) =>  
             grammar(s,{a, choice(t, {\assoc(t, b, alts)})});

// adding this rule makes the normalizer too slow to work with
//rule wrap    grammar(set[Symbol] s, {set[Production] a, Production p:prod(list[Symbol] lhs, Symbol rhs, Attributes attrs)}) =>
//             grammar(s, {a, choice(rhs, {p})});
                 
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
rule restrict restrict(Symbol s, restrict(s, Production p, set[list[Symbol]] q), set[list[Symbol]] r) =>
              restrict(s, p, q + r);

// this makes sure the ... (others) are merged in at the right place
// TODO: we have problems here because unordered productions will also be given an order...
rule others choice(Symbol s, {set[Production] a, others(s)}) => choice(s, a);
rule others choice(Symbol s, {set[Production] a, first(Symbol s, [list[Production] b, others(s), list[Production] c])}) =>
            first(s, b + [choice(s, a)] + c);
  
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
rule restrict restrict(Symbol s, diff(s, Production p, set[Production] o), set[list[Symbol]] r) =>
              diff(s, restrict(s, p, r), o);

rule prod   diff(Symbol s, prod(list[Symbol] lhs, Symbol t, Attributes a), set[Production] diffs) =>
            diff(s, choice(s, {prod(lhs,t,a)}), diffs);
                          
rule choice choice(Symbol s, {restrict(s, Production p, set[list[Symbol]] r), set[Production] q}) =>
            restrict(s, choice(s,{p,q}), r);
rule first  first(Symbol s, [list[Production] pre, restrict(s, Production p, set[list[Symbol]] r), list[Production] post]) =>
            restrict(s, first(s, [pre, p, post]), r);
rule prod   restrict(Symbol s, prod(list[Symbol] lhs, Symbol t, Attributes a), set[list[Symbol]] r) =>
            restrict(s, choice(s,{prod(lhs,t,a)}), r);

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
     
test \char-class([range(2,2), range(1,1)]) == \char-class([range(1,2)]);
test \char-class([range(3,4), range(2,2), range(1,1)]) == \char-class([range(1,4)]);
test \char-class([range(10,20), range(15,20), range(20,30)]) == \char-class([range(10,30)]);
test \char-class([range(10,20), range(10,19), range(20,30)]) == \char-class([range(10,30)]);

rule compl complement(\char-class(list[CharRange] r1)) 										=> \char-class(complement(r1));
rule diff  difference(\char-class(list[CharRange] r1), \char-class(list[CharRange] r2)) 	=> \char-class(difference(r1,r2));
rule union union(\char-class(list[CharRange] r1), \char-class(list[CharRange] r2)) 			=> \char-class(union(r1,r2));
rule inter intersection(\char-class(list[CharRange] r1), \char-class(list[CharRange] r2)) 	=> \char-class(intersection(r1,r2));

public list[CharRange] complement(list[CharRange] s) {
  return difference([range(0,0xFFFF)],s);
}

public list[CharRange] intersection(list[CharRange] l, list[CharRange] r) {
  return complement(union(complement(l), complement(r)));
} 

public list[CharRange] union(list[CharRange] l, list[CharRange] r) {
 cc = \char-class(l + r); // Enforce that the ranges are normalized
 if(\char-class(u) := cc) // and extract them from the normalized char-class
 	return u;
 throw "impossible case in union(<l>, <r>)";
}

// Take difference of two lists of ranges
// Precondition: both lists are ordered
// Postcondition: resulting list is ordered

public list[CharRange] difference(list[CharRange] l, list[CharRange] r) {
  if (l == [] || r == []) return l;

  <lhead,ltail> = <head(l), tail(l)>;
  <rhead,rtail> = <head(r), tail(r)>;

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

test complement(\char-class([])) == \char-class([range(0,65535)]);
test complement(\char-class([range(0,0)])) == \char-class([range(1,65535)]);
test complement(\char-class([range(1,1)])) == \char-class([range(0,0),range(2,65535)]);
test complement(\char-class([range(10,20), range(30,40)])) == \char-class([range(0,9),range(21,29),range(41,65535)]);
test complement(\char-class([range(10,35), range(30,40)])) == \char-class([range(0,9),range(41,65535)]);

test union(\char-class([range(10,20)]), \char-class([range(30, 40)])) == \char-class([range(10,20), range(30,40)]);
test union(\char-class([range(10,25)]), \char-class([range(20, 40)])) == \char-class([range(10,40)]);

test intersection(\char-class([range(10,20)]), \char-class([range(30, 40)])) == \char-class([]);
test intersection(\char-class([range(10,25)]), \char-class([range(20, 40)])) == \char-class([range(20, 25)]);

test difference(\char-class([range(10,30)]), \char-class([range(20,25)])) == \char-class([range(10,19), range(26,30)]);
test difference(\char-class([range(10,30), range(40,50)]), \char-class([range(25,45)])) ==\char-class( [range(10,24), range(46,50)]);


