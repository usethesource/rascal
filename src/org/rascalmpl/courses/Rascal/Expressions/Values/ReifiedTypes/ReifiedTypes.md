# ReifiedTypes

.Synopsis
Types can be represented by values

.Index
#

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

The type reify expression operator has two functions in one go:

*  it transforms type literals into values that represent them (an isomorphic relation)
*  it reifies the declarations necessary to build values of the types as well

As a result a reified type can be used to reconstruct a type and the abstract (<<Algebraic Data Type>>) or concrete (<<Syntax Definition>>) grammar that produced it. 


Type literals have a nice interaction with <<Type Parameters>>, since they can be used to bind a type parameter without having to provide a value of the type. An example is the ((parse)) function in ((ParseTree)) (see below for an example usage).

The values that are used to represent types are declared in the ((Libraries-Prelude-Type)) module and ((Libraries-Prelude-ParseTree)) modules, namely `Symbol` is the data-type to represent types symbolically and `Production` is the data-type for representing grammatical constructs. 

A type literal wraps a `Symbol` and a map of `Production`s.

.Examples
First import the module `Type`:
[source,rascal-shell]
----
import Type;
----
Builtin types can be constructed without definitions, so the reified type representation is simple:
[source,rascal-shell,continue]
----
#int
#list[int]
#rel[int from, int to]
----
to get the symbol from the reified type:
[source,rascal-shell,continue]
----
#int.symbol
----
or we can use some definitions and reify the defined type to see a different behavior:
[source,rascal-shell,continue]
----
data Nat = zero() | succ(Nat prev) | add(Nat l, Nat r) | mul(Nat l, Nat r);
#Nat
----
and we can get an abstract definition of the constructors of the [AlgebraicDataType]:
[source,rascal-shell,continue]
----
import Type;
#Nat.definitions[adt("Nat",[])]
----
we could go the other way around and construct a type literal dynamically:
[source,rascal-shell,continue]
----
type(\int(),())
type(\int(),()) == #int
----
we use type literals often in IO to express an expected type:
[source,rascal-shell,continue]
----
import ValueIO;
int testInt = readTextValueString(#int, "1");
tuple[int,int] testTuple = readTextValueString(#tuple[int,int], "\<1,2\>");
----



.Benefits

.Pitfalls

*  Note that the type reify operator always produces constant values, because type literals are always constants.

