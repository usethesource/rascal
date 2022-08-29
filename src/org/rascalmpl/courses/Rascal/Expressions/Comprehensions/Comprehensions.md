# Comprehensions

.Synopsis
Comprehensions provide a concise notation to conditionally generate new values.

.Index
[ ] { } ( ) | <- 

.Syntax

.Types

.Function

.Details

.Description
Comprehensions are defined for the following types:

*  `list`, see ((List-Comprehension)),
*  `set`, see ((Set-Comprehension)),
*  `map`, see ((Map-Comprehension)).


The syntax varies slightly for each type, but comprehensions have the following common elements:

*  A _generator_ can come in two flavours:
**  an _enumerator_ that generates all the values in some subject value.
**  a _filter_ that performs an arbitrary test on previously generated values.

*  One or more _contributing expressions_ that are added to the list, set or map that is being constructed.


The contributing expressions are evaluated for all possible values of the enumerators that are not
excluded by a test. When a filter fails, execution continues with the preceding enumerator (if any).

Each enumerator may introduce new variables that can be used in subsequent generators as well as in the contributing expressions.
A generator can use the variables introduced by preceding generators. 

.Examples

A list comprehension:
```rascal-shell
[ 3 * X | int X <- [1 .. 10] ];
```
A list comprehension with a filter:
```rascal-shell,continue
[ 3 * X | int X <- [1 .. 10], X > 5];
```
A list comprehension with multiple contributing expressions:
```rascal-shell,continue
[X, X * X | int X <- [1, 2, 3, 4, 5], X >= 3];
```
A set comprehension with a filter:
```rascal-shell,continue
{X | int X <- {1, 2, 3, 4, 5}, X >= 3};
```
A set comprehension that constructs a relation:
```rascal-shell,continue
{<X, Y> | int X <- {1, 2, 3}, int Y <- {2, 3, 4}, X >= Y};
{<Y, X> | <int X, int Y> <- {<1,10>, <2,20>}};
```
Introduce a map of `fruits` and use a map comprehension to filter fruits with an associated value larger than 10:
```rascal-shell,continue
fruits = ("pear" : 1, "apple" : 3, "banana" : 0, "berry" : 25, "orange": 35);
(fruit : fruits[fruit] | fruit <- fruits, fruits[fruit] > 10);
```

See ((List-Comprehension)), ((Set-Comprehension)), or ((Map-Comprehension)) for more examples.

.Benefits

.Pitfalls

