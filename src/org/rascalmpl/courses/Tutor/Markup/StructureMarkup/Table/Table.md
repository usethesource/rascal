# Table

.Synopsis
Mark up for a table.

.Syntax
[source,subs="quotes"]
----
| _Header_~1~ | _Header_~2~ | ... |
| --- | --- | --- |
| _Entry_~11~  | _Entry_~12~  | ... |
| _Entry_~21~  | _Entry_~22~  | ... |

----

.Types

.Function

.Details

.Description

The simplest table starts and ends with `|===`, each row is separetd by an empty line and each column starts with `|`.
There are, hwoever, other formats and options to specify the formatting of cells and columns.
See http://asciidoctor.org/docs/user-manual/#tables for details.

.Examples

##  Example 1 

```rascal
| A  | B  | C |
| --- | --- | --- |
| 11 | 12 | 13 |
| 21 | 22 | 23 |

```

gives:

| A  | B  | C |
| --- | --- | --- |
| 11 | 12 | 13 |
| 21 | 22 | 23 |


##  Example 2 

A column specification may precede the table and specifies the number of columns with default alignment (left) and with
specified alignment: left (`<`), centered (`^`) or right (`>`).

```rascal
[cols="1*,^,1*"]
| A  | B  | C |
| --- | --- | --- |
| 11 | 12 | 13 |
| 21 | 22 | 23 |

```

gives (with column B centered):

[cols="1*,^,1*"]
| A  | B  | C |
| --- | --- | --- |
| 11 | 12 | 13 |
| 21 | 22 | 23 |


##  Example 3 

```rascal
[cols="2*,>"]
| A  | B  | C |
| --- | --- | --- |
| 11 | 12 | 13 |
| 21 | 22 | 23 |

```

gives (with column C right-aligned):

[cols="2*,>"]
| A  | B  | C |
| --- | --- | --- |
| 11 | 12 | 13 |
| 21 | 22 | 23 |


##  Example 4 

```rascal
| Operator    | Description |
| --- | --- |
| `A \| B` | Or operator |

```

gives (note the escaped `|` character in one table entry):

| Operator    | Description |
| --- | --- |
| `A \| B` | Or operator |


.Benefits
Table formatting are versatile and include, sizing, subtables, column spans, and more, see  http://asciidoctor.org/docs/user-manual/#tables.

.Pitfalls

