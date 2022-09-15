---
title: Field Projection
keywords:
  - <
  - >

---

#### Synopsis

Projection of tuple.

#### Syntax

`Exp < Field~1~, Field~2~ ... >`

#### Types

#### Function

#### Description

_Exp_ should evaluate to a tuple or relation, and _Field_~i~ should be a field name or an integer constant
 that refers to elements in the order in which they occur in the original value (counting from 0). 

#### Examples

Suppose we have a relation with traffic information that records the name of the day, the day number, and the length of the traffic jams at that day.
```rascal-shell
rel[str day, int daynum, int length] Traffic = 
{<"mon", 1, 100>, <"tue", 2, 150>, <"wed", 3, 125>, 
 <"thur", 4, 110>, <"fri", 5, 90>};
Traffic<length,daynum>;
Traffic<2,day>;
```
Field projection thus selects parts from a larger value that has a fixed number of parts. The selection is based on position and not on value and can be used to completely reorder or remove the parts of a larger value.

#### Benefits

#### Pitfalls

