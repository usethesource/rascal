---
title: Tuple Subscription
keywords:
  - [
  - ]

---

#### Synopsis

Retrieve a tuple field by its index position.

#### Syntax

`Exp~1~ [ Exp~2~ ]`

#### Types

#### Function

#### Description

Subscription retrieves the tuple element with index _Exp_~2~ from the tuple value of _Exp_~1~.

#### Examples

Introduce a tuple, assign it to T and retrieve the element with index 0:
```rascal-shell
T = <"mon", 1>;
T[0];
```

#### Benefits

#### Pitfalls

