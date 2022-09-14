---
title: "Boolean Match"
keywords: ":="
---

.Synopsis
Match a pattern against an expression.

.Syntax
`Pat := Exp`

.Types

//

| `Pat`      | `Exp` |`Pat := Exp` |
| --- | --- | --- |
| ((Patterns)) | `value` | `bool`         |


.Function

.Details

.Description
See ((Pattern Matching)) for an introduction to pattern matching and ((Patterns)) for a complete description.

.Examples
```rascal-shell
123 := 456;
[10, *n, 50] := [10, 20, 30, 40, 50];
{10, *int n, 50} := {50, 40, 30, 30, 10};
```
.Benefits

.Pitfalls

