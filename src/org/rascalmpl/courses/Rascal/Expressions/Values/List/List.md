---
title: List
keywords:
  - "["
  - "]"

---

#### Synopsis

List values.

#### Syntax

`[ Exp~1~, Exp~2~, ... ]`

#### Types

//


|  `Exp~1~` |  `Exp~2~` |  ...  |  `[ Exp~1~, Exp~2~, ... ]`    |
| --- | --- | --- | --- |
| `T~1~`    | `T~2~`    |  ...  |  `list[lub(T~1~, T~2~, ... )]`  |


#### Usage

#### Function

#### Description

A list is an ordered sequence of values and has the following properties:

*  All elements have the same static type.
*  The order of the elements matters.
*  A list may contain an element more than once.


The type of a list has the form `list[T]`,
where `T` is an arbitrary type.

When a value or variable of type list occurs inside a list, that list value is inserted as list element.
To achieve _splicing_ of these elements, i.e., the insertion of the elements of the list value rather than the whole list,
it has to be prefixed by the splice operator `*`.

The following operators are provided on list:
(((TOC)))

There are also [library functions]((Library:module:List)) available for List.

#### Examples

```rascal-shell
[1, 2, 3];
[<1,10>, <2,20>, <3,30>];
[1, "b", 3];
[<"a",10>, <"b",20>, <"c",30>];
[["a", "b"], ["c", "d", "e"]];
```
List splicing works as follows: by prefixing `L` by the splice operator, its elements are included as elements in the enclosing list:
```rascal-shell
L = [1, 2, 3];
[10, L, 20];
[10, *L, 20];
```

#### Benefits

#### Pitfalls

