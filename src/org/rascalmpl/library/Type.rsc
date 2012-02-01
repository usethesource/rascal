@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@unfinished
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl}
@doc{
Synopsis: This is a module that reflects Rascal's type system, implemented in Rascal itself.

Description:  

The goal of this module is to provide:
  * reflection capabilities that are useful for deserialization and validation of data, and 
  * to provide the basic building blocks for syntax trees (see [ParseTree])

The following definition is built into Rascal:
  * <pre>data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);</pre>


The # operator will always produce a value of type[&T], where &T is bound to the type that was reified.

The following functions are provided on types:
<toc Rascal/Libraries/Prelude/Type 1>

Examples:
<screen>
#int
#rel[int,int]
data B = t();
#B
syntax A = "a";
#A;
type(\int(),())
</screen>     
    

}

module Type

import List;

@doc{
Synopsis: A Symbol represents a Rascal Type.
Description:
Symbols are values that represent Rascal's types. These are the atomic types.
We define:
* Atomic types.
* Labels that are used to give names to symbols, such as field names, constructor names, etc.
* Composite types.
* Parameters that represent a type variable.
}  
data Symbol                            //  Atomic types.
     =  \int()
     | \bool()
     | \real()
     | \rat()
     | \str()
     | \num()
     | \node()
     | \void()
     | \value()
     | \loc()
     | \datetime()
     ;
 
data Symbol                            // Labels
     = \label(str name, Symbol symbol)
     ;
  
data Symbol                            // Composite types.
     = \set(Symbol symbol)
     | \rel(list[Symbol] symbols)
     | \tuple(list[Symbol] symbols)
     | \list(Symbol symbol)
     | \map(Symbol from, Symbol to)
     | \bag(Symbol symbol)
     | \adt(str name, list[Symbol] parameters)
     | \cons(Symbol \adt, list[Symbol] parameters)
     | \alias(str name, list[Symbol] parameters, Symbol aliased)
     | \func(Symbol ret, list[Symbol] parameters)
     | \reified(Symbol symbol)
     ;

data Symbol
     = \parameter(str name, Symbol bound) // Parameter
     ;

@doc{
Synopsis: A production in a grammar or constructor in a data type.

Description:
Productions represent abstract (recursive) definitions of abstract data type constructors and functions.
}  
data Production
     = \cons(Symbol def, list[Symbol] symbols, set[Attr] attributes)
     | \func(Symbol def, list[Symbol] symbols, set[Attr] attributes)
     | \choice(Symbol def, set[Production] alternatives)
     ;

@doc{
  Attributes register additional semantics annotations of a definition. 
}
data Attr 
     = \tag(value \tag) 
     ;
  
public Symbol \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg) =
              \func(ret, parameters + \list(varArg));

// The following normalization rules canonicalize grammars to prevent arbitrary case distinctions later

@doc{
Synopsis: Choice between alternative productions.
Description:
Nested choice is flattened.
}
public Production choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})
  = choice(s, a+b);
  

@doc{Functions with variable argument lists are normalized to normal functions}
public Production \var-func(Symbol ret, str name, list[tuple[Symbol typ, str label]] parameters, Symbol varArg, str varLabel) =
       \func(ret, name, parameters + [<\list(varArg), varLabel>]);
       
public bool subtype(type[&T] t, type[&U] u) = subtype(t.symbol, u.symbol);

@doc{
  This function documents and implements the subtype relation of Rascal's type system. 
}
public bool subtype(Symbol s, s) = true;
public default bool subtype(Symbol s, Symbol t) = false;

