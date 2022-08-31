# Map Equal

.Synopsis
Equality operator on maps.

.Index
==

.Syntax
`Exp~1~ == Exp~2~`

.Types

| `_Exp~1~_`            |  `_Exp~2~_`             | `_Exp~1~_ == _Exp~2~_`  |
| --- | --- | --- |
| `map[_TK~1~_,_TV~2~_]` |  `map[_TK~2~_, _TV~2~_]` | `bool`                |


.Function

.Details

.Description
Yields `true` if both arguments contain the same key/value pairs, and `false` otherwise.

.Examples
```rascal-shell
("apple": 1, "pear": 2) == ("pear": 2, "apple": 1);
("apple": 1, "pear": 2) == ("apple": 1, "banana": 3) 
```

.Benefits

.Pitfalls

