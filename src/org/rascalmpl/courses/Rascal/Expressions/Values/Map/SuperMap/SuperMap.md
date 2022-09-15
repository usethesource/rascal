---
title: Map SuperMap
keywords:
  - >=

---

#### Synopsis

Supermap operator on map values.

#### Syntax

`Exp~1~ >= Exp~2~`

#### Types

| `Exp~1~`            |  `Exp~2~`             | `Exp~1~ >= Exp~2~`  |
| --- | --- | --- |
| `map[TK~1~,TV~2~]` |  `map[TK~2~, TV~2~]` | `bool`                |


#### Function

#### Description

Yields `true` if all key/value pairs in the map value of _Exp_~2~ occur in the map value _Exp_~1~
or the values of _Exp_~1~ and _Exp_~2~ are equal, and `false` otherwise.

#### Examples

```rascal-shell
("pear": 2, "apple": 1) >= ("apple": 1, "pear": 2);
("pear": 2, "apple": 1, "banana" : 3) >= ("apple": 1, "pear": 2);
("apple": 1, "banana" : 3) >= ("apple": 1, "pear": 2);
```

#### Benefits

#### Pitfalls

