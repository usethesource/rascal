# Set Union

.Synopsis
Union of two set values.

.Index
+

.Syntax

.Types

| `_Exp~1~_`    |  `_Exp~2~_`    | `_Exp~1~_ + _Exp~2~_`       |
| --- | --- | --- |
| `set[_T~1~_]` |  `set[_T~2~_]` | `set[lub(_T~1~_,_T~2~_)]`   |


.Description
The `+` operator computes set union if both operands are sets. If one of the operands is not a set, it acts as ((Set-Insert)) instead.

.Examples
```rascal-shell
{1, 2, 3} + {4, 5, 6};
{1,2,3} + {2,3,4};
{1, 2, 3} + {3};
{2} + { 2, 3, 4};
```

