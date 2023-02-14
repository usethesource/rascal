module lang::rascalcore::check::AType

extend lang::rascalcore::check::ATypeBase;
extend lang::rascalcore::grammar::definition::Characters;

import IO;
import Node;
import Set;

// ---- asubtype --------------------------------------------------------------

@doc{
.Synopsis
Subtype on types.
}       
bool asubtype(type[&T] t, type[&U] u) { throw "asubtype not yet implemented on <t> and <u>"; } //asubtype(t.symbol, u.symbol);

@doc{
.Synopsis
This function documents and implements the subtype relation of Rascal's type system. 
}

bool asubtype(AType s, s) = true;

//default bool asubtype(AType s, AType t) = (s.alabel? || t.alabel?) ? asubtype(unset(s, "alabel") , unset(t, "alabel")) : s == t;

default bool asubtype(AType s, AType t) {
    if(s.alabel? || t.alabel?) 
        return asubtype(unset(s, "alabel") , unset(t, "alabel")); 
    else 
        return s == t;
}

bool asubtype(tvar(s), AType r) { 
    //println("asubtype(tvar(<s>), <r>)");
    throw TypeUnavailable(); 
}
bool asubtype(AType l, tvar(s)) {
    //println("asubtype(<l> tvar(<s>))");
    throw TypeUnavailable(); 
}


bool asubtype(overloadedAType(overloads), AType r) = !isEmpty(overloads) && any(<_, _, tp> <- overloads, asubtype(tp, r));

bool asubtype(AType l, overloadedAType(overloads)) = !isEmpty(overloads) && any(<_, _, tp> <- overloads, asubtype(l, tp));

bool asubtype(AType _, avalue()) = true;

bool asubtype(avoid(), AType _) = true;

bool asubtype(anode(_), anode(_)) = true;
  
bool asubtype(acons(AType a, list[AType] ap, list[Keyword] _), acons(a,list[AType] bp, list[Keyword] _)) = asubtype(ap,bp);
bool asubtype(acons(AType a, list[AType] ap, list[Keyword] _), adt: aadt(str _, list[AType] _, _)) = asubtype(a,adt);
bool asubtype(acons(AType a, list[AType] ap, list[Keyword] _), afunc(a,list[AType] bp, list[Keyword] _)) = asubtype(ap,bp);
bool asubtype(acons(AType a, list[AType] ap, list[Keyword] _), anode(_)) = true;

bool asubtype(acons(a,list[AType] ap, list[Keyword] _), afunc(AType b, list[AType] bp, list[Keyword] _)) = asubtype(a, b) && comparable(ap, bp);
bool asubtype(afunc(AType a, list[AType] ap, list[Keyword] _), acons(b,list[AType] bp, list[Keyword] _)) = asubtype(a, b) && comparable(ap, bp);

bool asubtype(aprod(AProduction p), aprod(AProduction q)) {
    return asubtype(p.def, q.def);
}

bool asubtype(aprod(AProduction p), AType t) {
    return asubtype(p.def, t);
}

bool asubtype(aadt(str _, list[AType] _, _), anode(_)) = true;
bool asubtype(adt: aadt(str _, list[AType] _, _), acons(AType a, list[AType] ap, list[Keyword] _)) = asubtype(adt, a);
bool asubtype(aadt(str n, list[AType] l, _), aadt(n, list[AType] r, _)) = asubtype(l, r);
bool asubtype(aadt(_, _, sr), aadt("Tree", _, _)) = true when isConcreteSyntaxRole(sr);

bool asubtype(\start(AType a), AType b) = asubtype(a, b);
bool asubtype(AType a, \start(AType b)) = asubtype(a, b);

bool asubtype(AType::\iter-seps(AType s, list[AType] seps), AType::\iter-seps(AType t, list[AType] seps2)) = asubtype(s,t) && asubtype(removeLayout(seps), removeLayout(seps2));
bool asubtype(AType::\iter-seps(AType s, list[AType] seps), AType::\iter(AType t)) = asubtype(s,t) && isEmpty(removeLayout(seps));
bool asubtype(AType::\iter(AType s), AType::\iter-seps(AType t, list[AType] seps)) = asubtype(s,t) && isEmpty(removeLayout(seps));

bool asubtype(AType::\iter-seps(AType s, list[AType] seps), AType::\iter-star-seps(AType t, list[AType] seps2)) = asubtype(s,t) && asubtype(removeLayout(seps), removeLayout(seps2));
bool asubtype(AType::\iter(AType s), AType::\iter-star(AType t)) = asubtype(s, t);

