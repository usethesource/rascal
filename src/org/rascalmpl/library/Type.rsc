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
     | \rel(list[Symbol] symbols)
     | \lrel(list[Symbol] symbols)
     | \tuple(list[Symbol] symbols)
     | \list(Symbol symbol)
     | \map(Symbol from, Symbol to)
     | \adt(str name, list[Symbol] parameters)
     | \cons(Symbol \adt, str name, list[Symbol] parameters)
     | \alias(str name, list[Symbol] parameters, Symbol aliased)
     | \func(Symbol ret, list[Symbol] parameters, list[Symbol] kwTypes)
     | \overloaded(set[Symbol] alternatives)
     | \reified(Symbol symbol)
     ;

data Symbol // <4>
     = \parameter(str name, Symbol bound) 
     ;

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

// TODO: visualize the _actually implemented_ type lattice of Rascal with this code:
// 
```rascal-prepare
import Type;
data Exp = \int(int i);
allTypes = {#int, #bool, #real, #rat, #str, #num, #node, #void, #value, #loc, #datetime, #set[int], #set[value], #rel[int, int], #rel[value,value], #lrel[int, int], #lrel
[value,value], #list[int], #list[value], #map[str, int], #map[str, value], #Exp, #int(int), #value(value), #type[int], #type[value]};
import analysis::graphs::Graph;
typeLattice = transitiveReduction({ <"<t1>", "<t2>"> | <t1, t2:!t1> <- allTypes  * allTypes, subtype(t1, t2)});
import vis::Graphs;
// TODO: something goes wrong with the graph layout here
graph(typeLattice);
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
     = intersects(t1.symbol, t2.symbol);

@synopsis{Check if two types are comparable, i.e., one is a subtype of the other or vice versa.}
bool comparable(Symbol s, Symbol t) = subtype(s,t) || subtype(t,s); 

@synopsis{Check if two types are equivalent, i.e. they are both subtypes of each other.}
bool equivalent(Symbol s, Symbol t) = subtype(s,t) && subtype(t,s);

@synopsis{Strict structural equality between values.}
@description{
The difference between `eq` and `==` is that no implicit coercions are done between values of incomparable types
at the top-level. 

The `==` operator, for convience, equates `1.0` with `1` but not `[1] with [1.0]`, which can be annoying
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

@synopsis{The least-upperbound (lub) of two types is the common ancestor in the type lattice that is lowest.}
@description{
This function implements the lub operation in Rascal's type system, via unreifying the Symbol values
and calling into the underlying run-time Type implementation.
}
@examples{
import Type;
lub(#tuple[int,value], #tuple[value, int])     
lub(#int, #real)
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
import Type;
glb(#tuple[int,value], #tuple[value, int])   
glb(#int, #real)  
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
java &T make(type[&T] typ, str name, list[value] args, map[str,value] keywordArgs);
 
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
java Symbol typeOf(value v);