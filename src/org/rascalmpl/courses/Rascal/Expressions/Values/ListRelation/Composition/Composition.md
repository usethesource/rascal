# ListRelation Composition

.Synopsis
Composition of two list relation values.

.Index
o

.Syntax
`Exp~1~ o Exp~2~`

.Types

//

|`Exp~1~`             | `Exp~2~`            | `Exp~1~ o Exp~2~`  |
| --- | --- | --- |
| `lrel[T~1~, T~2~]` | `lrel[T~2~, T~3~]` | `lrel[T~1~, T~3~]` |


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

