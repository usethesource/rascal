# StaticTyping

.Synopsis
The static type system of Rascal.

.Syntax

.Types

.Function

.Details

.Description
Rascal is based on static typing, this means that as many errors and inconsistencies as possible are spotted before 
the program is executed. 

## The Type Lattice


The types are ordered in a so-called _type lattice_ shown in the following figure.

image::/RascalConcepts/StaticTyping/type-lattice.png[width=400,title="Type Lattice"]


The arrows describe a _subtype-of_ relation between types. The type `void` is the _smallest_ type and 
is included in all other types and the type `value` is the _largest_ type that includes all other types. 
We also see that `rel` is a subtype of `set` and that each ADT is a subtype of `node`. 
A special role is played by the datatype `Tree` that is the generic type of syntax trees. 
Syntax trees for specific languages are all subtypes of `Tree`. As a result, syntax trees can be addressed at two levels: 

*  in a generic fashion as `Tree` and,
*  in a specific fashion as a more precisely typed syntax tree. 
Finally, each `alias` is structurally equivalent to one or more specific other types.


The fact that the types are ordered in a lattice makes it possible to define a *Least Upper Bound* (lub) on types.
Given two types _T_~1~ and _T_~2~, `lub(_T_~1~, _T_~2~)` is defined as the nearest common super type of _T_~1~ and _T_~2~
in the type lattice.

## Advanced Features

The Rascal type system has various advanced features that are described separately:

*  Types may be be _parameterized_ resulting in very general and reusable types, see [Type Parameters].
*  Declarations of [Function]s and [AlgebraicDataType]s may be parameterized and [Type Constraints] can be used to define
   constraints on the actual type to be used.
*  The formal arguments of functions are bound to _values_ but in exceptional cases
  a function may need a type as argument value, ((Reified Types)) make this possible.

.Examples
Here are some simple examples of correct and incorrect typing:

We can assign an integer value to an integer variable:
[source,rascal-shell,continue,error]
----
int i = 3;
----
But assigning a string value gives an error:
[source,rascal-shell,continue,error]
----
int j = "abc";
----
The `num` type accepts integer and real values:
[source,rascal-shell,continue,error]
----
num n = i;
n = 3.14;
----
A variable of type `value` accepts all possible values:
[source,rascal-shell,continue,error]
----
value v = true;
v = "abc";
v = [1, 2, 3];
----

.Benefits

.Pitfalls

