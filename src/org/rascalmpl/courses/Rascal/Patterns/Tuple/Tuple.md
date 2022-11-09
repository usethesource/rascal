---
title: Tuple Pattern
keywords:
  - "<"
  - ">"

---

#### Synopsis

Tuple in abstract pattern.

#### Syntax

```rascal
<Pat~1~, ..., Pat~n~>
```

#### Types

#### Function

#### Description

A tuple pattern matches a tuple value, provided that _Pat_~1~, _Pat_~2~, ..., _Pat_~n~  match the elements of that tuple in order. Any variables bound by nested patterns are available from left to right.

#### Examples

```rascal-shell
import IO;
if(<A, B, C> := <13, false, "abc">)
   println("A = <A>, B = <B>, C = <C>");
```

#### Benefits

#### Pitfalls

