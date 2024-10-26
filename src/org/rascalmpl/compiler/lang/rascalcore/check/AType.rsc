@bootstrapParser
module lang::rascalcore::check::AType

/*
    Every needed to operate on ATypes, including asubtype, alub,
*/

extend lang::rascalcore::check::ATypeBase;
extend lang::rascalcore::grammar::definition::Characters;

import Node;
import Set;
//import IO;

// ---- asubtype --------------------------------------------------------------

@doc{
.Synopsis
Subtype on types.
}
//bool asubtype(type[&T] t, type[&U] u) { throw "asubtype not yet implemented on <t> and <u>"; } //asubtype(t.symbol, u.symbol);

@doc{
.Synopsis
This function documents and implements the subtype relation of Rascal's type system.
}

bool asubtype(tvar(s), AType r) {
    throw TypeUnavailable();
}
default bool asubtype(AType l, AType r){
    switch(r){
        case l:
            return true;
        case tvar(_):
            throw TypeUnavailable();
        case overloadedAType(overloads):
            //TODO: Alternative: return asubtype(l, (\avoid() | alub(it, tp) | <_, _, tp> <- overloads));
           return isEmpty(overloads) ? asubtype(l, avoid()) : any(<_, _, tp> <- overloads, asubtype(l, tp));
        case avalue():
            return true;
        case anum():
            return l is aint || l is areal || l is arat || l is anum;
        case aalias(str _, list[AType] _, AType aliased):
            return asubtype(l, aliased);
        case \start(AType t):
            return asubtype(l, t);
        case aprod(p):
            return asubtype(l, p.def);
        case \iter(AType t):
            return asubtype(l, t);
        case \iter-star(AType t):
            return asubtype(l, t);
        case \iter-seps(AType t, list[AType] _):
            return asubtype(l,t);// && isEmpty(removeLayout(seps));
        case  \iter-star-seps(AType t, list[AType] _):
            return  asubtype(l,t);// && isEmpty(removeLayout(seps));
        case conditional(AType t, _):
            return asubtype(l, t);
        case \seq(list[AType] rl):
            return \seq(list[AType] ll) := l && asubtypeList(ll, rl);
        case p2:aparameter(str _, AType b2): {
                res = false;
                if(l is aparameter){
                    res = asubtypeParam(l, r);
                } else {
                    res = p2.closed ? (equivalent(l, b2) || l is avoid) : asubtype(l, b2);     // [S6, S8]
                }
                //println("asubtype(<l>, <r>) =\> <res>");
                return res;
            }
        case areified(AType t):
            return asubtype(l, t);
    }
    if(l.alabel? || r.alabel?)
        return asubtype(unset(l, "alabel") , unset(r, "alabel"));
    else
        return l == r;
}

bool asubtype(p1:aparameter(str n1, AType b1), AType r)
    = asubtypeParam(p1, r);

// From "Exploring Type Parameters:
// aparameter, open
bool asubtypeParam(p1:aparameter(str n1, AType b1, closed=false), AType r) {
     res = false;
     if(aparameter(str _, AType _, closed=false) := r){                 // [S1]
        res = true;
     } else if(aparameter(str _, AType b2, closed=true) := r){          // [S3]
        res = asubtype(p1, b2);
      } else {                                                          // [S5]
        res = asubtype(b1, r);
     }
     //println("asubtype(<p1>, <r>) =\> <res>");
     return res;
 }

// aparameter, closed
bool asubtypeParam(p1:aparameter(str n1, AType b1, closed=true), AType r) {
    res = false;
    if(aparameter(str n2, AType _, closed=true) := r ){                  // [S2]
        res = n1 == n2;
    } else if(p2:aparameter(str _, AType _,closed=false) := r){// [S4]
        res = asubtype(b1, p2);
    } else {                                                              // [S7]
        res = asubtype(b1, r);
    }
    //println("asubtype(<p1>, <r>) =\> <res>");
    return res;
}

bool asubtype(overloadedAType(overloads), AType r)
    //TODO: Alternative: = asubtype((\avoid() | alub(it, tp) | <_, _, tp> <- overloads), r);
    = isEmpty(overloads) ? asubtype(avoid(), r) : any(<_, _, tp> <- overloads, asubtype(tp, r));

bool asubtype(avoid(), AType _) = true;
default bool asubtype(AType _, avalue()) = true;

