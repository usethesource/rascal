@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc{
  In this module character classes are normalized.
  
  It also provides a number of convenience functions on character classes.
}
module lang::rascalcore::agrammar::definition::Characters

import lang::rascal::\syntax::Rascal;
extend lang::rascalcore::check::ATypeBase;
import String;
import List;

//data AType;
data ACharRange = \empty-range();

ACharRange \new-range(int from, int to) = from <= to ? arange(from, to) : \empty-range();
 
public AType \new-char-class(list[ACharRange] ranges) 
  = \achar-class(([] | union(it, [r]) | r <- ranges));
  
//test bool testFlip() = \new-char-class([range(2,2), range(1,1)]) == \achar-class([range(1,2)]);
//test bool testMerge() = \new-char-class([range(3,4), range(2,2), range(1,1)]) == \achar-class([range(1,4)]);
//test bool testEnvelop() = \new-char-class([range(10,20), range(15,20), range(20,30)]) == \achar-class([range(10,30)]);
//test bool testEnvelop2() = \new-char-class([range(10,20), range(10,19), range(20,30)]) == \achar-class([range(10,30)]);

public AType complement(\achar-class(list[ACharRange] r1)) =
  \achar-class([ r | r <- complement(r1), !(r is \empty-range)]);
  
public default AType  complement(AType s) {
  throw "unsupported symbol for character class complement: <s>";
}
  
public AType difference(\achar-class(list[ACharRange] r1), \achar-class(list[ACharRange] r2)){
    res = \achar-class([r | r <- difference(r1,r2), !(r is \empty-range)]);
    return res;
}

public default AType  difference(AType s, AType t) {
  throw "unsupported symbols for  character class difference: <s> and <t>";
}

public AType union(\achar-class(list[ACharRange] r1), \achar-class(list[ACharRange] r2))
 = \achar-class([ r | r <- union(r1,r2), !(r is \empty-range)]);
 
public default AType  union(AType s, AType t) {
  throw "unsupported symbols for union: <s> and <t>";
}

public AType intersection(\achar-class(list[ACharRange] r1), \achar-class(list[ACharRange] r2)) 
 = \achar-class([ r | r <- intersection(r1,r2), !(r is \empty-range)]);

public default AType  intersection(AType s, AType t) {
  throw "unsupported symbols for intersection: <s> and <t>";
}

public bool lessThan(ACharRange r1, ACharRange r2) {
  if (ACharRange::arange(_,e1) := r1, ACharRange::arange(s2,_) := r2) {
    return e1 < s2;
  }
  throw "unexpected ranges <r1> and <r2>";
}

public ACharRange difference(ACharRange l, ACharRange r) {
  if (l == \empty-range() || r == \empty-range()) return l;
  
  if (arange(ls,le) := l, arange(rs,re) := r) {
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
    // ---------<-left->-----
    if (ls >= rs && le <= re) 
      return \empty-range(); 

    // inclusion of right into left
    // -------<-right->------->
    // <---------left--------->
    if (rs >= ls && re <= le) 
      return \new-range(ls,rs-1);

    // overlap on left side of right
    // <--left-------->----------
    // ---------<-----right----->
    if (le < re) 
      return \new-range(ls,rs-1); 
    
    // overlap on right side of right
    // -------------<---left---->
    // <----right------->--------
    if (ls > rs)
      return arange(re+1,le);

  }
  
  throw "did not expect to end up here! <l> - <r>"; 
}

public ACharRange intersect(ACharRange r1, ACharRange r2) {
  if (r1 == \empty-range() || r2 == \empty-range()) return \empty-range();
  
  if (arange(s1,e1) := r1, arange(s2,e2) := r2) {
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
      return arange(s2,e1); 
    
    // overlap on right side of right
    // -------------<---left---->
    // <----right------->--------
    if (s1 > s2)
      return arange(s1,e2); 
  }
  
  throw "unexpected ranges <r1> and <r2>";
}

public list[ACharRange] complement(list[ACharRange] s) {
  return difference([arange(1,0x10FFFF)],s); // the 0 character is excluded
}

public list[ACharRange] intersection(list[ACharRange] l, list[ACharRange] r) {
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
  if (lhead.begin > rhead.end) 
    return intersection(l,rtail); 

  // left before right
  // <-left-> ----------
  // -------- <-right->
  if (lhead.end < rhead.begin) 
    return intersection(ltail,r);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.begin >= rhead.begin && lhead.end <= rhead.end) 
    return lhead + intersection(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.begin >= lhead.begin && rhead.end <= lhead.end) 
    return rhead + intersection(l,rtail); 

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return arange(rhead.begin,lhead.end) + intersection(ltail,r); 
    
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.begin > rhead.begin)
    return arange(lhead.begin,rhead.end) + intersection(l,rtail); 
    
  throw "did not expect to end up here! <l> - <r>";
  
} 

