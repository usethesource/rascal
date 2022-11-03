---
title: Constructor FieldSelection
keywords:
  - "."
  - fields
  - getter
  - get
  - keyword fields
  - default fields
---

#### Synopsis

Select a field from a constructor by its field name.

#### Syntax

`Exp . Name`

#### Types


| `Exp`                                 | `Name` | `Exp . Name` |
| --- | --- | --- |
|`cons(T~1~ L~1~, T~2~ L~2~, ... )` |  `L~i~` | `T~i~`         |


#### Function

#### Description

Field selection applies to constructors with named elements such as fields and keyword fields.
_Exp_ should evaluate to a tuple with field _Name_ and returns the value of that field.
_Name_ stands for itself and is not evaluated.

Because the types of field names are known statically, field projection for constructors produces
values of precise types. This is unlike ((Node-FieldSelection)) where the resulting type is always `value`.

Constructor keyword fields have `default` expressions which can be dependent on each other and
on the positional fields (by name). They are computed lazily upon request.

#### Examples

```rascal-shell
data Example = example(int key, str val="<key>"); // <1>
t = example(42);
t.key = 2;
t.val
t.key += 2;
t.val
```

* <1> `val` is a keyword field with a default value computed from the positional field `key`.

#### Benefits

* constructors with fields are a versatile way of modeling and storing hierarchical data

#### Pitfalls

