---
title: "Value Conditional"
keywords: "?,:"
---

#### Synopsis

Conditional expression on values.

#### Syntax

`Exp~1~ ? Exp~2~ : Exp~3~`

#### Types


| `Exp~1~`  | `Exp~2~` | `Exp~3~` | `Exp~1~ ? Exp~2~ : Exp~3~`  |
| --- | --- | --- | --- |
|   `bool`   | `T~2~`   | `T~3~`   | `lub(T~2~,T~3~)`             |


#### Function

#### Description

Yields the value of _Exp_~2~ if the value of _Exp_~1~ is `true` and the value of _Exp_~3~ otherwise.
The result type is the _least upper bound_ (also known as `lub`, see ((Declarations-StaticTyping))) of the types of _Exp_~2~ and _Exp_~3~.

#### Examples

```rascal-shell
( 3 > 2 ) ? 30 : 40;
( 3 < 2 ) ? "abc" : {3, 4};
```

#### Benefits

#### Pitfalls

