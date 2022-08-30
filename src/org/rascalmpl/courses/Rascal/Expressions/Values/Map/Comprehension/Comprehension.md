# Map Comprehension

.Synopsis
A map comprehension generates a map value.

.Index
( | )

.Syntax
`( _Exp_~1~ : _Exp_~2~ | _Gen_~1~, _Gen_~2~, ... )`

.Types


|            |            |                         |                             |
| --- | --- | --- | --- |
| `_Exp~1~_` | `_Exp~2~_` | `( _Exp~1~_ : _Exp~2~_ \| _Gen~1~_, _Gen~2~_, ... )`  |
| `_T~1~_`   | `_T~2~_`   | `map[_T~1~_, _T~2~_]`                               |


.Function

.Details

.Description
A map comprehension consists of a number of two contributing expressions _Exp_~1~ (for key values), 
and _Exp_~2~ (the values associated with those key values) and a number of
generators _Gen_~1~, _Gen_~2~, _Gen_~3~, ... that are evaluated as described in ((Expressions-Comprehensions)).

.Examples
```rascal-shell
```
Introduce a map of `fruits`:
```rascal-shell,continue
fruits = ("pear" : 1, "apple" : 3, "banana" : 0, "berry" : 25, "orange": 35);
import String;
```
Use a map comprehension to filter fruits with a name of at most 5 characters:
```rascal-shell,continue
(fruit : fruits[fruit] | fruit <- fruits, size(fruit) <= 5);
```
Use a map comprehension to filter fruits with an associated value larger than 10:
```rascal-shell,continue
(fruit : fruits[fruit] | fruit <- fruits, fruits[fruit] > 10);
```

.Benefits

.Pitfalls