bool asubtype(AType::\iter-star-seps(AType s, list[AType] seps), AType::\iter-star-seps(AType t, list[AType] seps2)) = asubtype(s,t) && asubtype(removeLayout(seps), removeLayout(seps2));
bool asubtype(AType::\iter-star-seps(AType s, list[AType] seps), AType::\iter-star(AType t)) = asubtype(s,t) && isEmpty(removeLayout(seps));
bool asubtype(AType::\iter-star(AType s), AType::\iter-star-seps(AType t, list[AType] seps)) = asubtype(s,t) && isEmpty(removeLayout(seps));

bool asubtype(AType::\iter(AType s), aadt("Tree", [], _)) = true;
bool asubtype(AType::\iter(AType s), anode(_)) = true;

bool asubtype(AType::\iter-star(AType s), aadt("Tree", [], _)) = true;
bool asubtype(AType::\iter-star(AType s), anode(_)) = true;

bool asubtype(AType::\iter-seps(AType s, list[AType] seps), aadt("Tree", [], _)) = true;
bool asubtype(AType::\iter-seps(AType s, list[AType] seps), anode(_)) = true;

bool asubtype(AType::\iter-star-seps(AType s, list[AType] seps), aadt("Tree", [], _)) = true;
bool asubtype(AType::\iter-star-seps(AType s, list[AType] seps), anode(_)) = true;

bool asubtype(AType::alit(_), aadt("Tree", [], _)) = true;
bool asubtype(AType::acilit(_), aadt("Tree", [], _)) = true;
bool asubtype(AType::\achar-class(_), aadt("Tree", [], _)) = true;

// TODO: add subtype for elements under optional and alternative, but that would also require auto-wrapping/unwrapping in the run-time
// bool subtype(AType s, \opt(AType t)) = subtype(s,t);
// bool subtype(AType s, \alt({AType t, *_}) = true when subtype(s, t); // backtracks over the alternatives

bool asubtype(aint(), anum()) = true;
bool asubtype(arat(), anum()) = true;
bool asubtype(areal(), anum()) = true;

bool asubtype(atuple(AType l), atuple(AType r)) = asubtype(l, r);

// list and lrel
bool asubtype(alist(AType s), alist(AType t)) = asubtype(s, t);
bool asubtype(alrel(AType l), alrel(AType r)) = asubtype(l, r);

bool asubtype(alist(AType s), alrel(AType r)) = asubtype(s, atuple(r));
bool asubtype(alrel(AType l), alist(AType r)) = asubtype(atuple(l), r);

// set and rel
bool asubtype(aset(AType s), aset(AType t)) = asubtype(s, t);
bool asubtype(arel(AType l), arel(AType r)) = asubtype(l, r);

bool asubtype(aset(AType s), arel(AType r)) = asubtype(s, atuple(r));
bool asubtype(arel(AType l), aset(AType r)) = asubtype(atuple(l), r);

bool asubtype(abag(AType s), abag(AType t)) = asubtype(s, t);  

bool asubtype(amap(AType from1, AType to1), amap(AType from2, AType to2)) 
    { return  asubtype(from1, from2) && asubtype(to1, to2);}
    
// note that comparability is enough for function argument sub-typing due to pattern matching semantics
bool asubtype(afunc(AType r1, list[AType] p1, list[Keyword] _), afunc(AType r2, list[AType] p2, list[Keyword] _))
    = asubtype(r1, r2) && comparable(p1, p2);

// aparameter
bool asubtype(aparameter(str pname1, AType bound1), AType r) =
     aparameter(str _, AType bound2) := r ? asubtype(bound1, bound2) : asubtype(bound1, r)
    when /aparameter(pname1,_) !:= r;
bool asubtype(AType l, r:aparameter(str pname2, AType bound2)) = 
    aparameter(str _, AType bound1) := l ? asubtype(bound1, bound2) : asubtype(l, bound2)
    when /aparameter(pname2,_) !:= l;

// areified
bool asubtype(areified(AType s), areified(AType t)) = asubtype(s,t);
bool asubtype(areified(AType s), anode(_)) = true;

bool asubtype(anode(list[AType] l), anode(list[AType] r)) = l <= r;

// Character classes and char
bool asubtype(l:\achar-class(_), r:\achar-class(_)) {
    if(l.ranges == r.ranges) return true;
    //println("asubtype: <l>, <r>");
    if(difference(r, l) == \achar-class([])) return false;
    return true;
    // TODO: original code (below) was not executed properly by Rascal interpreter:
    //return  l.ranges == r.ranges || (difference(r, l) != \achar-class([]));
}
bool asubtype(l:\achar-class(_), aadt("Tree", _, _)) = true; // characters are Tree instances 

