# Map NotEqual

.Synopsis
Not equal operator on map values.

.Index
!=

.Syntax
`_Exp_~1~ != _Exp_~2~`

.Types

|====
| `_Exp~1~_`            |  `_Exp~2~_`             | `_Exp~1~_ != _Exp~2~_` 

| `map[_TK~1~_,_TV~2~_]` |  `map[_TK~2~_, _TV~2~_]` | `bool`               
|====

.Function

.Details

.Description
Yields `true` if both arguments contain different key/value pairs, and `false` otherwise.

.Examples
[source,rascal-shell]
----
("apple": 1, "pear": 2) != ("apple": 1, "banana": 3);
("apple": 1, "pear": 2) != ("pear": 2, "apple": 1);
----

.Benefits

.Pitfalls

