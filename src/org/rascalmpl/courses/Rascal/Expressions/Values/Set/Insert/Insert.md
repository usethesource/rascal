# Set Insert

.Synopsis
Add an element to a set.

.Index
+

.Syntax
`_Exp_~1~ + _Exp_~2~`

.Types


|====
| `_Exp~1~_`    |  `_Exp~2~_`    | `_Exp~1~_ + _Exp~2~_`      

| `set[_T~1~_]` |  `_T~2~_`      | `set[lub(_T~1~_,_T~2~_)]`  
| `_T~1~_`      |  `set[_T~2~_]` | `set[lub(_T~1~_,_T~2~_)]`  
|====

.Function

.Details

.Description

The `+` operator will add elements to sets.

.Examples
```rascal-shell
{1, 2, 3} + 4;
1 + { 2, 3, 4};
{1} + 1;
1 + {1};
```

.Benefits

.Pitfalls

*  if both operands of `+` are a set then it acts as ((Set-Union)).

