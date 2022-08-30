@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@unfinished
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl}
@doc{
.Synopsis
Rascal's type system, implemented in Rascal itself.

.Description

The goal of this module is to provide:

*  reflection capabilities that are useful for deserialization and validation of data, and 
*  to provide the basic building blocks for syntax trees (see ((Prelude-ParseTree)))

The following definition is built into Rascal:
[source,rascal]
----
data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);
----

The `#` operator will always produce a value of `type[&T]`, where `&T` is bound to the type that was reified.

.Examples
[source,rascal-shell]
----
import Type;
#int
#rel[int,int]
data B = t();
#B
syntax A = "a";
#A;
type(\int(),())
----
    
The following functions are provided on types:
}

module Type

import List;

@doc{
.Synopsis
A Symbol represents a Rascal Type.

.Description
Symbols are values that represent Rascal's types. These are the atomic types.
We define here:

<1>  Atomic types.
<2> Labels that are used to give names to symbols, such as field names, constructor names, etc.
<3>  Composite types.
<4>  Parameters that represent a type variable.

In ((Prelude-ParseTree)), see ((ParseTree-Symbol)), 
Symbols will be further extended with the symbols that may occur in a ParseTree.
}  
data Symbol    // <1>
     = \int()
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
 
data Symbol     // <2>
     = \label(str name, Symbol symbol)
     ;
  
data Symbol      // <3>
     = \set(Symbol symbol)
     | \rel(list[Symbol] symbols)
     | \lrel(list[Symbol] symbols)
     | \tuple(list[Symbol] symbols)
     | \list(Symbol symbol)
     | \map(Symbol from, Symbol to)
     | \bag(Symbol symbol)
     | \adt(str name, list[Symbol] parameters)
     | \cons(Symbol \adt, str name, list[Symbol] parameters)
     | \alias(str name, list[Symbol] parameters, Symbol aliased)
     | \func(Symbol ret, list[Symbol] parameters, list[Symbol] kwTypes)
     | \overloaded(set[Symbol] alternatives)
     | \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg)
     | \reified(Symbol symbol)
     ;

data Symbol // <4>
     = \parameter(str name, Symbol bound) 
     ;

@doc{
.Synopsis
A production in a grammar or constructor in a data type.

.Description
Productions represent abstract (recursive) definitions of abstract data type constructors and functions:

* `cons`: a constructor for an abstract data type.
* `func`: a function.
* `choice`: the choice between various alternatives.
* `composition`: composition of two productions.

In ParseTree, see ((ParseTree-Production)), 
Productions will be further extended and will be used to represent productions in syntax rules.
}  
data Production
     = \cons(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, set[Attr] attributes)
     | \choice(Symbol def, set[Production] alternatives)
     | \composition(Production lhs, Production rhs)
     ;

@doc{
.Synopsis
Attributes register additional semantics annotations of a definition. 
}
data Attr 
     = \tag(value \tag) 
     ;

@doc{
.Synopsis
Transform a function with varargs (`...`) to a normal function with a list argument.
}
public Symbol \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg) =
              \func(ret, parameters + \list(varArg), []);

// The following normalization rules canonicalize grammars to prevent arbitrary case distinctions later

@doc{
.Synopsis
Normalize the choice between alternative productions.

.Description
Nested choice is flattened.
}
public Production choice(Symbol s, set[Production] choices) {
    if (!any(choice(Symbol _, set[Production] _)  <- choices)) {
        fail choice;
	} else {   
	    // TODO: this does not work in interpreter and typechecker crashes on it (both related to the splicing)
	    //return choice(s, { *(choice(Symbol t, set[Production] b) := ch ? b : {ch}) | ch <- choices });
	    changed = false;
	    new_choices = {};
        for (ch <- choices) {
            if (choice(Symbol _, set[Production] b) := ch) {
	    		    changed = true;
	    		    new_choices += b;
	    	    } else {
	    		    new_choices += ch;
	    	    }
	    }
	
	    if (changed) {
	       	return choice(s, new_choices);
	    }
	    else {
            fail choice;
	    }
    }
}

//TODO:COMPILER
//the above code replaces the following code for performance reasons in compiled code
//public Production choice(Symbol s, {*Production a, choice(Symbol t, set[Production] b)})
//  = choice(s, a+b);
  

@doc{Functions with variable argument lists are normalized to normal functions}
// TODO: What is this? Not sure why this is here...
//public Production \var-func(Symbol ret, str name, list[tuple[Symbol typ, str label]] parameters, Symbol varArg, str varLabel) =
//       \func(ret, name, parameters + [<\list(varArg), varLabel>]);

@doc{
.Synopsis
Subtype on types.
}       
public bool subtype(type[&T] t, type[&U] u) = subtype(t.symbol, u.symbol);

@doc{
.Synopsis
This function documents and implements the subtype relation of Rascal's type system. 
}
public bool subtype(Symbol s, s) = true;
public default bool subtype(Symbol s, Symbol t) = false;

