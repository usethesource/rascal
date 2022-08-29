# Map SubMap

.Synopsis
Submap operator on map values.

.Index
<=

.Syntax
`_Exp_~1~ <= _Exp_~2~`

.Types

|====
| `_Exp~1~_`            |  `_Exp~2~_`             | `_Exp~1~_ <= _Exp~2~_` 

| `map[_TK~1~_,_TV~2~_]` |  `map[_TK~2~_, _TV~2~_]` | `bool`               
|====

.Function

.Details

.Description
Yields `true` if all key/value pairs in the map value of _Exp_~1~ occur in the map value _Exp_~2~
or the values of _Exp_~1~ and _Exp_~2~ are equal, and `false` otherwise.

.Examples
```rascal-shell
("apple": 1, "pear": 2) <= ("pear": 2, "apple": 1);
("apple": 1, "pear": 2) <= ("pear": 2, "apple": 1, "banana" : 3);
("apple": 1, "pear": 2) <= ("apple": 1, "banana" : 3);
```

.Benefits

.Pitfalls