public list[ACharRange] union(list[ACharRange] l, list[ACharRange] r) {
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
  if (lhead.begin > rhead.end + 1) 
    return rhead + union(l,rtail); 

  // left before right
  // <-left-> ----------
  // -------- <-right->
  if (lhead.end + 1< rhead.begin) 
    return lhead + union(ltail,r);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.begin >= rhead.begin && lhead.end <= rhead.end) 
    return union(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.begin >= lhead.begin && rhead.end <= lhead.end) 
    return union(l,rtail); 

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return union(arange(lhead.begin,rhead.end) + ltail, rtail); 
    
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.begin > rhead.begin)
    return union(ltail,arange(rhead.begin,lhead.end) + rtail); 
    
  throw "did not expect to end up here! <l> - <r>";
  
}

// Take difference of two lists of ranges
// Precondition: both lists are ordered
// Postcondition: resulting list is ordered

public list[ACharRange] difference(list[ACharRange] l, list[ACharRange] r) {
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
  if (lhead.begin > rhead.end) 
    return difference(l,rtail); 

  // left before right
  // <-left-> ----------
  // -------- <-right->
  if (lhead.end < rhead.begin) 
    return lhead + difference(ltail,r);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.begin >= rhead.begin && lhead.end <= rhead.end) 
    return difference(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.begin >= lhead.begin && rhead.end <= lhead.end) 
    return \new-range(lhead.begin,rhead.begin-1) 
         + difference(\new-range(rhead.end+1,lhead.end)+ltail,rtail);

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return \new-range(lhead.begin,rhead.begin-1) + difference(ltail,r); 
    
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.begin > rhead.begin)
    return difference(arange(rhead.end+1,lhead.end)+ltail, rtail);

  throw "did not expect to end up here! <l> - <r>";
}

//test bool testComp() = complement(\achar-class([])) == \achar-class([range(1,16777215)]);
//test bool testComp2() = complement(\achar-class([range(0,0)])) == \achar-class([range(1,16777215)]);
//test bool testComp3() = complement(\achar-class([range(1,1)])) == \achar-class([range(2,16777215)]);
//test bool testComp4() = complement(\achar-class([range(10,20), range(30,40)])) == \achar-class([range(1,9),range(21,29),range(41,16777215)]);
//test bool testComp5() = complement(\achar-class([range(10,35), range(30,40)])) == \achar-class([range(1,9),range(41,16777215)]);
//
//test bool testUnion1() = union(\achar-class([range(10,20)]), \achar-class([range(30, 40)])) == \achar-class([range(10,20), range(30,40)]);
//test bool testUnion2() = union(\achar-class([range(10,25)]), \achar-class([range(20, 40)])) == \achar-class([range(10,40)]);
// 
//test bool testInter1() = intersection(\achar-class([range(10,20)]), \achar-class([range(30, 40)])) == \achar-class([]);
//test bool testInter2() = intersection(\achar-class([range(10,25)]), \achar-class([range(20, 40)])) == \achar-class([range(20, 25)]);
//
//test bool testDiff1() = difference(\achar-class([range(10,30)]), \achar-class([range(20,25)])) == \achar-class([range(10,19), range(26,30)]);
//test bool testDiff2() = difference(\achar-class([range(10,30), range(40,50)]), \achar-class([range(25,45)])) ==\achar-class( [range(10,24), range(46,50)]);

public AType cc2ranges(Class cc) {
   switch(cc) {
     case Class::\simpleCharclass(Range* ranges) : return \new-char-class([range(r) | r <- ranges]);
     case Class::\bracket(Class c): return cc2ranges(c);
     case Class::\complement(Class c) : return complement(cc2ranges(c));
     case Class::\intersection(Class l, Class r) : return intersection(cc2ranges(l), cc2ranges(r));
     case Class::\union(Class l, Class r): return union(cc2ranges(l), cc2ranges(r));
     case Class::\difference(Class l, Class r): return difference(cc2ranges(l), cc2ranges(r));
     default: throw "cc2ranges, missed a case <cc>";
   }
}
      
private ACharRange range(Range r) {
  switch (r) {
    case character(Char c) : return arange(charToInt(c),charToInt(c));
    case fromTo(Char l1, Char r1) : {
      <cL,cR> = <charToInt(l1),charToInt(r1)>;
      // users may flip te ranges, but after this reversed ranges will results in empty ranges
      return cL <= cR ? arange(cL, cR) : arange(cR, cL);
    } 
    default: throw "range, missed a case <r>";
  }
} 
 
private int charToInt(Char c) {
  switch (c) {
    case [Char] /^<ch:[^"'\-\[\]\\\>\< ]>/        : return charAt(ch, 0); 
    case [Char] /^\\n/ : return charAt("\n", 0);
    case [Char] /^\\t/ : return charAt("\t", 0);
    case [Char] /^\\b/ : return charAt("\b", 0);
    case [Char] /^\\r/ : return charAt("\r", 0);
    case [Char] /^\\f/ : return charAt("\f", 0);
    case [Char] /^\\\>/ : return charAt("\>", 0);
    case [Char] /^\\\</ : return charAt("\<", 0);
    case [Char] /^\\<esc:["'\-\[\]\\ ]>/        : return charAt(esc, 0);
    case [Char] /^\\u<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return toInt("0x<hex>");
    case [Char] /^\\U<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return toInt("0x<hex>");
    case [Char] /^\\a<hex:[0-7][0-9a-fA-F]>/ : return toInt("0x<hex>");
    default: throw "charToInt, missed a case <c>";
  }
}
