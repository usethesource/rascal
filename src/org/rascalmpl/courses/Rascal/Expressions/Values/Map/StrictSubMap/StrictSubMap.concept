# Map StrictSubMap

.Synopsis
Strict submap operator on map values.

.Index
<

.Syntax
`_Exp_~1~ < _Exp_~2~`

.Types

|====
| `_Exp~1~_`            |  `_Exp~2~_`             | `_Exp~1~_ < _Exp~2~_` 

| `map[_TK~1~_,_TV~2~_]` |  `map[_TK~2~_, _TV~2~_]` | `bool`               
|====

.Function

.Details

.Description
Yields `true` if all key/value pairs in the map value of _Exp_~1~ occur in the map value _Exp_~2~
and the values of _Exp_~1~ and _EXp_~2~ are not equal, and `false` otherwise.

.Examples
[source,rascal-shell]
----
("apple": 1, "pear": 2) < ("pear": 2, "apple": 1, "banana" : 3);
("apple": 1, "pear": 2) < ("apple": 1, "banana" : 3);
----

.Benefits

.Pitfalls

