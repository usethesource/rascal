# Value Conditional

.Synopsis
Conditional expression on values.

.Index
? :

.Syntax
`Exp~1~ ? Exp~2~ : Exp~3~`

.Types


| `_Exp~1~_`  | `_Exp~2~_` | `_Exp~3~_` | `_Exp~1~_ ? _Exp~2~_ : _Exp~3~_`  |
| --- | --- | --- | --- |
|   `bool`   | `_T~2~_`   | `_T~3~_`   | `lub(_T~2~_,_T~3~_)`             |


.Function

.Details

.Description
Yields the value of _Exp_~2~ if the value of _Exp_~1~ is `true` and the value of _Exp_~3~ otherwise.
The result type is the _least upper bound_ (also known as `lub`, see ((Declarations-StaticTyping))) of the types of _Exp_~2~ and _Exp_~3~.

.Examples
```rascal-shell
( 3 > 2 ) ? 30 : 40;
( 3 < 2 ) ? "abc" : {3, 4};
```

.Benefits

.Pitfalls

