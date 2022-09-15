---
title: DateTime GreaterThan
keywords:
  - ">"

---

#### Synopsis

Greater than operator on datetime values.

#### Syntax

`Exp~1~ > Exp~2~`

#### Types

//

| `Exp~1~`      | `Exp~2~`      | `Exp~1~ > Exp~2~`  |
| --- | --- | --- |
| `datetime`     |  `datetime`    | `bool`               |


#### Function

#### Description

Yields `true` if the `datetime` value of Exp~1~ is later in time than the `datetime` value
of _Exp_~2~, and `false` otherwise.

#### Examples

```rascal-shell
$2010-07-15$ > $2010-07-14$;
$2011-07-15$ > $2010-07-15$;
```

#### Benefits

#### Pitfalls

