# Bullet Lists

.Synopsis
A (possible nested) list of bullet points.

.Syntax
*  `* _MarkedText_`
*  `** _MarkedText_`
*  `\*** _MarkedText_`
*  ...

.Types

.Function

.Details

.Description
Bullet lists create, possibly nested, lists of points.
The number of `*` characters determines the nesting level of a (sub)list.

A list item ends with:

*  the start of a new list item.
*  an empty line.

Bullet Lists and ((Numbered Lists)) can be mixed.
See http://asciidoctor.org/docs/user-manual/#unordered-lists for the precise rules.

.Examples
The input

[source]
----
* First item.
* Second item.
----

will produce:

*  First item.
*  Second item.


The input

[source]
----
* First item.
  ** First subitem.
  ** Second subitem.
* Second item.
----

will produce:

*  First item.
   **  First subitem.
   **  Second subitem.
*  Second item.

.Benefits

.Pitfalls
An empty line is required before and after a BulletList.

