---
title: "Map Union"
keywords: "+"
---

#### Synopsis

Union of two maps.

#### Syntax

`Exp~1~ + Exp~2~`

#### Types

| `Exp~1~`             |  `Exp~2~`             | `Exp~1~ + Exp~2~`                             |
| --- | --- | --- |
| `map[TK~1~, TV~1~]` |  `map[TK~2~, TV~2~]` | `map[lub(TK~1~,TK~2~),lub(TK~1~,TK~2~) ]`   |


#### Function

#### Description

The result is the union of the two map values of _Exp_~1~ and _Exp_~2~.
If they have a pair with the same key in common, that key will be associated
in the union with the value associated with that key in _Exp_~2~.

#### Examples

```rascal-shell
("apple": 1, "pear": 2) + ("banana": 3, "kiwi": 4);
("apple": 1, "pear": 2) + ("banana": 3, "apple": 4);
```

#### Benefits

Map union is very suited for representing environment composition in interpreters.

#### Pitfalls

