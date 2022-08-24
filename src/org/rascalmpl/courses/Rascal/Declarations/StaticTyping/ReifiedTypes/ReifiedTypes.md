# Reified Types

.Synopsis
Reified types are types that can be used as values.

.Index
# type &

.Syntax
`# _Name_`

.Types
`type`

.Function

.Details

.Description
Usually one declares functions that have arguments that have a type that corresponds to one of the many forms of values in Rascal.
In exceptional circumstances it is desirable to define functions that have a type itself as argument. 

To solve this problem in a more general manner something special has to be done. 
Types are not values and without an additional mechanism they cannot be passed as arguments to functions. 
To achieve this effect we introduce reified types that are denoted by the type `type`. 
In other words, reified types make it possible to use types as values.

.Examples
The prototypical example is a parse function: how to write a type safe parse function that expresses the type of the result we expect?
Suppose we want to parse a language that has the non-terminals `EXP`, `STAT` and `PROGRAM`.
A first, naive, solution introduces a parse function for each non-terminal:

[source,rascal]
----
EXP parseEXP(str s){ ... }
STAT parsePROGRAM(str s) { ... }
PROGRAM parsePROGRAM(str s) { ... }
----
Unfortunately this solution does not scale well to large languages with many non-terminals and it breaks down completely 
when we do not know the non-terminals before hand.

Now we can write (see <<Type Parameters>> for a description of the `&T` notation):

[source,rascal]
----
&T parse(type[&T] start, str s) { ... }
----
and use the parse by giving it a type as argument:

[source,rascal]
----
parse(#EXP, "1+3");
----

.Benefits

.Pitfalls

