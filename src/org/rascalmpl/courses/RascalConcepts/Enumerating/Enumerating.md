# Enumerating

.Synopsis
Enumerating values.

.Syntax

.Types

.Function

.Details

.Description
Enumerating regards the enumeration of the values in a given (finite) domain, be it the elements in a list, the substrings of a string, 
or all the nodes in a tree. 
Each value that is enumerated is first matched against a pattern before it can possibly contribute to the result of 
the enumerator. An enumerator yields `true` as long as it has generated a new value, and `false` otherwise.

See [Enumerator]((Rascal:Comprehensions-Enumerator)) for details.

.Examples
```rascal
int x <- { 1, 3, 5, 7, 11 }
int x <- [ 1 .. 10 ]
/asgStat(Id name, _) <- P
```

The first two produce the integer elements of a set of integers, respectively, a range of integers. 
Observe that the left-hand side of an enumerator is a pattern, of which `int x` is a specific instance. 
The use of more general patterns is illustrated by the third enumerator that does a deep traversal 
(as denoted by the descendant operator `/`) of the complete program `P` (that is assumed to have a 
`PROGRAM` as value) and only yields statements that match the assignment pattern (`asgStat`).
Note the use of an anonymous variable at the `EXP` position in the pattern.

Let's practice some of these examples.

```rascal-shell
int x <- {};
```
The enumerator does not produce any value and returns `false`.

```rascal-shell,error
int x <- {1, 3, 5, 7, 11 };
x;
```
Well, this is a disappointing experience. The generator returned `true` since it did produce a value.
Apparently, we cannot inspect the value of the variable `x` that was bound.

Another example that results in an error:
```rascal-shell,error
str x <- {1, 3, 5, 7, 11 };
```
Here, the enumerator produces its first integer value, an attempt is made to assign this to variable `x` that is declared as string,
and an error results.

A more satisfying use is as follows:
```rascal-shell
{ x * x | int x <- {1, 3, 5, 7, 11 }};
```
When used inside [Comprehensions]((Rascal:Expressions-Comprehensions)), 
or [For]((Rascal:Statements-For)), [Do]((Rascal:Statements-Do)), or [While]((Rascal:Statements-While)) 
statement, all values of the generator will be produced and used.
The variables that are introduced by a enumerator are local to the construct in which the enumerator is used.
Here is a similar example:
```rascal-shell
import IO;
for(int x <- {1, 3, 5, 7, 11 })
    println("x = <x>");
```

.Benefits

.Pitfalls
The variables that are bound by an enumerator are local to the statement in which the enumerator is used.

