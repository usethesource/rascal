---
title: Filter
---

#### Synopsis

Filter values in a ((List-Comprehension))], ((Set-Comprehension)) or ((Map-Comprehension)).

#### Syntax

`Exp`

#### Types

//

| `Exp`  |
| --- |
| `bool`   |


#### Function

#### Description

A  filter is a boolean-valued expression. 
If the evaluation of the filter gives `true` this indicates that the current combination of generated values up 
to this filter is still desired and execution continues with subsequent generators. 
If the evaluation gives `false` this indicates that the current combination of values is undesired, 
and that another combination should be tried by going back to the previous generator.

#### Examples

Adding a filter to a comprehension, may restrict the values that are included in the result of the comprehension:
```rascal-shell
[ X * X | int X <- [1, 2, 3, 4, 5, 6] ];
[ X * X | int X <- [1, 2, 3, 4, 5, 6], X % 3 == 0 ];
```
Filters can also be applied to values produced by several generators:
```rascal-shell,continue
[<X, Y> | int X <- [0 .. 10], int Y <- [0 .. 10], X + Y == 10]
```

#### Benefits

#### Pitfalls

