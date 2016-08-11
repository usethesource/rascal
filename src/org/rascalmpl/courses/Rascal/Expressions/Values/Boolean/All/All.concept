# Boolean All

.Synopsis
All argument expressions are true.

.Index
all

.Syntax
`all ( _Exp_~1~, _Exp_~2~, ... )`

.Types

//

|====
| `_Exp~1~_` | `_Exp~2~_` | ... | `all ( _Exp~1~_, _Exp~2~_, ... )`

|`bool`     | `bool`    | ... | `bool`                          
|====

.Function

.Details

.Description
Yields `true` when all combinations of values of _Exp_~i~ are true.

.Examples

Are all integers 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 even?
[source,rascal-shell,continue]
----
all(int n <- [1 .. 10], n % 2 == 0);
----
Are all integers 0, 2, 4, 6, 8, 10 even?
[source,rascal-shell,continue]
----
all(int n <- [0, 2 .. 10], n % 2 == 0);
----

When one of the _Exp_~i~ enumerates the elements of an empty list, `all` always returns `true`:
[source,rascal-shell]
----
all(int n <- [], n > 0);
----

.Benefits

.Pitfalls

WARNING: The Rascal interpreter and compiler give different results on an empty list. 
The interpreter returns `fals` for the abo eexample.
