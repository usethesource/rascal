---
title: Bullet Lists
---

#### Synopsis

A (possible nested) list of bullet points.

#### Syntax

*  `* MarkedText`
*  `** MarkedText`
*  `\*** MarkedText`
*  ...

#### Types

#### Function

#### Description

Bullet lists create, possibly nested, lists of points.
The number of `*` characters determines the nesting level of a (sub)list.

A list item ends with:

*  the start of a new list item.
*  an empty line.

Bullet Lists and ((Numbered Lists)) can be mixed.
(((TODO:See http://asciidoctor.org/docs/user-manual/#unordered-lists for the precise rules.)))

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
  ** First subitem.
  ** Second subitem.
* Second item.
```

will produce:

*  First item.
   **  First subitem.
   **  Second subitem.
*  Second item.

#### Benefits

#### Pitfalls

An empty line is required before and after a BulletList.