public bool subtype(Symbol _, \value()) = true;
public bool subtype(\void(), Symbol _) = true;
public bool subtype(Symbol::\cons(Symbol a, list[Symbol] _), a) = true;
public bool subtype(\adt(str _, list[Symbol] _), \node()) = true;
public bool subtype(\adt(str n, list[Symbol] l), \adt(n, list[Symbol] r)) = subtype(l, r);
public bool subtype(\alias(str _, list[Symbol] _, Symbol aliased), Symbol r) = subtype(aliased, r);
public bool subtype(Symbol l, \alias(str _, Symbol aliased)) = subtype(l, aliased);
public bool subtype(\int(), \num()) = true;
public bool subtype(\rat(), \num()) = true;
public bool subtype(\real(), \num()) = true;
public bool subtype(\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = subtype(l, r);
public bool subtype(\rel(list[Symbol] l), \rel(list[Symbol] r)) = subtype(l, r);
public bool subtype(\list(Symbol s), \list(Symbol t)) = subtype(s, t);  
public bool subtype(\set(Symbol s), \set(Symbol t)) = subtype(s, t);  
public bool subtype(\bag(Symbol s), \bag(Symbol t)) = subtype(s, t);  
public bool subtype(\map(Symbol from1, Symbol to1), \map(Symbol from2, Symbol to2)) = subtype(from1, from2) && subtype(to1, to2);
public bool subtype(Symbol::\func(Symbol r1, list[Symbol] p1), Symbol::\func(Symbol r2, list[Symbol] p2)) = subtype(r1, r2) && subtype(p2, p1); // note the contra-variance of the argument types
public bool subtype(\parameter(str _, Symbol bound), Symbol r) = subtype(bound, r);
public bool subtype(Symbol l, \parameter(str _, Symbol bound)) = subtype(l, bound);
public bool subtype(\label(str _, Symbol s), Symbol t) = subtype(s,t);
public bool subtype(Symbol s, \label(str _, Symbol t)) = subtype(s,t);
public bool subtype(list[Symbol] l, list[Symbol] r) = all(i <- [0..size(l) - 1], subtype(l[i], r[i])) when size(l) == size(r);
public default bool subtype(list[Symbol] l, list[Symbol] r) = false;

//data Symbol 
//  | \func(Symbol ret, list[Symbol] parameters)
//  ;

@doc{
Synopsis: The least-upperbound (lub) between two types.

Description:
  This function documents and implements the lub operation in Rascal's type system. 
}
public Symbol lub(Symbol s, s) = s;
public default Symbol lub(Symbol s, Symbol t) = \value();

public Symbol lub(\value(), Symbol t) = \value();
public Symbol lub(Symbol s, \value()) = \value();
public Symbol lub(\void(), Symbol t) = t;
public Symbol lub(Symbol s, \void()) = s;
public Symbol lub(\int(), \num()) = \num();
public Symbol lub(\int(), \real()) = \num();
public Symbol lub(\int(), \rat()) = \num();
public Symbol lub(\rat(), \num()) = \num();
public Symbol lub(\rat(), \real()) = \num();
public Symbol lub(\rat(), \int()) = \num();
public Symbol lub(\real(), \num()) = \num();
public Symbol lub(\real(), \int()) = \num();
public Symbol lub(\real(), \rat()) = \num();
public Symbol lub(\num(), \int()) = \num();
public Symbol lub(\num(), \real()) = \num();
public Symbol lub(\num(), \rat()) = \num();

public Symbol lub(\set(Symbol s), \set(Symbol t)) = \set(lub(s, t));  
public Symbol lub(\set(Symbol _), \rel(list[Symbol] _)) = \set(\value());  
public Symbol lub(\rel(list[Symbol] _), \set(Symbol _)) = \set(\value());
public Symbol lub(\rel(list[Symbol] l), \rel(list[Symbol] r)) = \rel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) == getLabels(r);
public Symbol lub(\rel(list[Symbol] l), \rel(list[Symbol] r)) = \rel(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) != getLabels(r);
public Symbol lub(\rel(list[Symbol] l), \rel(list[Symbol] r)) = \rel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && noneLabeled(r);
public Symbol lub(\rel(list[Symbol] l), \rel(list[Symbol] r)) = \rel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(r))) when size(l) == size(r) && noneLabeled(l) && allLabeled(r);
public Symbol lub(\rel(list[Symbol] l), \rel(list[Symbol] r)) = \rel(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && !allLabeled(l) && !allLabeled(r);
public Symbol lub(\rel(list[Symbol] l), \rel(list[Symbol] r)) = \set(\value()) when size(l) != size(r);
public Symbol lub(\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = \tuple(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) == getLabels(r);
public Symbol lub(\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = \tuple(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) != getLabels(r);
public Symbol lub(\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = \tuple(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && noneLabeled(r);
public Symbol lub(\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = \tuple(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(r))) when size(l) == size(r) && noneLabeled(l) && allLabeled(r);
public Symbol lub(\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = \tuple(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && !allLabeled(l) && !allLabeled(r);
public Symbol lub(\list(Symbol s), \list(Symbol t)) = \list(lub(s, t));  
public Symbol lub(\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), \map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = \map(\label(lfl, lub(lf,rf)), \label(ltl, lub(lt,rt))) when lfl == rfl && ltl == rtl;
public Symbol lub(\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), \map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = \map(lub(lf,rf), lub(lt,rt)) when lfl != rfl || ltl != rtl;
public Symbol lub(\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), \map(Symbol rf, Symbol rt)) = \map(\label(lfl, lub(lf,rf)), \label(ltl, lub(lt,rt))) when \label(_,_) !:= rf && \label(_,_) !:= rt;
public Symbol lub(\map(Symbol lf, Symbol lt), \map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = \map(\label(rfl, lub(lf,rf)), \label(rtl, lub(lt,rt))) when \label(_,_) !:= lf && \label(_,_) !:= lt;
public Symbol lub(\map(Symbol lf, Symbol lt), \map(Symbol rf, Symbol rt)) = \map(lub(lf,rf), lub(lt,rt)) when \label(_,_) !:= lf && \label(_,_) !:= lt && \label(_,_) !:= rf && \label(_,_) !:= rt;
public Symbol lub(\bag(Symbol s), \bag(Symbol t)) = \bag(lub(s, t));
public Symbol lub(\adt(str n, list[Symbol] _), \node()) = \node();
public Symbol lub(\node(), \adt(str n, list[Symbol] _)) = \node();
public Symbol lub(\adt(str n, list[Symbol] lp), \adt(n, list[Symbol] rp)) = \adt(n, addParamLabels(lub(lp,rp),getParamLabels(lp))) when size(lp) == size(rp) && getParamLabels(lp) == getParamLabels(rp);
public Symbol lub(\adt(str n, list[Symbol] lp), \adt(str m, list[Symbol] rp)) = \node() when n != m;
public Symbol lub(\adt(str ln, list[Symbol] lp), Symbol::\cons(Symbol b, list[Symbol] _)) = lub(\adt(ln,lp),b);
public Symbol lub(Symbol::\cons(Symbol la, list[Symbol] _), Symbol::\cons(Symbol ra, list[Symbol] _)) = lub(la,ra);
public Symbol lub(Symbol::\cons(Symbol a, list[Symbol] lp), \adt(str n, list[Symbol] rp)) = lub(a,\adt(n,rp));
public Symbol lub(Symbol::\cons(Symbol _, list[Symbol] _), \node()) = \node();
public Symbol lub(\alias(str _, list[Symbol] _, Symbol aliased), Symbol r) = lub(aliased, r);
public Symbol lub(Symbol l, \alias(str _, list[Symbol] _, Symbol aliased)) = lub(l, aliased);
public Symbol lub(\parameter(str _, Symbol bound), Symbol r) = lub(bound, r);
public Symbol lub(Symbol l, \parameter(str _, Symbol bound)) = lub(l, bound);
public Symbol lub(\reified(Symbol l), \reified(Symbol r)) = \reified(lub(l,r));
public Symbol lub(Symbol::\func(Symbol lr, list[Symbol] lp), Symbol::\func(Symbol rr, list[Symbol] rp)) = Symbol::\func(lub(lr,lp), lp) when subtype(lp,rp);
public Symbol lub(Symbol::\func(Symbol lr, list[Symbol] lp), Symbol::\func(Symbol rr, list[Symbol] rp)) = Symbol::\func(lub(lr,lp), rp) when subtype(rp,lp);
public Symbol lub(Symbol::\func(Symbol lr, list[Symbol] lp), Symbol::\func(Symbol rr, list[Symbol] rp)) = \value() when !subtype(lp,rp) && !subtype(rp,lp);

public list[Symbol] lub(list[Symbol] l, list[Symbol] r) = [lub(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r); 
public default list[Symbol] lub(list[Symbol] l, list[Symbol] r) = [\value()]; 

private bool allLabeled(list[Symbol] l) = all(i <- index(l), \label(_,_) := l[i]);
private bool noneLabeled(list[Symbol] l) = all(i <- index(l), \label(_,_) !:= l[i]);
private list[str] getLabels(list[Symbol] l) = [ s | li <- l, \label(s,_) := li ];
private list[Symbol] addLabels(list[Symbol] l, list[str] s) = [ \label(s[idx],l[idx]) | idx <- index(l) ] when size(l) == size(s);
private default list[Symbol] addLabels(list[Symbol] l, list[str] s) { throw "Length of symbol list <l> and label list <s> much match"; }
private list[Symbol] stripLabels(list[Symbol] l) = [ (\label(_,v) := li) ? v : li | li <- l ]; 

private list[str] getParamLabels(list[Symbol] l) = [ s | li <- l, \parameter(s,_) := li ];
private list[Symbol] addParamLabels(list[Symbol] l, list[str] s) = [ \parameter(s[idx],l[idx]) | idx <- index(l) ] when size(l) == size(s);
private default list[Symbol] addParamLabels(list[Symbol] l, list[str] s) { throw "Length of symbol list and label list much match"; } 

data Exception 
     = typeCastException(type[value] from, type[value] to);

public &T typeCast(type[&T] typ, value v) {
  if (&T x := v)
    return x;
  throw typeCastException(typeOf(v), typ);
}

@doc{
Synopsis: returns the dynamic type of a value as a reified type
Description: 

As opposed to the # operator, which produces the type of a value statically, this
function produces the dynamic type of a value, represented by a symbol.


Examples:
<screen>
import Type;
value x = 1;
typeOf(x)
</screen>

Pitfalls: 
  * Note that the typeOf function does not produce definitions, like the [Reify] operator does, 
since values may escape the scope in which they've been constructed leaving their contents possibly undefined.
}
@javaClass{org.rascalmpl.library.Type}
@reflect
public java Symbol typeOf(value v);

@doc{Tests to make sure a lub a == a.}
test bool lubIdent01() = lub(\int(), \int()) == \int();
test bool lubIdent02() = lub(\real(), \real()) == \real();
test bool lubIdent03() = lub(\num(), \num()) == \num();
test bool lubIdent04() = lub(\datetime(), \datetime()) == \datetime();
test bool lubIdent05() = lub(\str(), \str()) == \str();

@doc{Tests for numeric lubs.}
test bool lubNumeric01() = lub(\int(), \real()) == \num();
test bool lubNumeric02() = lub(\int(), \rat()) == \num();
test bool lubNumeric03() = lub(\int(), \num()) == \num();
test bool lubNumeric04() = lub(\real(), \int()) == \num();
test bool lubNumeric05() = lub(\real(), \num()) == \num();
test bool lubNumeric06() = lub(\real(), \rat()) == \num();
test bool lubNumeric07() = lub(\num(), \int()) == \num();
test bool lubNumeric08() = lub(\num(), \real()) == \num();
test bool lubNumeric09() = lub(\num(), \rat()) == \num();
test bool lubNumeric10() = lub(\rat(), \int()) == \num();
test bool lubNumeric11() = lub(\rat(), \real()) == \num();
test bool lubNumeric12() = lub(\rat(), \num()) == \num();

@doc{Tests for lubs of incomparable types.}
test bool lubIncomp01() = lub(\num(), \str()) == \value();
test bool lubIncomp02() = lub(\num(), \datetime()) == \value();
test bool lubIncomp03() = lub(\num(), \bool()) == \value();
test bool lubIncomp04() = lub(\num(), \node()) == \value();

@doc{Tests for lubs against parameters.}
test bool lubParam01() = lub(\num(), \parameter("t", \node())) == \value();
test bool lubParam02() = lub(\num(), \parameter("t", \int())) == \num();
test bool lubParam03() = lub(\int(), \parameter("t", \num())) == \num();

@doc{Tests lubs of tuples.}
test bool lubTuple01() = lub(\tuple([\label("f1",\int()),\label("f2",\bool())]),\tuple([\label("f1",\int()),\label("f2",\bool())])) == \tuple([\label("f1",\int()),\label("f2",\bool())]);
test bool lubTuple02() = lub(\tuple([\label("f1",\int()),\label("f2",\bool())]),\tuple([\label("f1",\int()),\label("f3",\bool())])) == \tuple([\int(),\bool()]);
test bool lubTuple03() = lub(\tuple([\label("f1",\int()),\label("f2",\bool())]),\tuple([\label("f1",\real()),\label("f2",\str())])) == \tuple([\label("f1",\num()),\label("f2",\value())]);


