# Boolean NoMatch

.Synopsis
Negated [Boolean Match] operator.

.Index
!:=

.Syntax
`_Pat_ !:= _Exp_`

.Types

//

|             |         |                  |
| --- | --- | --- |
| `_Pat_`     | `_Exp_` |`_Pat_ !:= _Exp_` |
| [Patterns]  | `value` | `bool`           |


.Function

.Details

.Description
See ((Pattern Matching)) for an introduction to pattern matching and ((Patterns)) for a complete description.

.Examples
```rascal-shell
123 !:= 456;
[10, *n, 50] !:= [10, 20, 30, 40];
{10, *n, 50} !:= {40, 30, 30, 10};
```

.Benefits

.Pitfalls

