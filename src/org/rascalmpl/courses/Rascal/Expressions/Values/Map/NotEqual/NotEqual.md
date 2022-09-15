---
title: Map NotEqual
keywords:
  - "!="

---

#### Synopsis

Not equal operator on map values.

#### Syntax

`Exp~1~ != Exp~2~`

#### Types

| `Exp~1~`            |  `Exp~2~`             | `Exp~1~ != Exp~2~`  |
| --- | --- | --- |
| `map[TK~1~,TV~2~]` |  `map[TK~2~, TV~2~]` | `bool`                |


#### Function

#### Description

Yields `true` if both arguments contain different key/value pairs, and `false` otherwise.

#### Examples

```rascal-shell
("apple": 1, "pear": 2) != ("apple": 1, "banana": 3);
("apple": 1, "pear": 2) != ("pear": 2, "apple": 1);
```

#### Benefits

#### Pitfalls