bool asubtype(ac:acons(AType a, list[AType] ap, list[Keyword] _), AType b){
    switch(b){
        case acons(a,list[AType] bp, list[Keyword] _):
             return comparableList(ap, bp);
        case adt: aadt(str _, list[AType] _, _):
             return asubtype(a,adt);
        case afunc(a,list[AType] bp, list[Keyword] _):
             return comparableList(ap, bp);
        case anode(_):
             return true;
        case afunc(AType b, list[AType] bp, list[Keyword] _):
             return asubtype(a, b) && comparableList(ap, bp);
        case \start(AType t):
            return asubtype(ac, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(ap: aprod(AProduction p), AType b){
    switch(b){
        case aprod(AProduction q):
            return asubtype(p.def, q.def);
        case \start(AType t):
            return asubtype(ap, t);
        case conditional(AType t, _):
            return asubtype(ap, t);
        case AType t:
            return asubtype(p.def, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(adt:aadt(str n, list[AType] l, SyntaxRole sr), AType b){
    switch(b){
        case anode(_):
            return true;
        case acons(AType a, list[AType] _, list[Keyword] _):
            return asubtype(adt, a);
        case aadt(n, list[AType] r, _):
            return asubtypeList(l, r);
        case aadt("Tree", _, _):
            if(isConcreteSyntaxRole(sr)) return true;
        case \start(AType t):
            if(isConcreteSyntaxRole(sr)) return asubtype(adt, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(\start(AType a), AType b) = asubtype(a, b);

bool asubtype(i:\iter(AType s), AType b){
    switch(b){
        case aadt("Tree", [], dataSyntax()):
            return true;
        case anode(_):
            return true;
        case iter(AType t):
            return asubtype(s, t);
        case \iter-star(AType t):
            return asubtype(s, t);
        case \iter-seps(AType t, list[AType] seps):
            return asubtype(s,t) && isEmpty(removeLayout(seps));
        case \iter-star-seps(AType t, list[AType] seps):
            return asubtype(s,t) && isEmpty(removeLayout(seps));
        case \start(AType t):
            return asubtype(i, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(i:\iter-seps(AType s, list[AType] seps), AType b){
    switch(b){
        case aadt("Tree", [], dataSyntax()):
            return true;
        case anode(_):
            return true;
        case \iter-seps(AType t, list[AType] seps2):
            return asubtype(s,t) && asubtypeList(removeLayout(seps), removeLayout(seps2));
        case \iter(AType t):
            return asubtype(s,t) && isEmpty(removeLayout(seps));
        case  \iter-star(AType t):
            return asubtype(s,t) && isEmpty(removeLayout(seps));
        case \iter-star-seps(AType t, list[AType] seps2):
            return asubtype(s,t) && asubtypeList(removeLayout(seps), removeLayout(seps2));
        case \start(AType t):
            return asubtype(i, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(i:\iter-star(AType s), AType b){
    switch(b){
        case aadt("Tree", [], dataSyntax()):
            return true;
        case  anode(_):
            return true;
        case \iter-star(AType t):
            return asubtype(s, t);
        case \iter-star-seps(AType t, list[AType] seps):
            return asubtype(s,t) && isEmpty(removeLayout(seps));
        case \start(AType t):
            return asubtype(i, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(i:\iter-star-seps(AType s, list[AType] seps), AType b){
    switch(b){
        case aadt("Tree", [], dataSyntax()):
            return true;
        case anode(_):
            return true;
        case \iter-star-seps(AType t, list[AType] seps2):
            return asubtype(s,t) && asubtypeList(removeLayout(seps), removeLayout(seps2));
        case \start(AType t):
            return asubtype(i, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(\opt(AType _),aadt(n, [], dataSyntax())) = n == "Tree";
bool asubtype(\alt(set[AType] _),aadt(n, [], dataSyntax())) = n == "Tree";
bool asubtype(\seq(list[AType] _),aadt(n, [], dataSyntax())) = n == "Tree";

bool asubtype(\seq(list[AType] ll),\seq(list[AType] rl)) {
    return asubtypeList(ll, rl);
}

bool asubtype(alit(_), aadt(n, [], _)) = n == "Tree";
bool asubtype(acilit(_), aadt(n, [], _)) = n == "Tree";

bool asubtype(conditional(AType s, _), AType r /*conditional(AType t, _)*/) {
    if(conditional(AType t, _) := r)
        return asubtype(s, t);
     else
        return asubtype(s, r);
}

// TODO: add subtype for elements under optional and alternative, but that would also require auto-wrapping/unwrapping in the run-time
// bool asubtype(AType s, \opt(AType t)) = subtype(s,t);
// bool asubtype(AType s, \alt({AType t, *_}) = true when subtype(s, t); // backtracks over the alternatives

//bool asubtype(aint(), anum()) = true;
//bool asubtype(arat(), anum()) = true;
//bool asubtype(areal(), anum()) = true;

bool asubtype(atuple(AType l), atuple(AType r)) = asubtype(l, r);

// list and lrel
bool asubtype(alist(AType a), AType b){
    switch(b){
        case alist(AType t):
            return asubtype(a, t);
        case alrel(AType t):
            return asubtype(a, atuple(t));
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(alrel(AType a), AType b){
    switch(b){
        case alrel(AType t):
            return asubtype(a, t);
        case alist(AType t):
            return asubtype(atuple(a), t);
        case avalue():
            return true;
    }
    fail;
}

// set and rel

bool asubtype(aset(AType a),  AType b){
    switch(b){
        case aset(AType t):
            return asubtype(a, t);
        case arel(AType t):
            return asubtype(a, atuple(t));
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(arel(AType a), AType b){
    switch(b){
        case arel(AType t):
            return  asubtype(a,t);
        case aset(AType t):
            return asubtype(atuple(a), t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(abag(AType s), abag(AType t)) = asubtype(s, t);

bool asubtype(amap(AType from1, AType to1), amap(AType from2, AType to2))
    = asubtype(from1, from2) && asubtype(to1, to2);

bool asubtype(AType l:afunc(AType a, list[AType] ap, list[Keyword] _), AType r){
    switch(r){
        case acons(b,list[AType] bp, list[Keyword] _):
            return asubtype(a, b) && comparableList(ap, bp);
        case afunc(AType b, list[AType] bp, list[Keyword] _): {
                // note that comparability is enough for function argument sub-typing due to pattern matching semantics
                ares = asubtype(a,b);
                bres  = comparableList(ap, bp);
                //println("asubtype(<l>, <r>) =\> <ares>, <bres>");
                return ares && bres;
            }
         case avalue():
            return true;
    }
    fail;
}

// areified
bool asubtype(areified(AType s), AType b){
    switch(b){
        case areified(AType t):
            return asubtype(s,t);
        case anode(_):
            return true;
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(a:anode(list[AType] l), AType b){
    switch(b){
        case anode(_):
            return true;
        case anode(list[AType] r):
            return l <= r;
        case \start(t):
            return asubtype(a, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(AType a:aalias(str _, list[AType] _, AType aliased), AType r) = asubtype(aliased, r);
bool asubtype(AType l, aalias(str _, list[AType] _, AType aliased)) = asubtype(l, aliased);

// Character classes and char

bool asubtype(l:\achar-class(_), AType r){
    switch(r){
        case \achar-class(_): {
            if(l.ranges == r.ranges)
                return true;
            if(difference(r, l) == \achar-class([]))
                return false;
            return true;
        }
        case aadt("Tree", _, _):
            return true; // characters are Tree instances
        case \start(t):
            return asubtype(l, t);
        case avalue():
            return true;
    }
    fail;
}

bool asubtype(l:\achar-class(list[ACharRange] _), achar(int c)) = l == \achar-class([arange(c,c)]);

bool asubtype(achar(int c), \achar-class(list[ACharRange] ranges))
    = difference(ranges, [arange(c,c)]) == [arange(c,c)];

bool asubtype(atypeList(list[AType] l), atypeList(list[AType] r)) = asubtypeList(l, r);

// asubtype on lists of atypes

bool asubtypeList(list[AType] l, list[AType] r){
    if(size(l) == 0){
        return size(r) == 0;
    }
    return size(l) == size(r) && all(i <- index(l), asubtype(l[i], r[i]));
}

list[AType] removeLayout(list[AType] seps) = [ s | s <- seps, !isLayoutAType(s) ];

// This is a clone from ATypeUtils to solve circularity;
@doc{Synopsis: Determine if the given type is a layout type.}
bool isLayoutAType(aparameter(_,AType tvb)) = isLayoutAType(tvb);

bool isLayoutAType(\conditional(AType ss,_)) = isLayoutAType(ss);
bool isLayoutAType(t:aadt(adtName,_,SyntaxRole sr)) = sr == layoutSyntax();
bool isLayoutAType(\start(AType ss)) = isLayoutAType(ss);
bool isLayoutAType(\iter(AType s)) = isLayoutAType(s);
bool isLayoutAType(\iter-star(AType s)) = isLayoutAType(s);
bool isLayoutAType(\iter-seps(AType s,_)) = isLayoutAType(s);
bool isLayoutAType(\iter-star-seps(AType s,_)) = isLayoutAType(s);

bool isLayoutAType(\opt(AType s)) = isLayoutAType(s);
bool isLayoutAType(\alt(set[AType] alts)) = any(a <- alts, isLayoutAType(a));
bool isLayoutAType(\seq(list[AType] symbols)) = all(s <- symbols, isLayoutAType(s));
default bool isLayoutAType(AType _) = false;

@doc{
.Synopsis
Check if two types are comparable, i.e., have a common supertype.
}
bool comparable(AType s, AType t)
    = s == t || asubtype(s,t) || asubtype(t,s);

bool comparableList(list[AType] l, list[AType] r) {
    if(size(l) == 0){
        return size(r) == 0;
    }
    //for(i <- index(l)){
    //    b = comparable(l[i], r[i]);
    //    println("comparableList <i>: <l[i]>, <r[i]> =\> <b>");
    //}
    return size(l) == size(r) && all(i <- index(l), comparable(l[i], r[i]));
}

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

// ---- alub: least-upper-bound ------------------------------------------------

//int size(atypeList(list[AType] l)) = size(l);

@doc{
.Synopsis
The least-upperbound (lub) between two types.

.Description
This function documents and implements the lub operation in Rascal's type system.
}
AType alub(tvar(s), AType r) {
    throw TypeUnavailable();
}
AType alub(AType l, tvar(s)) {
     throw TypeUnavailable();
}

AType alub(AType s, s) = s;
default AType alub(AType s, AType t)
    = (s.alabel? || t.alabel?) ? ((s.alabel == t.alabel)  ? alub(unset(s, "alabel") , unset(t, "alabel"))[alabel=s.alabel]
                                                          : alub(unset(s, "alabel"), unset(t, "alabel")))
                             : avalue();

AType alub(atypeList(ts1), atypeList(ts2)) = atypeList(alubList(ts1, ts2));

AType alub(overloadedAType(overloads), AType t2)
   // TODO:Alternative: alub((\avoid() | alub(it, tp) | <_, _, tp> <- overloads), t2);
   = isEmpty(overloads) ? t2 : alub((avalue() | aglb(it, tp) | <_, _, tp> <- overloads), t2);

AType alub(AType t1, overloadedAType(overloads))
    //TODO: Alternative: = alub(t1, (\avoid() | alub(it, tp) | <_, _, tp> <- overloads));
    = isEmpty(overloads) ? t1 : alub(t1, (avalue() | aglb(it, tp)  | <_, _, tp> <- overloads));

AType alub(avalue(), AType t) = avalue();
AType alub(AType s, avalue()) = avalue();
AType alub(avoid(), AType t)  = t;
AType alub(AType s, avoid())  = s;
AType alub(aint(), anum())    = anum();
AType alub(aint(), areal())   = anum();
AType alub(aint(), arat())    = anum();
AType alub(arat(), anum())    = anum();
AType alub(arat(), areal())   = anum();
AType alub(arat(), aint())    = anum();
AType alub(areal(), anum())   = anum();
AType alub(areal(), aint())   = anum();
AType alub(areal(), arat())   = anum();
AType alub(anum(), aint())    = anum();
AType alub(anum(), areal())   = anum();
AType alub(anum(), arat())    = anum();

AType alub(aset(AType s), aset(AType t)) = aset(alub(s, t));

AType alub(aset(AType s), arel(AType t)) = aset(alub(s,atuple(t)));
AType alub(arel(AType s), aset(AType t)) = aset(alub(atuple(s), t));

AType alub(arel(atypeList(list[AType] l)), arel(atypeList(list[AType] r)))  = size(l) == size(r) ? arel(atypeList(alubList(l, r))) : aset(avalue());

AType alub(alist(AType s), alist(AType t)) = alist(alub(s, t));
AType alub(alist(AType s), alrel(AType t)) = alist(alub(s,atuple(t)));
AType alub(alrel(AType s), alist(AType t)) = alist(alub(atuple(s),t));

AType alub(alrel(atypeList(list[AType] l)), alrel(atypeList(list[AType] r)))  = size(l) == size(r) ? alrel(atypeList(alubList(l, r))) : alist(avalue());

AType alub(atuple(atypeList(list[AType] l)), atuple(atypeList(list[AType] r))) = size(l) == size(r) ? atuple(atypeList(alubList(l, r))) : avalue();

AType alub(amap(ld, lr), amap(rd, rr)) = amap(alub(ld, rd), alub(lr, rr));

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
    if(size(lp) == size(rp)){
        return acons(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(acons(AType lr, list[AType] lp, list[Keyword] lkw), afunc(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(afunc(AType lr, list[AType] lp, list[Keyword] lkw), acons(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO how do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(acons(AType a,  list[AType] lp, list[Keyword] _), a2:aadt(str n, list[AType] rp, SyntaxRole _)) = alub(a,a2);
AType alub(acons(AType _,  list[AType] _,  list[Keyword] _), anode(_)) = anode([]);

AType alub(anode(list[AType] l), anode(list[AType] r)) = anode(l & r);

AType alub(aalias(str _, list[AType] _, AType aliased), AType r) = alub(aliased, r);
AType alub(AType l, aalias(str _, list[AType] _, AType aliased)) = alub(l, aliased);

// From "Exploring Type Parameters (adapted to keep aparameter as long as possible)
AType alub(p1:aparameter(n1, b1,closed=false), aparameter(n2, b2,closed=false)) = n1 == n2 && b1 == gl ? p1 : gl when gl := aglb(b1, b2);
AType alub(p:aparameter(n1, b1,closed=true), aparameter(n2, b2, closed=true)) = n1 == n2 ? aparameter(n1, aglb(b1, b2),closed=true)
                                                              : lb == b1 ? p : lb when lb := alub(b1, b2);

AType alub(p1:aparameter(n1, b1,closed=false), p2:aparameter(n2, b2,closed=true)) = n1 == n2 && lb == b1 ? p1 : lb when lb := alub(b1, p2);
AType alub(p1:aparameter(n1, b1, closed=true), p2:aparameter(n2, b2, closed=false)) = n1 == n2 && lb == b2 ? p2 : lb when lb := alub(p1, b2);

AType alub(p:aparameter(n, b, closed=false), AType r) = lb == b ? p : lb when !(r is aparameter), lb := alub(b, r);
AType alub(AType l, p:aparameter(n, b,closed=false))  = lb == b ? p : lb when !(l is aparameter), lb := alub(l, b);
AType alub(p:aparameter(n, b, closed=true), AType r)  = lb == b ? p : lb when !(r is aparameter), lb := alub(b, r);
AType alub(AType l, p:aparameter(n, b, closed=true))  = lb == b ? p : lb when !(l is aparameter), lb := alub(l, b);

AType alub(areified(AType l), areified(AType r)) = areified(alub(l,r));
AType alub(areified(AType l), anode(_)) = anode([]);

AType alub(l:\achar-class(_), r:\achar-class(_)) = union(l, r);

AType alub(\iter(AType l), \iter(AType r)) = aadt("Tree", [], dataSyntax());
AType alub(\iter(AType l), \iter-star(AType r)) = aadt("Tree", [], dataSyntax());

AType alub(\iter-star(AType l), \iter-star(AType r)) = aadt("Tree", [], dataSyntax());
AType alub(\iter-star(AType l), \iter(AType r)) = aadt("Tree", [], dataSyntax());

AType alub(\iter-seps(_, _), \iter-seps(_,_)) = aadt("Tree", [], dataSyntax());
AType alub(\iter-seps(_,_), \iter-star-seps(AType r,_)) = aadt("Tree", [], dataSyntax());

AType alub(\iter-star-seps(AType l, _), \iter-star-seps(AType r, _)) = aadt("Tree", [], dataSyntax());
AType alub(\iter-star-seps(AType l, _), \iter-seps(AType r, _)) = aadt("Tree", [], dataSyntax());

AType alub(l:aadt("Tree", _, _), AType r) = l
    when r is \achar-class || r is seq || r is opt || r is alt || r is iter || r is \iter-star || r is \iter-seps || r is \iter-star-seps;
AType alub(AType l, r:aadt("Tree", _, _)) = r
    when l is \achar-class || l is seq || l is opt || l is alt || l is iter || l is \iter-star || l is \iter-seps || l is \iter-star-seps;

AType alub(\start(AType l) , AType r) = alub(l, r);
AType alub(AType l, \start(AType r)) = alub(l, r);

AType alub(conditional(AType l, _), AType r) = alub(l, r);
AType alub(AType l, conditional(AType r, _)) = alub(l, r);

AType alub(\seq(list[AType] ll),\seq(list[AType] rl))
=   \seq(alubList(ll, rl));

// because functions _match_ their parameters, parameter types may be comparable (co- and contra-variant) and not
// only contra-variant. We choose the lub here over aglb (both would be correct), to
// indicate to the programmer the intuition that rather _more_ than fewer functions are substitutable.
AType alub(afunc(AType lr, list[AType] lp, list[Keyword] lkw), afunc(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO how do we want to propagate the keyword parameters?
    } else
        return avalue();
}

public list[AType] alubList(list[AType] l, list[AType] r) = [alub(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r);
default list[AType] alubList(list[AType] l, list[AType] r) = [avalue()];

private list[str] getTypeParamNames(list[AType] l) = [ s | li <- l, aparameter(s,_) := li];

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

// aglb

public AType aglb(AType s, s) = s;

public default AType aglb(AType s, AType t)
    = (s.alabel? || t.alabel?) ? ((s.alabel == t.alabel)  ? aglb(unset(s, "alabel") , unset(t, "alabel"))[alabel=s.alabel]
                                                          : aglb(unset(s, "alabel"), unset(t, "alabel")))
                               : avoid();

public AType aglb(avoid(), AType t)  = avoid();
public AType aglb(AType s, avoid())  = avoid();
public AType aglb(avalue(), AType t) = t;
public AType aglb(AType s, avalue()) = s;

public AType aglb(aint(), anum())    = aint();
public AType aglb(anum(), aint())    = aint();
public AType aglb(arat(),anum())     = arat();
public AType aglb(anum(), arat())    = arat();
public AType aglb(areal(), anum())   = areal();
public AType aglb(anum(), areal())   = areal();

public AType aglb(aset(AType s), aset(AType t)) = aset(aglb(s, t));
public AType aglb(aset(AType s), arel(AType t)) = aset(aglb(s,atuple(t)));
public AType aglb(arel(AType s), aset(AType t)) = aset(aglb(atuple(s), t));

AType aglb(arel(atypeList(list[AType] l)), arel(atypeList(list[AType] r)))  = size(l) == size(r) ? arel(atypeList(aglbList(l, r))) : aset(avalue());

AType aglb(overloadedAType(overloads), AType t2)
    //TODO: Alternative: = aglb((\avoid() | alub(it, tp) | <_, _, tp> <- overloads), t2);
    = isEmpty(overloads) ? t2 : aglb((avoid() | alub(it, tp) | <_, _, tp> <- overloads), t2);

AType aglb(AType t1, overloadedAType(overloads))
    //TODO: Alternative: = aglb(t1, (\avoid() | alub(it, tp) | <_, _, tp> <- overloads));
    = isEmpty(overloads) ? t1 : aglb(t1, (avoid() | alub(it, tp)  | <_, _, tp> <- overloads));

public AType aglb(alist(AType s), alist(AType t)) = alist(aglb(s, t));
public AType aglb(alist(AType s), alrel(AType t)) = alist(aglb(s,atuple(t)));
public AType aglb(alrel(AType s), alist(AType t)) = alist(aglb(atuple(s), t));

AType aglb(alrel(atypeList(list[AType] l)), alrel(atypeList(list[AType] r))) = size(l) == size(r) ? alrel(atypeList(aglbList(l, r))) : alist(avalue());

AType aglb(atuple(atypeList(list[AType] l)), atuple(atypeList(list[AType] r))) = size(l) == size(r) ? atuple(atypeList(aglbList(l, r))) : avalue();

AType aglb(amap(ld, lr), amap(rd, rr)) = amap(aglb(ld, rd), aglb(lr, rr));

public AType aglb(abag(AType s), abag(AType t)) = abag(aglb(s, t));

public AType aglb(anode(list[AType] l), aadt(str _, list[AType] _, SyntaxRole _), SyntaxRole _) = anode(l);
public AType aglb(aadt(str _, list[AType] _, SyntaxRole _), anode(list[AType] r)) = anode(r);

AType aglb(a1:aadt(str n, list[AType] lp, SyntaxRole lsr), a2:aadt(n, list[AType] rp, SyntaxRole rsr))
    = addADTLabel(a1, a2, aadt(n, aglbList(lp,rp), sr))
      when size(lp) == size(rp) && getTypeParamNames(lp) == getTypeParamNames(rp) && size(getTypeParamNames(lp)) > 0 &&
           sr := overloadSyntaxRole({lsr, rsr}) && sr != illegalSyntax();

AType aglb(a1:aadt(str n, list[AType] lp, SyntaxRole lsr), a2:aadt(n, list[AType] rp, SyntaxRole rsr))
    = addADTLabel(a1, a2, aadt(n, aglbList(lp,rp), sr))
      when size(lp) == size(rp) && size(getTypeParamNames(lp)) == 0 &&
           sr := overloadSyntaxRole({lsr, rsr}) && sr != illegalSyntax();

AType aglb(aadt(str n, list[AType] lp, SyntaxRole _), aadt(str m, list[AType] rp,SyntaxRole _)) = anode([]) when n != m;
AType aglb(a1: aadt(str ln, list[AType] lp,SyntaxRole  _), acons(AType b, _, _)) = aglb(a1,b);

public AType aglb(acons(AType la, list[AType] _, list[Keyword] _), acons(AType ra, list[AType] _, list[Keyword] _)) = aglb(la,ra);
public AType aglb(acons(AType a, list[AType] lp, list[Keyword] _), aadt(str n, list[AType] rp, SyntaxRole sr)) = aglb(a,aadt(n,rp,sr));
public AType aglb(acons(AType _, list[AType] _, list[Keyword] _), anode(list[AType] r)) = anode(r);

public AType aglb(aalias(str _, list[AType] _, AType aliased), AType r) = aglb(aliased, r);
public AType aglb(AType l, aalias(str _, list[AType] _, AType aliased)) = aglb(l, aliased);

// From "Exploring Type Parameters (adapted to keep aparamater as long as possible)

AType aglb(p1:aparameter(n1, b1,closed=false), aparameter(n2, b2,closed=false)) = n1 == n2 && b1 == gl ? p1 : gl when gl := aglb(b1, b2);
AType aglb(aparameter(n1, b1,closed=true), aparameter(n2, b2,closed=true)) = n1 == n2 ? aparameter(n1,aglb(b1, b2),closed=true)
                                                              : avoid();

AType aglb(p1:aparameter(n1, b1,closed=false), p2:aparameter(n2, b2,closed=true)) = n1 == n2 && b1 == gl ? p1 : gl when gl := aglb(b1, p2);
AType aglb(p1:aparameter(n1, b1,closed=true), p2:aparameter(n2, b2,closed=false)) = n1 == n2 && b2 == gl ? p2 : gl when gl := aglb(p1, b2);

AType aglb(p:aparameter(n, b,closed=false), AType r)  = b == gl ? p : gl when !(r is aparameter), gl := aglb(b, r);
AType aglb(AType l, p:aparameter(n, b,closed=false))  = b == gl ? p : gl when !(l is aparameter), gl := aglb(l, b);

AType aglb(aparameter(n, b, closed=true), AType r)  = avoid() when !(r is aparameter);// upperbound!=lowerbound
AType aglb(AType l, aparameter(n, b,closed=true))  = avoid() when !(l is aparameter);

public AType aglb(areified(AType l), areified(AType r)) = areified(aglb(l,r));
public AType aglb(areified(AType l), anode(list[AType] r)) = anode(r);

public AType aglb(afunc(AType lr, list[AType] lp, list[Keyword] kwl), afunc(AType rr, list[AType] rp, list[Keyword] kwr)) {
    aglbReturn = aglb(lr,rr);
    aglbParams = alub(atuple(atypeList(lp)),atuple(atypeList(rp)));
    if (atuple(_) := aglbParams)
        return afunc(aglbReturn, aglbParams.formals, kwl == kwr ? kwl : []);
    else
        return avalue();
}

public list[AType] aglbList(list[AType] l, list[AType] r) = [aglb(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r);
public default list[AType] aglbList(list[AType] l, list[AType] r) = [avalue()];
