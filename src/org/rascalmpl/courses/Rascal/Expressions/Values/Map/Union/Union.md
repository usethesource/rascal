# Map Union

.Synopsis
Union of two maps.

.Index
+

.Syntax
`_Exp_~1~ + _Exp_~2~`

.Types

|====
| `_Exp~1~_`             |  `_Exp~2~_`             | `_Exp~1~_ + _Exp~2~_`                            

| `map[_TK~1~_, _TV~1~_]` |  `map[_TK~2~_, _TV~2~_]` | `map[lub(_TK~1~_,_TK~2~_),lub(_TK~1~_,_TK~2~_) ]`  
|====

.Function

.Details

.Description
The result is the union of the two map values of _Exp_~1~ and _Exp_~2~.
If they have a pair with the same key in common, that key will be associated
in the union with the value associated with that key in _Exp_~2~.

.Examples
```rascal-shell
("apple": 1, "pear": 2) + ("banana": 3, "kiwi": 4);
("apple": 1, "pear": 2) + ("banana": 3, "apple": 4);
```

.Benefits
Map union is very suited for representing environment composition in interpreters.

.Pitfalls

