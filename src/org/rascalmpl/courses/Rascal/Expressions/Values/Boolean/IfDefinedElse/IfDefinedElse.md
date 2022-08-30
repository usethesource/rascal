# Boolean IfDefinedElse

.Synopsis
Test whether expression has a defined value, otherwise provide alternative.

.Index
?

.Syntax
`_Exp_~1~ ? _Exp_~2~`

.Types

//

| `_Exp~1~_` | `_Exp~2~_` | `_Exp~1~_ ? _Exp~2~_` |
| --- | --- | --- |
| `_T~1~_`   | `_T~2~_`   |  `_T~2~_ <: _T~1~_`  |


.Function

.Details

.Description
If no exception is generated during the evaluation of _Exp_~1~, the result of `_Exp_~1~ ? _Exp_~2~` is the value of _Exp_~1~.
Otherwise, it is the value of _Exp_~2~.

Also see ((Boolean-IsDefined)) and ((Assignment)).

.Examples
This test can, for instance, be used to handle the case that a certain key value is not in a map:
```rascal-shell,error
T = ("a" : 1, "b" : 2);
```
Trying to access the key `"c"` will result in an error:
```rascal-shell,continue,error
T["c"];
```
Using the `?` operator, we can write:
```rascal-shell,continue,error
T["c"] ? 0;
```
This is very useful, if we want to modify the associated value, but are not sure whether it exists:
```rascal-shell,continue,error
T["c"] ? 0 += 1;
```
Another example using a list:
```rascal-shell,continue,error
L = [10, 20, 30];
L[4] ? 0;
```
It is, however, not possible to assign to index positions outside the list.

.Benefits

.Pitfalls

