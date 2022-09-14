---
title: "Set Comprehension"
keywords: "{,|,}"
---

.Synopsis
A set comprehension generates a set value.

.Syntax
`{ Exp~1~, Exp~2~, ... | Gen~1~, Gen~2~, ... }`

.Types


| `Exp~1~` | `Exp~2~` | ... | `{ Exp~1~, Exp~2~, ... \| Gen~1~, Gen~2~, ... }`  |
| --- | --- | --- | --- | --- |
| `T~1~`   | `T~2~`   | ... | `set[ lub( T~1~, T~2~, ... ) ]`                    |


.Function

.Description
A set comprehension consists of a number of contributing expressions _Exp_~1~, _Exp_~2~, ... and a number of
generators _Gen_~1~, _Gen_~2~, _Gen_~3~, ... that are evaluated as described in ((Expressions-Comprehensions)).

.Examples
```rascal-shell
{ N * N | int N <- [0 .. 10]};
{ N * N | int N <- [0 .. 10], N % 3 == 0};
```

.Benefits

.Pitfalls

