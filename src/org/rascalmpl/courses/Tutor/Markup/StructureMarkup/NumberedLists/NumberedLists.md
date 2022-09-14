---
title: Numbered Lists
---

.Synopsis
A (possibly nested) list of numbered points.

.Syntax

* `Number MarkedText`
* `. MarkedText`
* `.. MarkedText`
* `\... MarkedText`
* ...


.Types

.Function

.Description
Numbered Lists create, possibly nested, lists of numbered points.
They either start with a period (`.`) or a number. The number of periods indicates the nesting level.

A list item ends with:

*  the start of a new list item.
*  an empty line.

((Bullet Lists)) and Numbered Lists can be mixed.
See http://asciidoctor.org/docs/user-manual/#ordered-lists for the precise rules.

.Examples
The input

```
. First item.
. Second item.
```

will produce:

. First item.
. Second item.

The input

```
. First item.
  ..  First subitem.
  ..  Second subitem.
. Second item
```

will produce:

. First item.
  ..  First subitem.
  ..  Second subitem.
. Second item

.Benefits

.Pitfalls
An empty line is required before and after a NumberedList.

