# Boolean Implication

.Synopsis
The _implication_ operator on Boolean values.

.Index
==>

.Syntax
`_Exp_~1~ ==> _Exp_~2~`

.Types

//

|====
| `_Exp~1~_` | `_Exp~2~_`  | `_Exp~1~_ ==> _Exp~2~_` 

| `bool`       | `bool`         | `bool` 
|====

.Function

.Details

.Description
The _implication_ operator on Boolean values defined as follows:

|====
| `_Exp~1~_` | `_Exp~2~_`  | `_Exp~1~_ ==> _Exp~2~_` 

| `true`       | `true`         | `true` 
| `true`       | `false`         | `false` 
| `false`       | `true`         | `true` 
| `false`       | `false`         | `true` 
|====

Boolean operators have _short circuit_ semantics:  only those operands are evaluated that are needed to compute the result. In the case of the `==>` operator, the result is `true` if `_Exp_~1~` evaluates to `false`, otherwise `_Exp_~2~` is evaluated to determine the result.

.Examples
```rascal-shell
false ==> true;
```


.Benefits

.Pitfalls

