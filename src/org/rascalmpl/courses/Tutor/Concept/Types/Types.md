---
title: Types
---

#### Synopsis

Part of the synopsis that describes any types or typing rules introduced by this concept.

#### Syntax

```
#### Types

_MarkedText_
```

#### Types

#### Function

#### Description

The `Types` section describes any types that are involved in the concept that is described.
The description can be just text, but in many cases a table is useful to describe types.


#### Examples

Here is a type description of an if-then-else statement:

```
#### Types

| `Exp` | `if ( Exp ) Statement;`  |
``` | 
| `bool`  |  `void`                      |



| `Exp` | `Statement~1~` | `Statement~2~` | `if ( Exp ) Statement~1~ else Statement~2~;`  |
| --- | --- | --- | --- |
| `bool`  |  T~1~        | T~2~         | `lub(T~1~, T~2~)`                               |

----

The result will be displayed as:

#### Types

| `Exp` | `if ( Exp ) Statement;`  |
| --- | --- |
| `bool`  |  `void`                      |



| `Exp` | `Statement~1~` | `Statement~2~` | `if ( Exp ) Statement~1~ else Statement~2~;`  |
| --- | --- | --- | --- |
| `bool`  |  T~1~        | T~2~         | `lub(T~1~, T~2~)`                               |


#### Benefits

* The reader gets an immediate overview of how to use an expression or a function

#### Pitfalls

* There may be many different ways of using an operator, combinatorially many, which can not be explored visually in a list or a table.
* These type signatures are written manually and not generated from source (yet)
