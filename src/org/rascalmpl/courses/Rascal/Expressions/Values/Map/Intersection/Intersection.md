# Map Intersection

.Synopsis
Intersection of two maps.

.Index
&

.Syntax
`_Exp_~1~ & _Exp_~2~`

.Types


|====
| `_Exp~1~_`    |  `_Exp~2~_`      | `_Exp~1~_ & _Exp~2~_`     

| `map[_T~1~1_, _T12_]` |  `set[_T~2~_]`   | `set[lub(_T~1~_,_T~2~_)]` 
|====

.Function

.Details

.Description
Returns the intersection of the two map values of _Exp_~1~ and _Exp_~2~, i.e., a map that contains the key/value pairs that
occur in both maps.

.Examples
```rascal-shell
("apple": 1, "pear": 2) & ("banana": 3, "apple": 1);
("apple": 1, "pear": 2) & ("banana": 3, "apple": 4);
```

.Benefits

.Pitfalls