bool asubtype(achar(int c), \achar-class(list[ACharRange] ranges)) {
    res = difference(ranges, [arange(c,c)]) == [arange(c,c)];
    return res;
}
bool asubtype(l:\achar-class(list[ACharRange] _), achar(int c)) = l == \achar-class([arange(c,c)]);

// Utilities

bool asubtype(atypeList(list[AType] l), atypeList(list[AType] r)) = asubtype(l, r);

bool asubtype(list[AType] l, list[AType] r) = all(i <- index(l), asubtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool asubtype(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;

bool asubtype(list[AType] l, list[AType] r) = all(i <- index(l), asubtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool asubtype(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;


list[AType] removeLayout(list[AType] seps) = [ s | s <- seps, !isLayoutType(s) ];

// This is a clone from ATypeUtils to solve circularity;
@doc{Synopsis: Determine if the given type is a layout type.}
bool isLayoutType(aparameter(_,AType tvb)) = isLayoutType(tvb);

bool isLayoutType(AType::\conditional(AType ss,_)) = isLayoutType(ss);
bool isLayoutType(t:aadt(adtName,_,SyntaxRole sr)) = sr == layoutSyntax();
bool isLayoutType(AType::\start(AType ss)) = isLayoutType(ss);
bool isLayoutType(AType::\iter(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\iter-star(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\iter-seps(AType s,_)) = isLayoutType(s);
bool isLayoutType(AType::\iter-star-seps(AType s,_)) = isLayoutType(s);

bool isLayoutType(AType::\opt(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\alt(set[AType] alts)) = any(a <- alts, isLayoutType(a));
bool isLayoutType(AType::\seq(list[AType] symbols)) = all(s <- symbols, isLayoutType(s));
default bool isLayoutType(AType _) = false;

@doc{
.Synopsis
Check if two types are comparable, i.e., have a common supertype.
}
bool comparable(AType s, AType t)
    = s == t || asubtype(s,t) || asubtype(t,s);

bool comparable(list[AType] l, list[AType] r) = all(i <- index(l), comparable(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool comparable(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;

bool outerComparable(AType l, AType r){
    return outerComparable1(l, r);
}

bool outerComparable1(AType l, l) = true;
bool outerComparable1(alist(_), alist(_)) = true;
bool outerComparable1(aset(_), aset(_)) = true;
bool outerComparable1(abag(_), abag(_)) = true;
bool outerComparable1(arel(atypeList(list[AType] ts1)), arel(atypeList(list[AType] ts2))) = size(ts1) == size(ts2);
bool outerComparable1(arel(_), aset(_)) = true;
bool outerComparable1(aset(_), arel(_)) = true;
bool outerComparable1(alrel(atypeList(list[AType] ts1)), alrel(atypeList(list[AType] ts2))) = size(ts1) == size(ts2);
bool outerComparable1(alrel(_), alist(_)) = true;
bool outerComparable1(atuple(atypeList(ts1)), atuple(atypeList(ts2))) = size(ts1) == size(ts2);
bool outerComparable1(amap(_,_), amap(_,_)) = true;

bool outerComparable1(f1:afunc(AType r1, list[AType] p1, list[Keyword] _), f2:afunc(AType r2, list[AType] p2, list[Keyword] _))
    = outerComparable(r1, r2) && (f1.varArgs ? (f2.varArgs ? outerComparable(p1, p2)
                                                           : outerComparable(p1[0..-1], p2))
                                             : (f2.varArgs ? outerComparable(p1, p2[0..-1])
                                                           : outerComparable(p1, p2)));              
    
bool outerComparable1(afunc(AType r1, list[AType] p1, list[Keyword] _), acons(AType r2, list[AType] p2, list[Keyword] _))
    = outerComparable(r1, r2) && outerComparable(p1, p2);
bool outerComparable1(acons(AType r1, list[AType] p1, list[Keyword] _), afunc(AType r2, list[AType] p2, list[Keyword] _))
    = outerComparable(r1, r2) && outerComparable(p1, p2);

bool outerComparable1(aparameter(str pname1, AType bound1), aparameter(str pname2, AType bound2)) 
    = outerComparable(bound1, bound2);

bool outerComparable1(aadt(str adtName1, list[AType] parameters1, SyntaxRole syntaxRole1),  areified(_)) = true;


default bool outerComparable1(AType l, AType r) {
    return comparable(l, r);
}

bool outerComparable(list[AType] l, list[AType] r) = all(i <- index(l), outerComparable(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool outerComparable(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;


@doc{
.Synopsis
Check if two types are equivalent.
}
bool equivalent(AType s, AType t) = asubtype(s,t) && asubtype(t,s);


@doc{
.Synopsis
Structural equality between values. 

.Description
The difference is that no implicit coercions are done between values of incomparable types, such as == does for
int, real and rat.

.Examples

[source,rascal-shell]
----
import Type;
1 == 1.0
eq(1,1.0)
----
}
@javaClass{org.rascalmpl.library.Type}
public java bool eq(value x, value y);

// ---- lub: least-upper-bound ------------------------------------------------

int size(atypeList(list[AType] l)) = size(l);

@doc{
.Synopsis
The least-upperbound (lub) between two types.

.Description
This function documents and implements the lub operation in Rascal's type system. 
}
AType alub(tvar(s), AType r) { 
    //println("alub(tvar(<s>), <r>)"); 
    throw TypeUnavailable(); 
} 
AType alub(AType l, tvar(s)) { 
    //println("alub(<l>, tvar(<s>))"); 
    throw TypeUnavailable(); 
}

AType alub(AType s, s) = s;
default AType alub(AType s, AType t)
    = (s.alabel? || t.alabel?) ? (s.alabel == t.alabel)  ? alub(unset(s, "alabel") , unset(t, "alabel"))[alabel=s.alabel]
                                                     : alub(unset(s, "alabel"), unset(t, "alabel"))
                             : avalue();

AType alub(atypeList(ts1), atypeList(ts2)) = atypeList(alubList(ts1, ts2));
AType alub(avalue(), AType t) = avalue();
AType alub(AType s, avalue()) = avalue();
AType alub(avoid(), AType t) = t;
AType alub(AType s, avoid()) = s;
AType alub(aint(), anum()) = anum();
AType alub(aint(), areal()) = anum();   // why not areal();
AType alub(aint(), arat()) = anum();      // why not arat();
AType alub(arat(), anum()) = anum();
AType alub(arat(), areal()) = anum();
AType alub(arat(), aint()) = anum();    // why not arat();
AType alub(areal(), anum()) = anum();
AType alub(areal(), aint()) = anum();   // why not areal();
AType alub(areal(), arat()) = anum();
AType alub(anum(), aint()) = anum();
AType alub(anum(), areal()) = anum();
AType alub(anum(), arat()) = anum();

AType alub(aset(AType s), aset(AType t)) = aset(alub(s, t)); 
 
AType alub(aset(AType s), arel(atypeList(list[AType] ts))) = aset(alub(s,atuple(atypeList(ts))));  
AType alub(arel(atypeList(list[AType] ts)), aset(AType s)) = aset(alub(s,atuple(atypeList(ts))));

AType alub(r1: arel(atypeList(list[AType] l)), r2:arel(atypeList(list[AType] r)))  = size(l) == size(r) ? arel(atypeList(alubList(l, r))) : aset(avalue());

AType alub(alist(AType s), alist(AType t)) = alist(alub(s, t));  
AType alub(alist(AType s), alrel(atypeList(list[AType] ts))) = alist(alub(s,atuple(atypeList(ts))));  
AType alub(alrel(atypeList(list[AType] ts)), alist(AType s)) = alist(alub(s,atuple(atypeList(ts))));

AType alub(lr1: alrel(atypeList(list[AType] l)), lr2:alrel(atypeList(list[AType] r)))  = size(l) == size(r) ? alrel(atypeList(alubList(l, r))) : alist(avalue());

AType alub(t1: atuple(atypeList(list[AType] l)), t2:atuple(atypeList(list[AType] r))) = size(l) == size(r) ? atuple(atypeList(alubList(l, r))) : atuple(avalue());

AType alub(m1: amap(ld, lr), m2: amap(rd, rr)) = amap(alub(ld, rd), alub(lr, rr));

AType alub(abag(AType s), abag(AType t)) = abag(alub(s, t));

AType alub(aadt(str n, list[AType] _, SyntaxRole_), anode(_))  = anode([]);
AType alub(anode(_), aadt(str n, list[AType] _, SyntaxRole _)) = anode([]);

AType alub(a1:aadt(str n, list[AType] lp, SyntaxRole lsr), a2:aadt(n, list[AType] rp, SyntaxRole rsr)) 
                                                = addADTLabel(a1, a2, aadt(n, alubList(lp,rp), sr))
                                                  when size(lp) == size(rp) && getTypeParamNames(lp) == getTypeParamNames(rp) && size(getTypeParamNames(lp)) > 0 &&
                                                       sr := overloadSyntaxRole({lsr, rsr}) && sr != illegalSyntax();
                                                                         
AType alub(a1:aadt(str n, list[AType] lp, SyntaxRole lsr), a2:aadt(n, list[AType] rp, SyntaxRole rsr)) 
                                                = addADTLabel(a1, a2, aadt(n, alubList(lp,rp), sr))
                                                  when size(lp) == size(rp) && size(getTypeParamNames(lp)) == 0 && sr := overloadSyntaxRole({lsr, rsr}) && sr != illegalSyntax();
                                                                         
AType alub(aadt(str n, list[AType] lp, SyntaxRole _), aadt(str m, list[AType] rp,SyntaxRole _)) = anode([]) when n != m;
AType alub(a1: aadt(str ln, list[AType] lp,SyntaxRole  _), acons(AType b, _, _)) = alub(a1,b);

AType addADTLabel(AType a1, AType a2, AType adt){
  if(a1.alabel? && a1.alabel == a2.alabel) adt = adt[alabel=a1.alabel];
  return adt;
}

//AType alub(acons(AType la, list[AType] _,  list[Keyword] _), acons(AType ra, list[AType] _, list[Keyword] _)) = alub(la,ra);
AType alub(acons(AType lr, list[AType] lp, list[Keyword] lkw), acons(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(/*lr == rr && */size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(acons(AType lr, list[AType] lp, list[Keyword] lkw), afunc(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp) && size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(afunc(AType lr, list[AType] lp, list[Keyword] lkw), acons(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp) && size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO how do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(acons(AType a,  list[AType] lp, list[Keyword] _), a2:aadt(str n, list[AType] rp, SyntaxRole _)) = alub(a,a2);
AType alub(acons(AType _,  list[AType] _,  list[Keyword] _), anode(_)) = anode([]);

AType alub(anode(list[AType] l), anode(list[AType] r)) = anode(l & r);

bool keepParams(aparameter(str s1, AType bound1), aparameter(str s2, AType bound2)) = s1 == s2 && equivalent(bound1,bound2);

AType alub(AType l:aparameter(str s1, AType bound1), AType r:aparameter(str s2, AType bound2)) = l when keepParams(l,r);
AType alub(AType l:aparameter(str s1, AType bound1), AType r:aparameter(str s2, AType bound2)) = alub(bound1,bound2) when !keepParams(l,r);
AType alub(aparameter(str _, AType bound), AType r) = alub(bound, r) when aparameter(_,_) !:= r; //!(isRascalTypeParam(r));
AType alub(AType l, aparameter(str _, AType bound)) = alub(l, bound) when aparameter(_,_) !:= l; //!(isRascalTypeParam(l));

AType alub(areified(AType l), areified(AType r)) = areified(alub(l,r));
AType alub(areified(AType l), anode(_)) = anode([]);

AType alub(l:\achar-class(_), r:\achar-class(_)) = union(l, r);
AType alub(l:aadt("Tree", _, _), \achar-class(_)) = l;
AType alub(\achar-class(_), r:aadt("Tree", _, _)) = r;
 
// TODO: missing lub of iter/iter-plus relation here.
// TODO: missing lub of aadt("Tree", _, _) with all non-terminal types such as seq, opt, iter

// because functions _match_ their parameters, parameter types may be comparable (co- and contra-variant) and not
// only contra-variant. We choose the lub here over glb (both would be correct), to
// indicate to the programmer the intuition that rather _more_ than fewer functions are substitutable.
AType alub(afunc(AType lr, list[AType] lp, list[Keyword] lkw), afunc(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO how do we want to propagate the keyword parameters?
    } else
        return avalue();
}

public list[AType] alubList(list[AType] l, list[AType] r) = [alub(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r); 
default list[AType] alubList(list[AType] l, list[AType] r) = [avalue()]; 

private list[str] getTypeParamNames(list[AType] l) = [ s | li <- l, aparameter(s,_) := li ];

@doc{Calculate the lub of a list of types.}
public AType lubList(list[AType] ts) {
    AType theLub = avoid();
    for (t <- ts) theLub = alub(theLub,t);
    return theLub;
}



//public set[AType] numericTypes = { aint(), areal(), arat(), anum() };

public bool comparableOrNum(AType l, AType r) {
    leftAsNum = visit(l) {
        case aint() => anum()
        case areal() => anum()
        case arat() => anum()
    };
    
    rightAsNum = visit(r) {
        case aint() => anum()
        case areal() => anum()
        case arat() => anum()
    };
    
    return comparable(l, r) || comparable(leftAsNum,rightAsNum);
}