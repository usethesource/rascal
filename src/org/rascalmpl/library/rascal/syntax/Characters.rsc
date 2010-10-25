@doc{
  In this module character classes are normalized.
  
  It also provides a number of convenience functions on character classes.
}
module rascal::syntax::Characters

import ParseTree;
import rascal::syntax::Grammar;
import List;

public data CharRange = \empty-range();    
  
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

public bool lessThan(CharRange r1, CharRange r2) {
  if (range(s1,e1) := r1, range(s2,e2) := r2) {
    return e1 < s2;
  }
  throw "unexpected ranges <r1> and <r2>";
}

public CharRange difference(CharRange l, CharRange r) {
  if (l == \empty-range() || r == \empty-range()) return l;
  
  if (\char-range(ls,le) := l, \char-range(rs,re) := r) {
    // left beyond right
    // <-right-> --------
    // --------- <-left->
    if (ls > re) 
      return l; 

    // left before right
    // <-left-> ----------
    // -------- <-right->
    if (le < rs) 
      return l;

    // inclusion of left into right
    // <--------right------->
    // ---------<-left->-----
    if (ls >= rs && le <= re) 
      return \empty-range(); 

    // inclusion of right into left
    // -------<-right->------->
    // <---------left--------->
    if (rs >= ls && re <= le) 
      return range(ls,rs-1);

    // overlap on left side of right
    // <--left-------->----------
    // ---------<-----right----->
    if (le < re) 
      return range(ls,rs-1); 
    
    // overlap on right side of right
    // -------------<---left---->
    // <----right------->--------
    if (ls > rs)
      return range(re+1,le);

  }
  
  throw "did not expect to end up here! <l> - <r>"; 
}

public CharRange intersect(CharRange r1, CharRange r2) {
  if (r1 == \empty-range() || r2 == \empty-range()) return \empty-range();
  
  if (range(s1,e1) := r1, range(s2,e2) := r2) {
    // left beyond right
    // <-right-> --------
    // --------- <-left->
    if (s1 > e2) 
      return \empty-range(); 

    // left before right
    // <-left-> ----------
    // -------- <-right->
    if (e1 < s2) 
      return \empty-range();

    // inclusion of left into right
    // <--------right------->
    // ---------<-left->-----
    if (s1 >= s2 && e1 <= e2) 
      return r1; 

    // inclusion of right into left
    // -------<-right->------->
    // <---------left--------->
    if (s2 >= s1 && e2 <= e1) 
      return r2; 

    // overlap on left side of right
    // <--left-------->----------
    // ---------<-----right----->
    if (e1 < e2) 
      return range(s2,e1); 
    
    // overlap on right side of right
    // -------------<---left---->
    // <----right------->--------
    if (s1 > s2)
      return range(s1,e2); 
  }
  
  throw "unexpected ranges <r1> and <r2>";
}

public list[CharRange] complement(list[CharRange] s) {
  return difference([range(0,0xFFFF)],s);
}

public list[CharRange] intersection(list[CharRange] l, list[CharRange] r) {
  if (l == r) return l;
  if (l == [] || r == []) return [];
  
  <lhead,ltail> = <head(l), tail(l)>;
  <rhead,rtail> = <head(r), tail(r)>;

  if (lhead == \empty-range()) 
    return intersection(ltail, r);

  if (rhead == \empty-range()) 
    return intersection(l, rtail);

  // left beyond right
  // <-right-> --------
  // --------- <-left->
  if (lhead.start > rhead.end) 
    return intersection(l,rtail); 

  // left before right
  // <-left-> ----------
  // -------- <-right->
  if (lhead.end < rhead.start) 
    return intersection(ltail,r);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.start >= rhead.start && lhead.end <= rhead.end) 
    return lhead + intersection(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.start >= lhead.start && rhead.end <= lhead.end) 
    return rhead + intersection(l,rtail); 

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return range(rhead.start,lhead.end) + intersection(ltail,r); 
    
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.start > rhead.start)
    return range(lhead.start,rhead.end) + intersection(l,rtail); 
    
  throw "did not expect to end up here! <l> - <r>";
  
} 

public list[CharRange] union(list[CharRange] l, list[CharRange] r) {
  if (l == r) return l;
  if (l == []) return r;
  if (r == []) return l;
  
  <lhead,ltail> = <head(l), tail(l)>;
  <rhead,rtail> = <head(r), tail(r)>;

  if (lhead == \empty-range()) 
    return union(ltail, r);

  if (rhead == \empty-range()) 
    return union(l, rtail);

  // left beyond right
  // <-right-> --------
  // --------- <-left->
  if (lhead.start > rhead.end) 
    return rhead + union(l,rtail); 

  // left before right
  // <-left-> ----------
  // -------- <-right->
  if (lhead.end < rhead.start) 
    return lhead + union(ltail,r);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.start >= rhead.start && lhead.end <= rhead.end) 
    return union(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.start >= lhead.start && rhead.end <= lhead.end) 
    return union(l,rtail); 

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return union(range(lhead.start,rhead.end) + ltail, rtail); 
    
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.start > rhead.start)
    return union(ltail,range(rhead.start,lhead.end) + rtail); 
    
  throw "did not expect to end up here! <l> - <r>";
  
}

// Take difference of two lists of ranges
// Precondition: both lists are ordered
// Postcondition: resulting list is ordered

public list[CharRange] difference(list[CharRange] l, list[CharRange] r) {
  if (l == [] || r == []) return l;
  if (l == r) return [];
  
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
    return lhead + difference(ltail,r);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.start >= rhead.start && lhead.end <= rhead.end) 
    return difference(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.start >= lhead.start && rhead.end <= lhead.end) 
    return range(lhead.start,rhead.start-1) 
         + difference(range(rhead.end+1,lhead.end)+ltail,rtail);

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return range(lhead.start,rhead.start-1) + difference(ltail,r); 
    
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.start > rhead.start)
    return difference(range(rhead.end+1,lhead.end)+ltail, rtail);

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
