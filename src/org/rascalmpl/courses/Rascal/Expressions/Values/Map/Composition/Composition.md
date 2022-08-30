# Map Composition

.Synopsis
Composition of two map values.

.Index
o

.Syntax
`_Exp_~1~ o _Exp_~2~`

.Types

|====
|`_Exp~1~_`            | `_Exp~2~_`           | `_Exp~1~_ o _Exp~2~_` 

| `map[_T~1~_, _T~2~_]` | `map[_T~2~_, _T~3~_]` | `map[_T~1~_, _T~3~_]` 
|====

.Function

.Details

.Description
Returns the composition of two maps.

.Examples
```rascal-shell
import Map;
("one" : 1, "two" : 2) o (1 : 10, 2 : 20);
```

.Benefits

.Pitfalls
We use the letter `o` as operator and this may conflict other defined names.
