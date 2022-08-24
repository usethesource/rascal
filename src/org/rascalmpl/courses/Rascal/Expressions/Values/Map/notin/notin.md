# Map notin

.Synopsis
Negated membership test on the keys of a map.

.Index
notin

.Syntax
`_Exp_~1~ notin _Exp_~2~`

.Types

|====
| `_Exp~1~_`           |  `_Exp~2~_`         | `_Exp~1~_ notin _Exp~2~_` 

| `_T~1~_`  <: `_TK_`  |  `map[_TK_, _TV_]` | `bool`               
|====

.Function

.Details

.Description
Yields `true` if the value of _Exp_~1~ does not occur as key in the map value of _Exp_~2~ and `false` otherwise. 
The type of _Exp_~1~ should be compatible with the key type _TK_ of _Exp_~2~.

.Examples
[source,rascal-shell]
----
"pineapple" notin ("apple": 1, "pear": 2);
"pear" notin ("apple": 1, "pear": 2);
----

.Benefits

.Pitfalls

