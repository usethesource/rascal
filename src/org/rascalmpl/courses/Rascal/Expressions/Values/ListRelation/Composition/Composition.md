# ListRelation Composition

.Synopsis
Composition of two list relation values.

.Index
o

.Syntax
`_Exp_~1~ o _Exp_~2~`

.Types

//

|====
|`_Exp~1~_`             | `_Exp~2~_`            | `_Exp~1~_ o _Exp~2~_` 

| `lrel[_T~1~_, _T~2~_]` | `lrel[_T~2~_, _T~3~_]` | `lrel[_T~1~_, _T~3~_]`
|====

.Function

.Details

.Description
Returns the composition of two binary list relations.

.Examples
```rascal-shell
[<1,10>, <2,20>, <3,15>] o [<10,100>, <20,200>];
```

.Benefits

.Pitfalls
We use the letter `o` as operator and this may conflict other defined names.

