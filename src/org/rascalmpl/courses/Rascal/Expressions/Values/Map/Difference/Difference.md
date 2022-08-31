# Map Difference

.Synopsis
The difference between two maps.

.Index
-

.Syntax
`Exp~1~ - Exp~2~`

.Types


| `_Exp~1~_`             |  `_Exp~2~_`             | `_Exp~1~_ - _Exp~2~_`                             |
| --- | --- | --- |
| `map[_TK~1~_, _TV~1~_]` |  `map[_TK~2~_, _TV~2~_]` | `map[lub(_TK~1~_,_TK~2~_),lub(_TK~1~_,_TK~2~_)]`   |


.Function

.Details

.Description
The result is the difference of the two map values of _Exp_~1~ and _Exp_~2~,
i.e. a map with all pairs in _Exp_~1~ that do have a key that does not occur in _Exp_~2~.

.Examples
```rascal-shell
("apple": 1, "pear": 2) - ("banana": 3, "apple": 4);
```

.Benefits

.Pitfalls

