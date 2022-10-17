---
title: Bullet Lists
---

#### Synopsis

A (possible nested) list of bullet points.

#### Syntax

Here `␠` is used to mark a space character:

*  `* MarkedText`
*  `␠␠␠* MarkedText`
*  `␠␠␠␠␠␠* MarkedText`
*  ...

#### Types

#### Function

#### Description

Bullet lists create, possibly nested, lists of points.
The number of `*` characters determines the nesting level of a (sub)list.

A list item ends with:

*  the start of a new list item.
*  an empty line.



#### Examples

The input

```
* First item.
* Second item.
```

will produce:

*  First item.
*  Second item.


The input

```
* First item.
   * First subitem.
   * Second subitem.
* Second item.
```

will produce:

*  First item.
   *  First subitem.
   *  Second subitem.
*  Second item.

#### Benefits

* ((BulletLists)) and ((NumberedLists)) can be nested under eachother.

#### Pitfalls

* An empty line is required _after_ ((BulletLists)).