public bool subtype(Symbol _, Symbol::\value()) = true;
public bool subtype(Symbol::\void(), Symbol _) = true;
public bool subtype(Symbol::\cons(Symbol a, _, list[Symbol] _), a) = true;
public bool subtype(Symbol::\cons(Symbol a, str name, list[Symbol] ap), Symbol::\cons(a,name,list[Symbol] bp)) = subtype(ap,bp);
public bool subtype(Symbol::\adt(str _, list[Symbol] _), Symbol::\node()) = true;
public bool subtype(Symbol::\adt(str n, list[Symbol] l), Symbol::\adt(n, list[Symbol] r)) = subtype(l, r);
public bool subtype(Symbol::\alias(str _, list[Symbol] _, Symbol aliased), Symbol r) = subtype(aliased, r);
public bool subtype(Symbol l, \alias(str _, list[Symbol] _, Symbol aliased)) = subtype(l, aliased);
public bool subtype(Symbol::\int(), Symbol::\num()) = true;
public bool subtype(Symbol::\rat(), Symbol::\num()) = true;
public bool subtype(Symbol::\real(), Symbol::\num()) = true;
public bool subtype(Symbol::\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = subtype(l, r);

// list and lrel
public bool subtype(Symbol::\list(Symbol s), Symbol::\list(Symbol t)) = subtype(s, t); 
public bool subtype(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = subtype(l, r);

// Potential alternative rules:
//public bool subtype(\list(Symbol s), \lrel(list[Symbol] r)) = subtype(s, (size(r) == 1) ? r[0] : \tuple(r));
//public bool subtype(\lrel(list[Symbol] l), \list(Symbol r)) = subtype((size(l) == 1) ? l[0] : \tuple(l), r);

public bool subtype(Symbol::\list(Symbol s), Symbol::\lrel(list[Symbol] r)) = subtype(s, Symbol::\tuple(r));
public bool subtype(Symbol::\lrel(list[Symbol] l), \list(Symbol r)) = subtype(Symbol::\tuple(l), r);

// set and rel
public bool subtype(Symbol::\set(Symbol s), Symbol::\set(Symbol t)) = subtype(s, t);
public bool subtype(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = subtype(l, r);

//Potential alternative rules:
//public bool subtype(\set(Symbol s), \rel(list[Symbol] r)) = subtype(s, (size(r) == 1) ? r[0] : \tuple(r));
//public bool subtype(\rel(list[Symbol] l), \set(Symbol r)) = subtype((size(l) == 1) ? l[0] : \tuple(l), r);

public bool subtype(Symbol::\set(Symbol s), Symbol::\rel(list[Symbol] r)) = subtype(s, Symbol::\tuple(r));
public bool subtype(Symbol::\rel(list[Symbol] l), Symbol::\set(Symbol r)) = subtype(Symbol::\tuple(l), r);

public bool subtype(Symbol::\bag(Symbol s), Symbol::\bag(Symbol t)) = subtype(s, t);  
public bool subtype(Symbol::\map(Symbol from1, Symbol to1), Symbol::\map(Symbol from2, Symbol to2)) = subtype(from1, from2) && subtype(to1, to2);
// TODO: this should be comparable parameters and we miss something about the kwparams:
public bool subtype(Symbol::\func(Symbol r1, list[Symbol] p1, list[Symbol] kw1), Symbol::\func(Symbol r2, list[Symbol] p2, list[Symbol] kw2)) = subtype(r1, r2) && subtype(p2, p1); 
public bool subtype(Symbol::\parameter(str _, Symbol bound), Symbol r) = subtype(bound, r);
public bool subtype(Symbol l, Symbol::\parameter(str _, Symbol bound)) = subtype(l, bound);
public bool subtype(Symbol::\label(str _, Symbol s), Symbol t) = subtype(s,t);
public bool subtype(Symbol s, Symbol::\label(str _, Symbol t)) = subtype(s,t);
public bool subtype(Symbol::\reified(Symbol s), Symbol::\reified(Symbol t)) = subtype(s,t);
public bool subtype(Symbol::\reified(Symbol s), Symbol::\node()) = true;
public bool subtype(list[Symbol] l, list[Symbol] r) = all(i <- index(l), subtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
public default bool subtype(list[Symbol] l, list[Symbol] r) = size(l) == 0 && size(r) == 0;

//public bool subtype(Symbol::\func(Symbol r1, list[Symbol] p1), Symbol::\var-func(Symbol r2, list[Symbol] p2, Symbol va)) =
//	subtype(r1,r2) && size(p1)-1 == size(p2) && subtype(p2,head(p1,size(p1)-1)) && subtype(\list(va),last(p1));
//
//public bool subtype(Symbol::\var-func(Symbol r1, list[Symbol] p1, Symbol va), Symbol::\func(Symbol r2, list[Symbol] p2)) {
//	if (subtype(r1,r2)) {
//		if (size(p1) <= size(p2) && subtype(head(p2,size(p1)),p1)) {
//			if (size(p2) > 0 && size(p2) > size(p1)) {
//				list[Symbol] theRest = tail(p2,size(p2)-size(p1));
//				list[Symbol] vaExp = [ va | idx <- index(theRest) ];
//				return subtype(theRest, vaExp);
//			}
//			return true;
//		}
//	}
//	return false;
//}
//
//public bool subtype(Symbol::\var-func(Symbol r1, list[Symbol] p1, Symbol va1), Symbol::\var-func(Symbol r2, list[Symbol] p2, Symbol va2)) {
//	if (subtype(r1,r2)) {
//		if (size(p1) == size(p2)) {
//			return subtype(p2+\list(va2), p1+\list(va1));
//		} else if (size(p1) == 0) {
//			return subtype(p2+\list(va2), [va1 | idx <- index(p2)] + \list(va1));
//		} else if (size(p2) == 0) {
//			return subtype([va2 | idx <- index(p1)] + \list(va2), p1 + \list(va1));
//		} else if (size(p1) < size(p2)) {
//			return subtype(take(size(p1),p2),p1) && subtype(tail(p2,size(p2)-size(p1))+\list(va2),[va1|idx<-index(tail(p2,size(p2)-size(p1)))]+\list(va1));
//		} else if (size(p2) < size(p1)) {
//			return subtype(p2,take(size(p2),p1)) && subtype([va2|idx <- index(tail(p1,size(p1)-size(p2)))]+\list(va2),tail(p1,size(p1)-size(p2))+\list(va1));
//		}
//	}
//	return false;
//}

@doc{
.Synopsis
Check if two types are comparable, i.e., have a common supertype.
}
public bool comparable(Symbol s, Symbol t) = subtype(s,t) || subtype(t,s); 

@doc{
.Synopsis
Check if two types are equivalent.
}
public bool equivalent(Symbol s, Symbol t) = subtype(s,t) && subtype(t,s);


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

@doc{
.Synopsis
The least-upperbound (lub) between two types.

.Description
This function documents and implements the lub operation in Rascal's type system. 
}
public Symbol lub(Symbol s, s) = s;
public default Symbol lub(Symbol s, Symbol t) = \value();

public Symbol lub(Symbol::\value(), Symbol t) = Symbol::\value();
public Symbol lub(Symbol s, Symbol::\value()) = Symbol::\value();
public Symbol lub(Symbol::\void(), Symbol t) = t;
public Symbol lub(Symbol s, Symbol::\void()) = s;
public Symbol lub(Symbol::\int(), Symbol::\num()) = Symbol::\num();
public Symbol lub(Symbol::\int(), Symbol::\real()) = Symbol::\num();
public Symbol lub(Symbol::\int(), Symbol::\rat()) = Symbol::\num();
public Symbol lub(Symbol::\rat(), Symbol::\num()) = Symbol::\num();
public Symbol lub(Symbol::\rat(), Symbol::\real()) = Symbol::\num();
public Symbol lub(Symbol::\rat(), Symbol::\int()) = Symbol::\num();
public Symbol lub(Symbol::\real(), Symbol::\num()) = Symbol::\num();
public Symbol lub(Symbol::\real(), Symbol::\int()) = Symbol::\num();
public Symbol lub(Symbol::\real(), Symbol::\rat()) = Symbol::\num();
public Symbol lub(Symbol::\num(), Symbol::\int()) = Symbol::\num();
public Symbol lub(Symbol::\num(), Symbol::\real()) = Symbol::\num();
public Symbol lub(Symbol::\num(), Symbol::\rat()) = Symbol::\num();

public Symbol lub(Symbol::\set(Symbol s), Symbol::\set(Symbol t)) = Symbol::\set(lub(s, t));  
public Symbol lub(Symbol::\set(Symbol s), Symbol::\rel(list[Symbol] ts)) = Symbol::\set(lub(s,Symbol::\tuple(ts)));  
public Symbol lub(Symbol::\rel(list[Symbol] ts), Symbol::\set(Symbol s)) = Symbol::\set(lub(s,Symbol::\tuple(ts)));

public Symbol lub(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) == getLabels(r);
public Symbol lub(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) != getLabels(r);
public Symbol lub(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && noneLabeled(r);
public Symbol lub(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(r))) when size(l) == size(r) && noneLabeled(l) && allLabeled(r);
public Symbol lub(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && !allLabeled(l) && !allLabeled(r);
public Symbol lub(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = \set(\value()) when size(l) != size(r);

public Symbol lub(Symbol::\list(Symbol s), Symbol::\list(Symbol t)) = Symbol::\list(lub(s, t));  
public Symbol lub(Symbol::\list(Symbol s), \lrel(list[Symbol] ts)) = Symbol::\list(lub(s,\tuple(ts)));  
public Symbol lub(Symbol::\lrel(list[Symbol] ts), Symbol::\list(Symbol s)) = Symbol::\list(lub(s,\tuple(ts)));

public Symbol lub(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) == getLabels(r);
public Symbol lub(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) != getLabels(r);
public Symbol lub(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && noneLabeled(r);
public Symbol lub(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(r))) when size(l) == size(r) && noneLabeled(l) && allLabeled(r);
public Symbol lub(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && !allLabeled(l) && !allLabeled(r);
public Symbol lub(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\list(\value()) when size(l) != size(r);

public Symbol lub(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) == getLabels(r);
public Symbol lub(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) != getLabels(r);
public Symbol lub(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && noneLabeled(r);
public Symbol lub(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(addLabels(lub(stripLabels(l), stripLabels(r)),getLabels(r))) when size(l) == size(r) && noneLabeled(l) && allLabeled(r);
public Symbol lub(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(lub(stripLabels(l), stripLabels(r))) when size(l) == size(r) && ! ( (allLabeled(l) && allLabeled(r)) || (allLabeled(l) && noneLabeled(r)) || (noneLabeled(l) && allLabeled(r)));

public Symbol lub(Symbol::\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), Symbol::\map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = Symbol::\map(\label(lfl, lub(lf,rf)), \label(ltl, lub(lt,rt))) when lfl == rfl && ltl == rtl;
public Symbol lub(Symbol::\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), Symbol::\map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = Symbol::\map(lub(lf,rf), lub(lt,rt)) when lfl != rfl || ltl != rtl;
public Symbol lub(Symbol::\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), Symbol::\map(Symbol rf, Symbol rt)) = Symbol::\map(\label(lfl, lub(lf,rf)), \label(ltl, lub(lt,rt))) when \label(_,_) !:= rf && \label(_,_) !:= rt;
public Symbol lub(Symbol::\map(Symbol lf, Symbol lt), Symbol::\map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = Symbol::\map(\label(rfl, lub(lf,rf)), \label(rtl, lub(lt,rt))) when \label(_,_) !:= lf && \label(_,_) !:= lt;
public Symbol lub(Symbol::\map(Symbol lf, Symbol lt), Symbol::\map(Symbol rf, Symbol rt)) = Symbol::\map(lub(lf,rf), lub(lt,rt)) when \label(_,_) !:= lf && \label(_,_) !:= lt && \label(_,_) !:= rf && \label(_,_) !:= rt;

public Symbol lub(Symbol::\bag(Symbol s), Symbol::\bag(Symbol t)) = Symbol::\bag(lub(s, t));
public Symbol lub(Symbol::\adt(str n, list[Symbol] _), Symbol::\node()) = Symbol::\node();
public Symbol lub(Symbol::\node(), \adt(str n, list[Symbol] _)) = Symbol::\node();
public Symbol lub(Symbol::\adt(str n, list[Symbol] lp), Symbol::\adt(n, list[Symbol] rp)) = Symbol::\adt(n, addParamLabels(lub(lp,rp),getParamLabels(lp))) when size(lp) == size(rp) && getParamLabels(lp) == getParamLabels(rp) && size(getParamLabels(lp)) > 0;
public Symbol lub(Symbol::\adt(str n, list[Symbol] lp), Symbol::\adt(n, list[Symbol] rp)) = Symbol::\adt(n, lub(lp,rp)) when size(lp) == size(rp) && size(getParamLabels(lp)) == 0;
public Symbol lub(Symbol::\adt(str n, list[Symbol] lp), Symbol::\adt(str m, list[Symbol] rp)) = Symbol::\node() when n != m;
public Symbol lub(Symbol::\adt(str ln, list[Symbol] lp), Symbol::\cons(Symbol b, _, list[Symbol] _)) = lub(Symbol::\adt(ln,lp),b);

public Symbol lub(Symbol::\cons(Symbol la, _, list[Symbol] _), Symbol::\cons(Symbol ra, _, list[Symbol] _)) = lub(la,ra);
public Symbol lub(Symbol::\cons(Symbol a, _, list[Symbol] lp), Symbol::\adt(str n, list[Symbol] rp)) = lub(a,Symbol::\adt(n,rp));
public Symbol lub(Symbol::\cons(Symbol _, _, list[Symbol] _), Symbol::\node()) = Symbol::\node();

public Symbol lub(Symbol::\alias(str _, list[Symbol] _, Symbol aliased), Symbol r) = lub(aliased, r);
public Symbol lub(Symbol l, \alias(str _, list[Symbol] _, Symbol aliased)) = lub(l, aliased);

public bool keepParams(Symbol::\parameter(str s1, Symbol bound1), Symbol::\parameter(str s2, Symbol bound2)) = s1 == s2 && equivalent(bound1,bound2);

public Symbol lub(Symbol l:Symbol::\parameter(str s1, Symbol bound1), Symbol r:Symbol::\parameter(str s2, Symbol bound2)) = l when keepParams(l,r);
public Symbol lub(Symbol l:Symbol::\parameter(str s1, Symbol bound1), Symbol r:Symbol::\parameter(str s2, Symbol bound2)) = lub(bound1,bound2) when !keepParams(l,r);
public Symbol lub(Symbol::\parameter(str _, Symbol bound), Symbol r) = lub(bound, r) when !(isTypeVar(r));
public Symbol lub(Symbol l, Symbol::\parameter(str _, Symbol bound)) = lub(l, bound) when !(isTypeVar(l));

public Symbol lub(Symbol::\reified(Symbol l), Symbol::\reified(Symbol r)) = Symbol::\reified(lub(l,r));
public Symbol lub(Symbol::\reified(Symbol l), Symbol::\node()) = Symbol::\node();

public Symbol lub(Symbol::\func(Symbol lr, list[Symbol] lp, list[Symbol] lkw), Symbol::\func(Symbol rr, list[Symbol] rp, list[Symbol] rkw)) {
	lubReturn = lub(lr,rr);
	lubParams = glb(Symbol::\tuple(lp),Symbol::\tuple(rp));
	
	// TODO: what is the real lub of the kwparams?
	if (isTupleType(lubParams))
		return \func(lubReturn, lubParams.symbols, lkw == rkw ? lkw : []);
	else
		return Symbol::\value();
}

public Symbol lub(Symbol::\label(_,Symbol l), Symbol r) = lub(l,r);
public Symbol lub(Symbol l, Symbol::\label(_,Symbol r)) = lub(l,r);

public list[Symbol] lub(list[Symbol] l, list[Symbol] r) = [lub(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r); 
public default list[Symbol] lub(list[Symbol] l, list[Symbol] r) = [\value()]; 

private bool allLabeled(list[Symbol] l) = all(li <- l, Symbol::\label(_,_) := li);
private bool noneLabeled(list[Symbol] l) = all(li <- l, Symbol::\label(_,_) !:= li);
private list[str] getLabels(list[Symbol] l) = [ s | li <- l, Symbol::\label(s,_) := li ];
private list[Symbol] addLabels(list[Symbol] l, list[str] s) = [ Symbol::\label(s[idx],l[idx]) | idx <- index(l) ] when size(l) == size(s);
private default list[Symbol] addLabels(list[Symbol] l, list[str] s) { throw "Length of symbol list <l> and label list <s> much match"; }
private list[Symbol] stripLabels(list[Symbol] l) = [ (Symbol::\label(_,v) := li) ? v : li | li <- l ]; 

private list[str] getParamLabels(list[Symbol] l) = [ s | li <- l, Symbol::\parameter(s,_) := li ];
private list[Symbol] addParamLabels(list[Symbol] l, list[str] s) = [ Symbol::\parameter(s[idx],l[idx]) | idx <- index(l) ] when size(l) == size(s);
private default list[Symbol] addParamLabels(list[Symbol] l, list[str] s) { throw "Length of symbol list and label list much match"; } 

@doc{
.Synopsis
The greatest lower bound (glb) between two types.

.Description
This function documents and implements the glb operation in Rascal's type system. 
}
public Symbol glb(Symbol s, s) = s;
public default Symbol glb(Symbol s, Symbol t) = \void();

public Symbol glb(Symbol::\void(), Symbol t) = Symbol::\void();
public Symbol glb(Symbol s, Symbol::\void()) = Symbol::\void();
public Symbol glb(Symbol::\value(), Symbol t) = t;
public Symbol glb(Symbol s, Symbol::\value()) = s;

public Symbol glb(Symbol::\int(), Symbol::\num()) = Symbol::\int();
public Symbol glb(Symbol::\num(), Symbol::\int()) = Symbol::\int();
public Symbol glb(Symbol::\rat(),Symbol::\num()) = Symbol::\rat();
public Symbol glb(Symbol::\num(), Symbol::\rat()) = Symbol::\rat();
public Symbol glb(Symbol::\real(), Symbol::\num()) = Symbol::\real();
public Symbol glb(Symbol::\num(), Symbol::\real()) = Symbol::\real();

public Symbol glb(Symbol::\set(Symbol s), Symbol::\set(Symbol t)) = Symbol::\set(glb(s, t));  
public Symbol glb(Symbol::\set(Symbol s), Symbol::\rel(list[Symbol] ts)) = Symbol::\set(glb(s,Symbol::\tuple(ts)));  
public Symbol glb(Symbol::\rel(list[Symbol] ts), Symbol::\set(Symbol s)) = \set(glb(s,Symbol::\tuple(ts)));

public Symbol glb(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) == getLabels(r);
public Symbol glb(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(glb(stripLabels(l), stripLabels(r))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) != getLabels(r);
public Symbol glb(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && noneLabeled(r);
public Symbol glb(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(r))) when size(l) == size(r) && noneLabeled(l) && allLabeled(r);
public Symbol glb(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = Symbol::\rel(glb(stripLabels(l), stripLabels(r))) when size(l) == size(r) && !allLabeled(l) && !allLabeled(r);
public Symbol glb(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = \set(\value()) when size(l) != size(r);

public Symbol glb(Symbol::\list(Symbol s), Symbol::\list(Symbol t)) = Symbol::\list(glb(s, t));  
public Symbol glb(Symbol::\list(Symbol s), Symbol::\lrel(list[Symbol] ts)) = Symbol::\list(glb(s,Symbol::\tuple(ts)));  
public Symbol glb(Symbol::\lrel(list[Symbol] ts), Symbol::\list(Symbol s)) = Symbol::\list(glb(s,Symbol::\tuple(ts)));

public Symbol glb(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) == getLabels(r);
public Symbol glb(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(glb(stripLabels(l), stripLabels(r))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) != getLabels(r);
public Symbol glb(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && noneLabeled(r);
public Symbol glb(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(r))) when size(l) == size(r) && noneLabeled(l) && allLabeled(r);
public Symbol glb(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\lrel(glb(stripLabels(l), stripLabels(r))) when size(l) == size(r) && !allLabeled(l) && !allLabeled(r);
public Symbol glb(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = Symbol::\list(\value()) when size(l) != size(r);

public Symbol glb(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) == getLabels(r);
public Symbol glb(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(glb(stripLabels(l), stripLabels(r))) when size(l) == size(r) && allLabeled(l) && allLabeled(r) && getLabels(l) != getLabels(r);
public Symbol glb(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(l))) when size(l) == size(r) && allLabeled(l) && noneLabeled(r);
public Symbol glb(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(addLabels(glb(stripLabels(l), stripLabels(r)),getLabels(r))) when size(l) == size(r) && noneLabeled(l) && allLabeled(r);
public Symbol glb(Symbol::\tuple(list[Symbol] l), Symbol::\tuple(list[Symbol] r)) = Symbol::\tuple(glb(stripLabels(l), stripLabels(r))) when size(l) == size(r) && !allLabeled(l) && !allLabeled(r);

public Symbol glb(Symbol::\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), Symbol::\map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = Symbol::\map(\label(lfl, glb(lf,rf)), \label(ltl, glb(lt,rt))) when lfl == rfl && ltl == rtl;
public Symbol glb(Symbol::\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), Symbol::\map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = Symbol::\map(glb(lf,rf), glb(lt,rt)) when lfl != rfl || ltl != rtl;
public Symbol glb(Symbol::\map(\label(str lfl, Symbol lf), \label(str ltl, Symbol lt)), Symbol::\map(Symbol rf, Symbol rt)) = Symbol::\map(\label(lfl, glb(lf,rf)), \label(ltl, glb(lt,rt))) when \label(_,_) !:= rf && \label(_,_) !:= rt;
public Symbol glb(Symbol::\map(Symbol lf, Symbol lt), Symbol::\map(\label(str rfl, Symbol rf), \label(str rtl, Symbol rt))) = Symbol::\map(\label(rfl, glb(lf,rf)), \label(rtl, glb(lt,rt))) when \label(_,_) !:= lf && \label(_,_) !:= lt;
public Symbol glb(Symbol::\map(Symbol lf, Symbol lt), Symbol::\map(Symbol rf, Symbol rt)) = Symbol::\map(glb(lf,rf), glb(lt,rt)) when \label(_,_) !:= lf && \label(_,_) !:= lt && \label(_,_) !:= rf && \label(_,_) !:= rt;

public Symbol glb(Symbol::\bag(Symbol s), Symbol::\bag(Symbol t)) = Symbol::\bag(glb(s, t));
public Symbol glb(Symbol::\adt(str n, list[Symbol] _), Symbol::\node()) = Symbol::\node();
public Symbol glb(\node(), Symbol::\adt(str n, list[Symbol] _)) = Symbol::\node();
public Symbol glb(Symbol::\adt(str n, list[Symbol] lp), Symbol::\adt(n, list[Symbol] rp)) = Symbol::\adt(n, addParamLabels(glb(lp,rp),getParamLabels(lp))) when size(lp) == size(rp) && getParamLabels(lp) == getParamLabels(rp) && size(getParamLabels(lp)) > 0;
public Symbol glb(Symbol::\adt(str n, list[Symbol] lp), Symbol::\adt(n, list[Symbol] rp)) = Symbol::\adt(n, glb(lp,rp)) when size(lp) == size(rp) && size(getParamLabels(lp)) == 0;
public Symbol glb(Symbol::\adt(str n, list[Symbol] lp), Symbol::\adt(str m, list[Symbol] rp)) = Symbol::\node() when n != m;
public Symbol glb(Symbol::\adt(str ln, list[Symbol] lp), Symbol::\cons(Symbol b, _, list[Symbol] _)) = glb(Symbol::\adt(ln,lp),b);

public Symbol glb(Symbol::\cons(Symbol la, _, list[Symbol] _), Symbol::\cons(Symbol ra, _, list[Symbol] _)) = glb(la,ra);
public Symbol glb(Symbol::\cons(Symbol a, _, list[Symbol] lp), Symbol::\adt(str n, list[Symbol] rp)) = glb(a,Symbol::\adt(n,rp));
public Symbol glb(Symbol::\cons(Symbol _, _, list[Symbol] _), \node()) = \node();

public Symbol glb(Symbol::\alias(str _, list[Symbol] _, Symbol aliased), Symbol r) = glb(aliased, r);
public Symbol glb(Symbol l, Symbol::\alias(str _, list[Symbol] _, Symbol aliased)) = glb(l, aliased);

public Symbol glb(Symbol::\parameter(str _, Symbol bound), Symbol r) = glb(bound, r);
public Symbol glb(Symbol l, Symbol::\parameter(str _, Symbol bound)) = glb(l, bound);

public Symbol glb(Symbol::\reified(Symbol l), Symbol::\reified(Symbol r)) = Symbol::\reified(glb(l,r));
public Symbol glb(Symbol::\reified(Symbol l), Symbol::\node()) = Symbol::\node();

public Symbol glb(Symbol::\func(Symbol lr, list[Symbol] lp, list[Symbol] kwl), Symbol::\func(Symbol rr, list[Symbol] rp, list[Symbol] kwr)) {
	glbReturn = glb(lr,rr);
	glbParams = lub(Symbol::\tuple(lp),Symbol::\tuple(rp));
	if (isTupleType(glbParams))
		return \func(glbReturn, glbParams.symbols, kwl == kwr ? kwl : []);
	else
		return Symbol::\value();
}

public Symbol glb(Symbol::\label(_,Symbol l), Symbol r) = glb(l,r);
public Symbol glb(Symbol l, Symbol::\label(_,Symbol r)) = glb(l,r);

public list[Symbol] glb(list[Symbol] l, list[Symbol] r) = [glb(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r); 
public default list[Symbol] glb(list[Symbol] l, list[Symbol] r) = [\value()]; 


data Exception 
     = typeCastException(Symbol from, type[value] to);

public &T typeCast(type[&T] typ, value v) {
  if (&T x := v)
    return x;
  throw typeCastException(typeOf(v), typ);
}

@doc{
.Synopsis
Instantiate an ADT constructor of a given type with the given children and optional keyword arguments.

.Description

This function will build a constructor if the definition exists and throw an exception otherwise.
}
@javaClass{org.rascalmpl.library.Type}
public java &T make(type[&T] typ, str name, list[value] args);
 
@javaClass{org.rascalmpl.library.Type}
public java &T make(type[&T] typ, str name, list[value] args, map[str,value] keywordArgs);
 
@doc{
.Synopsis
Returns the dynamic type of a value as a reified type.

.Description

As opposed to the # operator, which produces the type of a value statically, this
function produces the dynamic type of a value, represented by a symbol.


.Examples
[source,rascal-shell]
----
import Type;
value x = 1;
typeOf(x)
----

.Pitfalls

*  Note that the `typeOf` function does not produce definitions, like the 
   [reify]((Rascal:Values-ReifiedTypes)) operator `#` does, 
   since values may escape the scope in which they've been constructed leaving their contents possibly undefined.
}
@javaClass{org.rascalmpl.library.Type}
public java Symbol typeOf(value v);

@doc{
.Synopsis
Determine if the given type is an int.
}
public bool isIntType(Symbol::\alias(_,_,Symbol at)) = isIntType(at);
public bool isIntType(Symbol::\parameter(_,Symbol tvb)) = isIntType(tvb);
public bool isIntType(Symbol::\label(_,Symbol lt)) = isIntType(lt);
public bool isIntType(Symbol::\int()) = true;
public default bool isIntType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a bool.
}
public bool isBoolType(Symbol::\alias(_,_,Symbol at)) = isBoolType(at);
public bool isBoolType(Symbol::\parameter(_,Symbol tvb)) = isBoolType(tvb);
public bool isBoolType(Symbol::\label(_,Symbol lt)) = isBoolType(lt);
public bool isBoolType(Symbol::\bool()) = true;
public default bool isBoolType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a real.
}
public bool isRealType(Symbol::\alias(_,_,Symbol at)) = isRealType(at);
public bool isRealType(Symbol::\parameter(_,Symbol tvb)) = isRealType(tvb);
public bool isRealType(Symbol::\label(_,Symbol lt)) = isRealType(lt);
public bool isRealType(Symbol::\real()) = true;
public default bool isRealType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a rational.
}
public bool isRatType(Symbol::\alias(_,_,Symbol at)) = isRatType(at);
public bool isRatType(Symbol::\parameter(_,Symbol tvb)) = isRatType(tvb);
public bool isRatType(Symbol::\label(_,Symbol lt)) = isRatType(lt);
public bool isRatType(Symbol::\rat()) = true;
public default bool isRatType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a string.
}
public bool isStrType(Symbol::\alias(_,_,Symbol at)) = isStrType(at);
public bool isStrType(Symbol::\parameter(_,Symbol tvb)) = isStrType(tvb);
public bool isStrType(Symbol::\label(_,Symbol lt)) = isStrType(lt);
public bool isStrType(Symbol::\str()) = true;
public default bool isStrType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a num.
}
public bool isNumType(Symbol::\alias(_,_,Symbol at)) = isNumType(at);
public bool isNumType(Symbol::\parameter(_,Symbol tvb)) = isNumType(tvb);
public bool isNumType(Symbol::\label(_,Symbol lt)) = isNumType(lt);
public bool isNumType(Symbol::\num()) = true;
public default bool isNumType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a node.
}
public bool isNodeType(Symbol::\alias(_,_,Symbol at)) = isNodeType(at);
public bool isNodeType(Symbol::\parameter(_,Symbol tvb)) = isNodeType(tvb);
public bool isNodeType(Symbol::\label(_,Symbol lt)) = isNodeType(lt);
public bool isNodeType(Symbol::\node()) = true;
public bool isNodeType(Symbol::\adt(_,_)) = true;
public default bool isNodeType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a void.
}
public bool isVoidType(Symbol::\alias(_,_,Symbol at)) = isVoidType(at);
public bool isVoidType(Symbol::\parameter(_,Symbol tvb)) = isVoidType(tvb);
public bool isVoidType(Symbol::\label(_,Symbol lt)) = isVoidType(lt);
public bool isVoidType(Symbol::\void()) = true;
public default bool isVoidType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a value.
}
public bool isValueType(Symbol::\alias(_,_,Symbol at)) = isValueType(at);
public bool isValueType(Symbol::\parameter(_,Symbol tvb)) = isValueType(tvb);
public bool isValueType(Symbol::\label(_,Symbol lt)) = isValueType(lt);
public bool isValueType(Symbol::\value()) = true;
public default bool isValueType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a loc.
}
public bool isLocType(Symbol::\alias(_,_,Symbol at)) = isLocType(at);
public bool isLocType(Symbol::\parameter(_,Symbol tvb)) = isLocType(tvb);
public bool isLocType(Symbol::\label(_,Symbol lt)) = isLocType(lt);
public bool isLocType(Symbol::\loc()) = true;
public default bool isLocType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a `datetime`.
}
public bool isDateTimeType(Symbol::\alias(_,_,Symbol at)) = isDateTimeType(at);
public bool isDateTimeType(Symbol::\parameter(_,Symbol tvb)) = isDateTimeType(tvb);
public bool isDateTimeType(Symbol::\label(_,Symbol lt)) = isDateTimeType(lt);
public bool isDateTimeType(Symbol::\datetime()) = true;
public default bool isDateTimeType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a set.
}
public bool isSetType(Symbol::\alias(_,_,Symbol at)) = isSetType(at);
public bool isSetType(Symbol::\parameter(_,Symbol tvb)) = isSetType(tvb);
public bool isSetType(Symbol::\label(_,Symbol lt)) = isSetType(lt);
public bool isSetType(Symbol::\set(_)) = true;
public bool isSetType(Symbol::\rel(_)) = true;
public default bool isSetType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a relation.
}
public bool isRelType(Symbol::\alias(_,_,Symbol at)) = isRelType(at);
public bool isRelType(Symbol::\parameter(_,Symbol tvb)) = isRelType(tvb);
public bool isRelType(Symbol::\label(_,Symbol lt)) = isRelType(lt);
public bool isRelType(Symbol::\rel(_)) = true;
public bool isRelType(Symbol::\set(Symbol tp)) = true when isTupleType(tp);
public default bool isRelType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a list relation.
}
public bool isListRelType(Symbol::\alias(_,_,Symbol at)) = isListRelType(at);
public bool isListRelType(Symbol::\parameter(_,Symbol tvb)) = isListRelType(tvb);
public bool isListRelType(Symbol::\label(_,Symbol lt)) = isListRelType(lt);
public bool isListRelType(Symbol::\lrel(_)) = true;
public bool isListRelType(Symbol::\list(Symbol tp)) = true when isTupleType(tp);
public default bool isListRelType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a tuple.
}
public bool isTupleType(Symbol::\alias(_,_,Symbol at)) = isTupleType(at);
public bool isTupleType(Symbol::\parameter(_,Symbol tvb)) = isTupleType(tvb);
public bool isTupleType(Symbol::\label(_,Symbol lt)) = isTupleType(lt);
public bool isTupleType(Symbol::\tuple(_)) = true;
public default bool isTupleType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a list.
}
public bool isListType(Symbol::\alias(_,_,Symbol at)) = isListType(at);
public bool isListType(Symbol::\parameter(_,Symbol tvb)) = isListType(tvb);
public bool isListType(Symbol::\label(_,Symbol lt)) = isListType(lt);
public bool isListType(Symbol::\list(_)) = true;
public bool isListType(Symbol::\lrel(_)) = true;
public default bool isListType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a list relation.
}
public bool isListRelType(Symbol::\alias(_,_,Symbol at)) = isListRelType(at);
public bool isListRelType(Symbol::\parameter(_,Symbol tvb)) = isListRelType(tvb);
public bool isListRelType(Symbol::\label(_,Symbol lt)) = isListRelType(lt);
public bool isListRelType(Symbol::\lrel(_)) = true;
public default bool isListRelType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a map.
}
public bool isMapType(Symbol::\alias(_,_,Symbol at)) = isMapType(at);
public bool isMapType(Symbol::\parameter(_,Symbol tvb)) = isMapType(tvb);
public bool isMapType(Symbol::\label(_,Symbol lt)) = isMapType(lt);
public bool isMapType(Symbol::\map(_,_)) = true;
public default bool isMapType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a bag (bags are not yet implemented).
}
public bool isBagType(Symbol::\alias(_,_,Symbol at)) = isBagType(at);
public bool isBagType(Symbol::\parameter(_,Symbol tvb)) = isBagType(tvb);
public bool isBagType(Symbol::\label(_,Symbol lt)) = isBagType(lt);
public bool isBagType(Symbol::\bag(_)) = true;
public default bool isBagType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is an Abstract Data Type (ADT).
}
public bool isADTType(Symbol::\alias(_,_,Symbol at)) = isADTType(at);
public bool isADTType(Symbol::\parameter(_,Symbol tvb)) = isADTType(tvb);
public bool isADTType(Symbol::\label(_,Symbol lt)) = isADTType(lt);
public bool isADTType(Symbol::\adt(_,_)) = true;
public bool isADTType(Symbol::\reified(_)) = true;
public default bool isADTType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a constructor.
}
public bool isConstructorType(Symbol::\alias(_,_,Symbol at)) = isConstructorType(at);
public bool isConstructorType(Symbol::\parameter(_,Symbol tvb)) = isConstructorType(tvb);
public bool isConstructorType(Symbol::\label(_,Symbol lt)) = isConstructorType(lt);
public bool isConstructorType(Symbol::\cons(Symbol _,str _,list[Symbol] _)) = true;
public default bool isConstructorType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is an alias.
}
public bool isAliasType(Symbol::\alias(_,_,_)) = true;
public bool isAliasType(Symbol::\parameter(_,Symbol tvb)) = isAliasType(tvb);
public bool isAliasType(Symbol::\label(_,Symbol lt)) = isAliasType(lt);
public default bool isAliasType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a function.
}
public bool isFunctionType(Symbol::\alias(_,_,Symbol at)) = isFunctionType(at);
public bool isFunctionType(Symbol::\parameter(_,Symbol tvb)) = isFunctionType(tvb);
public bool isFunctionType(Symbol::\label(_,Symbol lt)) = isFunctionType(lt);
public bool isFunctionType(Symbol::\func(_,_,_)) = true;
public default bool isFunctionType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is a reified type.
}
public bool isReifiedType(Symbol::\alias(_,_,Symbol at)) = isReifiedType(at);
public bool isReifiedType(Symbol::\parameter(_,Symbol tvb)) = isReifiedType(tvb);
public bool isReifiedType(Symbol::\label(_,Symbol lt)) = isReifiedType(lt);
public bool isReifiedType(Symbol::\reified(_)) = true;
public default bool isReifiedType(Symbol _) = false;

@doc{
.Synopsis
Determine if the given type is an type variable (parameter).
}
public bool isTypeVar(Symbol::\parameter(_,_)) = true;
public bool isTypeVar(Symbol::\alias(_,_,Symbol at)) = isTypeVar(at);
public bool isTypeVar(Symbol::\label(_,Symbol lt)) = isTypeVar(lt);
public default bool isTypeVar(Symbol _) = false;
