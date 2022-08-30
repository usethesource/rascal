# Map Subscription

.Synopsis
Retrieve a value by its key in map.

.Index
[ ]

.Syntax
`_Exp_~1~ [ _Exp_~2~ ]`

.Types

| `_Exp~1~_`           | `_Exp~2~_` | `_Exp~1~_ [ _Exp~2~_ ]`  |
| --- | --- | --- |
| `map[_T~1~_, _T~2~_]` | `_T~1~_`   | `_T~2~_`                |


.Function

.Details

.Description
Map subscription uses the value of _Exp_~2~ as key in the map value of _Exp_~1~ and returns the associated value.
If this key does not occur in the map, the exception `NoSuchKey` is thrown.

.Examples
```rascal-shell,error
```
Introduce a map, assign it to `colors`, and retrieve the element with index `"trumps"`:
```rascal-shell,continue,error
colors = ("hearts":"red", "clover":"black", 
          "trumps":"black", "clubs":"red");
colors["trumps"];
```
Explore some erroneous subscription expressions:
```rascal-shell,continue,error
colors[0];
colors["square"];
```

.Benefits

.Pitfalls

