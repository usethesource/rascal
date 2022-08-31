#  Tuple Equal

.Synopsis
Equality operator on tuple values.

.Index
==

.Syntax
`Exp~1~ == Exp~2~`

.Types


| `Exp~1~`                      |  `Exp~2~`                      | `Exp~1~ == Exp~2~` |
| --- | --- | --- |
| `tuple[ T~11~, T~12~, ... ]` |  `tuple[ T~21~, T~22~, ... ]` | `bool`              |


.Function

.Details

.Description
Yields `true` if both tuples are identical and `false` otherwise.

.Examples
```rascal-shell
<1, "abc", true> == <1, "abc", true>;
```

.Benefits

.Pitfalls

