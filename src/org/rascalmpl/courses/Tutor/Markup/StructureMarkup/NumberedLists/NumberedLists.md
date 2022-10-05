---
title: Numbered Lists
---

#### Synopsis

A (possibly nested) list of numbered points.

#### Syntax

Here `␠` is used to represent a visible space:
* ``1. MarkedText``
* ``␠␠␠1. MarkedText``
* ``␠␠␠␠␠␠1. MarkedText``

#### Types

#### Function

#### Description

Numbered Lists create, possibly nested, lists of numbered points.
They start with a number followed by a period (`1.`). The number of spaces indicates the nesting level, every 3 spaces represents one level.

A list item ends with:

*  the start of a new list item.
*  an empty line.

((BulletLists)) and ((NumberedLists)) can be mixed.

#### Examples

The input

```
1. First item.
1. Second item.
```

will produce:

1. First item.
1. Second item.

The input

```
1. First item.
   1.  First subitem.
   1.  Second subitem.
1. Second item
```

will produce:

1. First item.
   1.  First subitem.
   1.  Second subitem.
1. Second item

#### Benefits

#### Pitfalls

* An empty line is required _after_ a NumberedList

