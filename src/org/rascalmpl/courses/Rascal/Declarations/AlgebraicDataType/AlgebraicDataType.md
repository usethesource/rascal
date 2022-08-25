# Algebraic Data Type


.Synopsis
Define a user-defined type (Algebraic Data Type).

.Index
data

.Syntax

.Types

.Function

.Details

.Description

In ordinary programming languages record types or classes exist to introduce a new type name for a collection of related, 
named, values and to provide access to the elements of such a collection through their name. 

In Rascal, algebraic data types provide this facility. They have to be declared, and
then values can be declared using calls to the declared constructor functions,
see ((Values-Constructor)).

.Examples

The following data declaration defines the datatype `Bool` that contains various constants (`tt()` and `ff()`
and constructor functions `conj` and `disj`.
[source,rascal-shell,continue]
----
data Bool = tt() | ff() | conj(Bool L, Bool R)  | disj(Bool L, Bool R);
----
terms of type `Bool` can be constructed using the defined constructors:
[source,rascal-shell,continue]
----
conj(tt(),ff());
----

.Benefits

.Pitfalls

