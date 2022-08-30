# Relation Join

.Synopsis
Join two relation values.

.Index
join

.Syntax
`_Exp_~1~ join _Exp_~2~`

.Types


| `_Exp~1~_`                            |  `_Exp~2~_`                            | `_Exp~1~_ join _Exp~2~_`                                             |
| --- | --- | --- |
| `rel[ _T~11~_, _T~12~_, _T~13~_, ... ]` |  `rel[ _T~21~_, _T~22~_, _T~23~_, ... ]` | `rel[ _T~11~_, _T~12~_, _T~13~_, ..., _T~21~_, _T~22~_, _T~23~_, ... ]`  |


.Function

.Details

.Description
Relation resulting from the natural join of the relation values of the two arguments.
This relation contains tuples that are the result from concatenating the elements from both arguments.

.Examples
```rascal-shell
{<1,2>, <10,20>} join {<2,3>};
{<1,2>} join {3, 4};
{<1,2>, <10,20>} join {<2,3>, <20,30>};
```

.Benefits

.Pitfalls

