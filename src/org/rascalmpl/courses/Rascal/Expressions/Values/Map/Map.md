---
title: Map
keywords:
  - "("
  - ":"
  - ")"

---

#### Synopsis

Map values.

#### Syntax

`( KeyExp~1~ : ValExp~1~, KeyExp~2~ : ValExp~2~, ... )`

#### Types


| `KeyExp~1~` | `ValExp~1~` | `KeyExp~2~` | `ValExp~2~` | ... | `( KeyExp~1~ : ValExp~1~, KeyExp~2~ : ValExp~2~, ... )`   |
| --- | --- | --- | --- | --- | --- |
| `TK~1~`     |  `TV~1~`    |  `TK~2~`    | `TV~2~`     | ... | `map[lub(TK~1~, TK~2~, ... ) , lub(TV~1~, TV~2~, ... )]`  |


#### Usage

#### Function

#### Description

A map is a set of key/value pairs and has the following properties:

*  Key and value may have different static types.

*  A key can only occur once.


Maps resemble functions rather than relations in the sense that only a single value can be associated with each key.

The following functions are provided for maps:

(((TOC)))

#### Examples

```rascal-shell
("pear" : 1, "apple" : 3, "banana" : 0);
```

#### Benefits

#### Pitfalls

