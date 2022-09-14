---
title: "DateTime LessThan"
keywords: "<"
---

#### Synopsis

Less than operator on datetime values.

#### Syntax

`Exp~1~ < Exp~2~`

#### Types

//

| `Exp~1~`      | `Exp~2~`      | `Exp~1~ < Exp~2~`  |
| --- | --- | --- |
| `datetime`     |  `datetime`    | `bool`               |


#### Function

#### Description

Yields `true` if the `datetime` value of Exp~1~ is earlier in time than the `datetime` value
of _Exp~2~_, and `false` otherwise.

#### Examples

```rascal-shell
$2010-07-14$ < $2010-07-15$;
$2011-07-15$ < $2010-07-14$;
```

#### Benefits

#### Pitfalls

