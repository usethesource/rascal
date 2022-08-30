# Types

.Synopsis
Part of the synopsis that describes any types or typing rules introduced by this concept.

.Syntax
[source,subs="verbatim,quotes"]
----
.Types
_MarkedText_
----

.Types

.Function

.Details

.Description
The `Types` section describes any types that are involved in the concept that is described.
The description can be just text, but in many cases a table is useful to describe types.


.Examples
Here is a type description of an if-then-else statement:

[source,subs="verbatim,quotes"]
----
.Types
| `_Exp_` | `if ( _Exp_ ) _Statement_;`  |
| --- | --- |
| `bool`  |  `void`                      |



| `_Exp_` | `_Statement~1~_` | `_Statement~2~_` | `if ( _Exp_ ) _Statement~1~_ else _Statement~2~_;`  |
| --- | --- | --- | --- |
| `bool`  |  _T~1~_        | _T~2~_         | `lub(_T~1~_, _T~2~_)`                               |

----

The result will be displayed as:

.Types
| `_Exp_` | `if ( _Exp_ ) _Statement_;`  |
| --- | --- |
| `bool`  |  `void`                      |



| `_Exp_` | `_Statement~1~_` | `_Statement~2~_` | `if ( _Exp_ ) _Statement~1~_ else _Statement~2~_;`  |
| --- | --- | --- | --- |
| `bool`  |  _T~1~_        | _T~2~_         | `lub(_T~1~_, _T~2~_)`                               |


.Benefits

.Pitfalls

