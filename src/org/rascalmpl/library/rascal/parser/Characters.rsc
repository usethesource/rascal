@doc{
  In this module character classes are normalized.
  
  It also provides a number of convenience functions on character classes.
}
module rascal::parser::Characters

import ParseTree;
import rascal::parser::Grammar;
import List;

private data CharRange = \empty-range();
  
rule empty range(int from, int to) => \empty-range() when to < from;
rule empty \char-class([list[CharRange] a,\empty-range(),list[CharRange] b]) => \char-class(a+b);

rule merge \char-class([list[CharRange] a,range(int from1, int to1),list[CharRange] b,range(int from2, int to2),list[CharRange] c]) =>
           \char-class(a+[range(min([from1,from2]),max([to1,to2]))]+b+c)
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
