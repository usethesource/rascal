@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl}
@synopsis{Rascal's type system, implemented in Rascal itself.}
@description{
The goal of this module is to provide:

*  reflection capabilities that are useful for deserialization and validation of data, and 
*  to provide the basic building blocks for syntax trees (see ((module:ParseTree))),
*  provide the implementations of Rascal's type _lattice_: ((subtype)), ((glb)), ((lub)) and ((intersects)) for reified type Symbols

The following definition is built into Rascal:
```rascal
data type[&T] = type(Symbol symbol, map[Symbol, Production] definitions);
```

For values of type `type[...]` the static and dynamic type systems satisfy three additional constraints over the rules of type-parameterized data types:
1. For any type `T`: `#T` has type `type[T]`
2. For any type T and any value of `type[T]`, namely `type(S, D)` it holds that S is the symbolic representation of type `T` using the ((Type-Symbol)) type, and
3. ... `D` holds all the necessary data and syntax rules required to form values of type `T`.

In other words, the `#` operator will always produce a value of `type[&T]`, where `&T` is bound to the type that was reified _and_ said value will contain the full grammatical definition for what was bound to `&T`.

The ((subtype)) relation of Rascal has all the mathematical properties of a _finite lattice_; where ((lub)) implements the _join_ and ((glb)) implements the _meet_ operation.
This is a core design principle of Rascal with the following benefits:
* Type inference has a guaranteed least or greatest solution, always. This means that constraints are always solvable in an unambiguous manner.
* A _principal type_ can always be computed, which is a most precise and unique solution of a type inference problem. Without the lattice, solution candidates could become incomparable and thus ambiguous. Without
this principal type property, type inference is predictable for programmers.
* Solving type inference constraints can be implemented efficiently. The algorithm, based on ((lub)) and ((glb)), makes progress _deterministically_ and does not require backtracking
to find better solutions. Since the lattice is not very deep, fixed-point solutions are always found quickly.

Much of the aspects of the ((subtype)) lattice are derived from the fact that Rascal's values are _immutable_ or _readonly_. This typically allows for containers
to be co-variant in their type parameters: `list[int] <: list[value]`. Because all values in Rascal are immutable, and implemented using persistent data-structures,
its type literals do not feature annotations for co- and contra-variance. We can assume co-variance practically everywhere (even for function parameters).

Function types in Rascal are special because functions are always _openly extensible_, as are the modules they are contained in. This means that the parameter
types of functions can also be extended, and thus they are co-variant (accepting more rather than fewer kinds of data). Contra-variance is still also allowed, 
of course, and so we say function parameter types are "variant" in both directions. ((lub)) has been hard-wired to choose the more general (co-variant) 
solutions for function parameter types to reflect the general applicability of the openly extensible functions.
}
@examples{
```rascal-shell
import Type;
#int
#rel[int,int]
data B = t();
#B
syntax A = "a";
#A;
type(\int(),())
```
}
module Type

@synopsis{A Symbol represents a Rascal Type.}
@description{
Symbols are values that represent Rascal's types. These are the atomic types.
We define here:

<1>  Atomic types.
<2> Labels that are used to give names to symbols, such as field names, constructor names, etc.
<3>  Composite types.
<4>  Parameters that represent a type variable.

In ((module:ParseTree)), see ((ParseTree-Symbol)), 
Symbols will be further extended with the symbols that may occur in a parse tree. 
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
     | \tuple(list[Symbol] symbols)
     | \list(Symbol symbol)
     | \map(Symbol from, Symbol to)
     | \adt(str name, list[Symbol] parameters)
     | \cons(Symbol \adt, str name, list[Symbol] parameters)
     | \alias(str name, list[Symbol] parameters, Symbol aliased)
     | \func(Symbol ret, list[Symbol] parameters, list[Symbol] kwTypes)
     | \reified(Symbol symbol)
     ;

data Symbol // <4>
     = \parameter(str name, Symbol bound) 
     ;

data Symbol = \data(Symbol modified); // to-data modifier

Symbol \data(\data(Symbol s)) = \data(s);
Symbol \data(adt(n, ps))      = adt(n, ps);

bool subtype(\data(Symbol s), \node())        = true;
bool subtype(\data(parameter(_,_)), adt(_,_)) = true;

@synopsis{rel types are syntactic sugar for sets of tuples.}
Symbol \rel(list[Symbol] symbols) 
     = \set(\tuple(symbols));

@synopsis{lrel types are syntactic sugar for lists of tuples.}
Symbol \lrel(list[Symbol] symbols) 
     = \list(\tuple(symbols));

@synopsis{Overloaded/union types are always reduced to the least upper bound of their constituents.}
@description{
This semantics of overloading in the type system is essential to make sure it remains a _lattice_.
}
Symbol overloaded(set[Symbol] alternatives)
     = (\void() | lub(it, a) | Symbol a <- alternatives);

@synopsis{A production in a grammar or constructor in a data type.}
@description{
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

@synopsis{Attributes register additional semantics annotations of a definition.}
data Attr 
     = \tag(value \tag) 
     ;

@synopsis{Transform a function with varargs (`...`) to a normal function with a list argument.}
Symbol \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg) 
     = \func(ret, parameters + \list(varArg), []);

@synopsis{Normalize the choice between alternative productions.}
@description{
The following normalization rules canonicalize grammars to prevent arbitrary case distinctions later
Nested choice is flattened.

The below code replaces the following code for performance reasons in compiled code:
```rascal
Production choice(Symbol s, {*Production a, choice(Symbol t, set[Production] b)})
     = choice(s, a+b);
```
}
Production choice(Symbol s, set[Production] choices) {
    if (!any(choice(Symbol _, set[Production] _)  <- choices)) {
        fail choice;
	} else {   
	    // TODO: this does not work in interpreter and typechecker crashes on it (both related to the splicing)
	    //return choice(s, { *(choice(Symbol t, set[Production] b) := ch ? b : {ch}) | ch <- choices });
	    bool changed = false;
	    set[Production] new_choices = {};
         for (Production ch <- choices) {
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

  
@synopsis{Subtype is the core implementation of Rascal's type lattice.}
@description{
The following graph depicts Rascal's type lattice for a number of example types, including:
* all the builtin types, with `value` as maximum and `void` as the minimum of the lattice
* parameterized container types (co-variant)
* a function type example (variant in parameter positions, both directions)
* a data type `Exp`
* a reified type `type[value]` and `type[int]` (co-variant)
```rascal-prepare
import Type;
data Exp = \int(int i);
allTypes = {#int, #bool, #real, #rat, #str, #num, #node, #void, #value, #loc, #datetime, #set[int], #set[value], #rel[int, int], #rel[value,value], #lrel[int, int], #lrel
[value,value], #list[int], #list[value], #map[str, int], #map[str, value], #Exp, #int(int), #int(num), #int(value), #value(value), #type[int], #type[value]};
import analysis::graphs::Graph;
typeLattice = { <"<t1>", "<t2>"> | <t1, t2:!t1> <- allTypes  * allTypes, subtype(t1, t2)};
import vis::Graphs;
graph(transitiveReduction(typeLattice<1,0>), cfg=cytoGraphConfig(\layout=defaultDagreLayout()));
```
}
@examples{
```rascal-shell
import Type;
subtype(#int, #value)
subtype(#value, #int)
```
}
bool subtype(type[&T] t, type[&U] u) = subtype(t.symbol, u.symbol);

@synopsis{This function documents and implements the subtype relation of Rascal's type system.}
@javaClass{org.rascalmpl.library.Type}
java bool subtype(Symbol s, Symbol t);

@synopsis{Checks if two types have a non-empty intersection, meaning there exists values which types with both as a supertype}
@description{
Consider `tuple[int, value]` and `tuple[value, int]` which are not subtypes of one another,
but all the values in their intersection, `tuple[int, int]` belong to both types.

Type intersection is important for the type-checking of pattern matching, and since function parameters
are patterns in Rascal, also for the type-checking of function parameters. Pattern matching between two 
types is possible as long as the intersection is non-empty. This is true if one is a sub-type of other,
or vice verse: then the intersection _is_ the subtype. However, the above tuple example also shows
there can be non-empty intersections when the types are not sub-types.
}
@javaClass{org.rascalmpl.library.Type}
java bool intersects(Symbol s, Symbol t);

bool intersects(type[&T] t, type[&U] u)
     = intersects(t.symbol, u.symbol);

@synopsis{Check if two types are comparable, i.e., one is a subtype of the other or vice versa.}
bool comparable(Symbol s, Symbol t) = subtype(s,t) || subtype(t,s); 

bool comparable(type[value] s, type[value] t) = comparable(s.symbol, t.symbol);

@synopsis{Check if two types are equivalent, i.e. they are both subtypes of each other.}
bool equivalent(Symbol s, Symbol t) = subtype(s,t) && subtype(t,s);

bool equivalent(type[value] s, type[value] t) = equivalent(s.symbol, t.symbol);

@synopsis{Strict structural equality between values.}
@description{
The difference between `eq` and `==` is that no implicit coercions are done between values of incomparable types
at the top-level. 

The `==` operator, for convenience, equates `1.0` with `1` but not `[1] with [1.0]`, which can be annoying
when writing consistent specifications. The new number system that is coming up will not have these issues.
}
@examples{
```rascal-shell
import Type;
1 == 1.0
eq(1,1.0)
```
}
@javaClass{org.rascalmpl.library.Type}
java bool eq(value x, value y);

@synopsis{The least upper bound (lub) of two types is the common ancestor in the type lattice that is lowest.}
@description{
This function implements the lub operation in Rascal's type system, via unreifying the Symbol values
and calling into the underlying run-time Type implementation.
}
@examples{
```rascal-shell
import Type;
lub(#tuple[int,value], #tuple[value, int])     
lub(#int, #real)
```
}
@javaClass{org.rascalmpl.library.Type}
java Symbol lub(Symbol s1, Symbol s2);

type[value] lub(type[&T] t, type[&T] u)
     = type(lub(t.symbol, u.symbol), ());

@synopsis{The greatest lower bound (glb) between two types, i.e. a common descendant of two types in the lattice which is largest.}
@description{
This function implements the glb operation in Rascal's type system, via unreifying the Symbol values
and calling into the underlying run-time Type implementation.
}
@examples{
```rascal-shell
import Type;
glb(#tuple[int,value], #tuple[value, int])   
glb(#int, #real)  
```
}
@javaClass{org.rascalmpl.library.Type}
java Symbol glb(Symbol s1, Symbol s2);

type[value] glb(type[&T] t, type[&T] u)
     = type(glb(t.symbol, u.symbol), ());

data Exception = typeCastException(Symbol from, type[value] to);

&T typeCast(type[&T] typ, value v) {
  if (&T x := v) {
     return x;
  }

  throw typeCastException(typeOf(v), typ);
}

@synopsis{Dynamically instantiate an data constructor of a given type with the given children and optional keyword arguments.}
@description{
This function will build a constructor if the definition exists and the parameters fit its description, or throw an exception otherwise.

This function can be used to validate external data sources against a data type such as XML, JSON and YAML documents.
}
@javaClass{org.rascalmpl.library.Type}
java &T make(type[&T] typ, str name, list[value] args);
 
@javaClass{org.rascalmpl.library.Type}
public java &T make(type[&T] typ, str name, list[value] args, map[str,value] keywordArgs);

@javaClass{org.rascalmpl.library.Type}
@synopsis{Instantiate a constructor value by first declaring the given constructor and then applying it to the given parameters.}
@description{
The grammar rules for data constructors in a reified type can inversely be applied
again to construct constructor instances, dynamically.

This "reflection" feature is dynamically typed, so we don't know statically which type
of ADT will come out.
}
@examples{
This is the value-ified represention of `data Exp = constant(int n)`:
```rascal-shell
import Type;
rule = cons(label("constant",adt("Exp",[])),[label("n",\int())],[],{});
// we can use it to instantiate a constructor value that uses that rule:
example = make(rule, [1]);
// to illustrate we now construct it the normal way and test for equivalence:
data Exp = constant(int n);
example == constant(1);
```
}
public java node make(Production cons, list[value] args, map[str,value] keywordArgs=());

@synopsis{Returns the dynamic type of a value as a ((Type-Symbol)).}
@description{
As opposed to the # operator, which produces the type of a value statically, this
function produces the dynamic type of a value, represented by a symbol.
}
@examples{
```rascal-shell
import Type;
value x = 1;
typeOf(x)
```
}
@benefits{
* constructing a reified type from a dynamic type is possible:
```rascal-shell,continue
type(typeOf(x), ())
```
}
@pitfalls{
Note that the `typeOf` function does not produce definitions, like the 
reify operator `#` does, since values may escape the scope in which they've been constructed.
}
@javaClass{org.rascalmpl.library.Type}
public java Symbol typeOf(value v);

@javaClass{org.rascalmpl.library.Type}
public java Production getConstructor(&A <: node constructor);

@synopsis{Determine if the given type is an int.}
public bool isIntType(Symbol::\alias(_,_,Symbol at)) = isIntType(at);
public bool isIntType(Symbol::\parameter(_,Symbol tvb)) = isIntType(tvb);
public bool isIntType(Symbol::\label(_,Symbol lt)) = isIntType(lt);
public bool isIntType(Symbol::\int()) = true;
public default bool isIntType(Symbol _) = false;

@synopsis{Determine if the given type is an bool.}
public bool isBoolType(Symbol::\alias(_,_,Symbol at)) = isBoolType(at);
public bool isBoolType(Symbol::\parameter(_,Symbol tvb)) = isBoolType(tvb);
public bool isBoolType(Symbol::\label(_,Symbol lt)) = isBoolType(lt);
public bool isBoolType(Symbol::\bool()) = true;
public default bool isBoolType(Symbol _) = false;

@synopsis{Determine if the given type is an real.}
public bool isRealType(Symbol::\alias(_,_,Symbol at)) = isRealType(at);
public bool isRealType(Symbol::\parameter(_,Symbol tvb)) = isRealType(tvb);
public bool isRealType(Symbol::\label(_,Symbol lt)) = isRealType(lt);
public bool isRealType(Symbol::\real()) = true;
public default bool isRealType(Symbol _) = false;

@synopsis{Determine if the given type is an rat.}
public bool isRatType(Symbol::\alias(_,_,Symbol at)) = isRatType(at);
public bool isRatType(Symbol::\parameter(_,Symbol tvb)) = isRatType(tvb);
public bool isRatType(Symbol::\label(_,Symbol lt)) = isRatType(lt);
public bool isRatType(Symbol::\rat()) = true;
public default bool isRatType(Symbol _) = false;

@synopsis{Determine if the given type is an str.}
public bool isStrType(Symbol::\alias(_,_,Symbol at)) = isStrType(at);
public bool isStrType(Symbol::\parameter(_,Symbol tvb)) = isStrType(tvb);
public bool isStrType(Symbol::\label(_,Symbol lt)) = isStrType(lt);
public bool isStrType(Symbol::\str()) = true;
public default bool isStrType(Symbol _) = false;

@synopsis{Determine if the given type is an num.}
public bool isNumType(Symbol::\alias(_,_,Symbol at)) = isNumType(at);
public bool isNumType(Symbol::\parameter(_,Symbol tvb)) = isNumType(tvb);
public bool isNumType(Symbol::\label(_,Symbol lt)) = isNumType(lt);
public bool isNumType(Symbol::\num()) = true;
public default bool isNumType(Symbol _) = false;

@synopsis{Determine if the given type is an node.}
public bool isNodeType(Symbol::\alias(_,_,Symbol at)) = isNodeType(at);
public bool isNodeType(Symbol::\parameter(_,Symbol tvb)) = isNodeType(tvb);
public bool isNodeType(Symbol::\label(_,Symbol lt)) = isNodeType(lt);
public bool isNodeType(Symbol::\node()) = true;
public bool isNodeType(Symbol::\adt(_,_)) = true;
public default bool isNodeType(Symbol _) = false;

@synopsis{Determine if the given type is an void.}
public bool isVoidType(Symbol::\alias(_,_,Symbol at)) = isVoidType(at);
public bool isVoidType(Symbol::\parameter(_,Symbol tvb)) = isVoidType(tvb);
public bool isVoidType(Symbol::\label(_,Symbol lt)) = isVoidType(lt);
public bool isVoidType(Symbol::\void()) = true;
public default bool isVoidType(Symbol _) = false;

@synopsis{Determine if the given type is an void.}
public bool isValueType(Symbol::\alias(_,_,Symbol at)) = isValueType(at);
public bool isValueType(Symbol::\parameter(_,Symbol tvb)) = isValueType(tvb);
public bool isValueType(Symbol::\label(_,Symbol lt)) = isValueType(lt);
public bool isValueType(Symbol::\value()) = true;
public default bool isValueType(Symbol _) = false;

@synopsis{Determine if the given type is an loc.}
public bool isLocType(Symbol::\alias(_,_,Symbol at)) = isLocType(at);
public bool isLocType(Symbol::\parameter(_,Symbol tvb)) = isLocType(tvb);
public bool isLocType(Symbol::\label(_,Symbol lt)) = isLocType(lt);
public bool isLocType(Symbol::\loc()) = true;
public default bool isLocType(Symbol _) = false;

@synopsis{Determine if the given type is an datetime.}
public bool isDateTimeType(Symbol::\alias(_,_,Symbol at)) = isDateTimeType(at);
public bool isDateTimeType(Symbol::\parameter(_,Symbol tvb)) = isDateTimeType(tvb);
public bool isDateTimeType(Symbol::\label(_,Symbol lt)) = isDateTimeType(lt);
public bool isDateTimeType(Symbol::\datetime()) = true;
public default bool isDateTimeType(Symbol _) = false;

@synopsis{Determine if the given type is an set.}
public bool isSetType(Symbol::\alias(_,_,Symbol at)) = isSetType(at);
public bool isSetType(Symbol::\parameter(_,Symbol tvb)) = isSetType(tvb);
public bool isSetType(Symbol::\label(_,Symbol lt)) = isSetType(lt);
public bool isSetType(Symbol::\set(_)) = true;
public bool isSetType(Symbol::\rel(_)) = true;
public default bool isSetType(Symbol _) = false;

@synopsis{Determine if the given type is an rel.}
public bool isRelType(Symbol::\alias(_,_,Symbol at)) = isRelType(at);
public bool isRelType(Symbol::\parameter(_,Symbol tvb)) = isRelType(tvb);
public bool isRelType(Symbol::\label(_,Symbol lt)) = isRelType(lt);
public bool isRelType(Symbol::\rel(_)) = true;
public bool isRelType(Symbol::\set(Symbol tp)) = true when isTupleType(tp);
public default bool isRelType(Symbol _) = false;

@synopsis{Determine if the given type is an lrel.}
public bool isListRelType(Symbol::\alias(_,_,Symbol at)) = isListRelType(at);
public bool isListRelType(Symbol::\parameter(_,Symbol tvb)) = isListRelType(tvb);
public bool isListRelType(Symbol::\label(_,Symbol lt)) = isListRelType(lt);
public bool isListRelType(Symbol::\lrel(_)) = true;
public bool isListRelType(Symbol::\list(Symbol tp)) = true when isTupleType(tp);
public default bool isListRelType(Symbol _) = false;

@synopsis{Determine if the given type is an tuple.}
public bool isTupleType(Symbol::\alias(_,_,Symbol at)) = isTupleType(at);
public bool isTupleType(Symbol::\parameter(_,Symbol tvb)) = isTupleType(tvb);
public bool isTupleType(Symbol::\label(_,Symbol lt)) = isTupleType(lt);
public bool isTupleType(Symbol::\tuple(_)) = true;
public default bool isTupleType(Symbol _) = false;

@synopsis{Determine if the given type is a list.}
public bool isListType(Symbol::\alias(_,_,Symbol at)) = isListType(at);
public bool isListType(Symbol::\parameter(_,Symbol tvb)) = isListType(tvb);
public bool isListType(Symbol::\label(_,Symbol lt)) = isListType(lt);
public bool isListType(Symbol::\list(_)) = true;
public bool isListType(Symbol::\lrel(_)) = true;
public default bool isListType(Symbol _) = false;

@synopsis{Determine if the given type is an map.}
public bool isMapType(Symbol::\alias(_,_,Symbol at)) = isMapType(at);
public bool isMapType(Symbol::\parameter(_,Symbol tvb)) = isMapType(tvb);
public bool isMapType(Symbol::\label(_,Symbol lt)) = isMapType(lt);
public bool isMapType(Symbol::\map(_,_)) = true;
public default bool isMapType(Symbol _) = false;

@synopsis{Determine if the given type is an bag.}
public bool isBagType(Symbol::\alias(_,_,Symbol at)) = isBagType(at);
public bool isBagType(Symbol::\parameter(_,Symbol tvb)) = isBagType(tvb);
public bool isBagType(Symbol::\label(_,Symbol lt)) = isBagType(lt);
public bool isBagType(Symbol::\bag(_)) = true;
public default bool isBagType(Symbol _) = false;

@synopsis{Determine if the given type is an adt.}
public bool isADTType(Symbol::\alias(_,_,Symbol at)) = isADTType(at);
public bool isADTType(Symbol::\parameter(_,Symbol tvb)) = isADTType(tvb);
public bool isADTType(Symbol::\label(_,Symbol lt)) = isADTType(lt);
public bool isADTType(Symbol::\adt(_,_)) = true;
public bool isADTType(Symbol::\reified(_)) = true;
public default bool isADTType(Symbol _) = false;

@synopsis{Determine if the given type is an constructor.}
public bool isConstructorType(Symbol::\alias(_,_,Symbol at)) = isConstructorType(at);
public bool isConstructorType(Symbol::\parameter(_,Symbol tvb)) = isConstructorType(tvb);
public bool isConstructorType(Symbol::\label(_,Symbol lt)) = isConstructorType(lt);
public bool isConstructorType(Symbol::\cons(Symbol _,str _,list[Symbol] _)) = true;
public default bool isConstructorType(Symbol _) = false;

@synopsis{Determine if the given type is an alias.}
public bool isAliasType(Symbol::\alias(_,_,_)) = true;
public bool isAliasType(Symbol::\parameter(_,Symbol tvb)) = isAliasType(tvb);
public bool isAliasType(Symbol::\label(_,Symbol lt)) = isAliasType(lt);
public default bool isAliasType(Symbol _) = false;

@synopsis{Determine if the given type is an function.}
public bool isFunctionType(Symbol::\alias(_,_,Symbol at)) = isFunctionType(at);
public bool isFunctionType(Symbol::\parameter(_,Symbol tvb)) = isFunctionType(tvb);
public bool isFunctionType(Symbol::\label(_,Symbol lt)) = isFunctionType(lt);
public bool isFunctionType(Symbol::\func(_,_,_)) = true;
public default bool isFunctionType(Symbol _) = false;

@synopsis{Determine if the given type is an reified.}
public bool isReifiedType(Symbol::\alias(_,_,Symbol at)) = isReifiedType(at);
public bool isReifiedType(Symbol::\parameter(_,Symbol tvb)) = isReifiedType(tvb);
public bool isReifiedType(Symbol::\label(_,Symbol lt)) = isReifiedType(lt);
public bool isReifiedType(Symbol::\reified(_)) = true;
public default bool isReifiedType(Symbol _) = false;

@synopsis{Determine if the given type is an type parameter.}
public bool isTypeVar(Symbol::\parameter(_,_)) = true;
public bool isTypeVar(Symbol::\alias(_,_,Symbol at)) = isTypeVar(at);
public bool isTypeVar(Symbol::\label(_,Symbol lt)) = isTypeVar(lt);
public default bool isTypeVar(Symbol _) = false;

java Symbol typeOf(value v);
