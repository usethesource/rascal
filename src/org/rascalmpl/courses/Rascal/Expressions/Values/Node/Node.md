# Node

.Synopsis
Node values.

.Index
( )

.Syntax

`_Exp_~0~ ( _Exp_~1~, _Exp_~2~, ..., _FieldName~1~_ = _Expr~1_, _FieldName~2~ = _Expr~2_, ... )`

.Types

[cols="10,10,10,10,60"]
|====
|`_Exp~0~_`  | `_Exp~1~_` | `_Exp~2~_` | ... | `_Exp~0~_ ( _Exp~1~_, _Exp~2~_, ... )` 

| `str`      | `value`    | `value`    | ... | `node`                              
|====

.Usage

.Function

.Details

.Description

Values of type `node` represent untyped trees and are constructed as follows:

* the string value of _Exp~0~_ is the node name;
* zero or more expressions of type `value` are the node\'s children.
* optionally, unordered named fields can be added as well.

The following are provided for nodes:
(((TOC)))

.Examples

A node with name "my_node" and three arguments:
[source,rascal-shell,continue]
----
"my_node"(1, true, "abc");
----
A nested node structure:
[source,rascal-shell,continue]
----
"my_node1"(1, "my_node2"(3.5, ["a", "b", "c"]), true);
----
A node with named fields:
[source,rascal-shell,continue]
----
"my_node2"(1,2,size=2,age=24);
----

.Benefits

* nodes are untyped and can be used to quickly import untyped data into Rascal
* pattern matching on nodes is quite expressive

.Pitfalls

* the lack of types at run-time makes pattern matching on node possibly inaccurate (you might match more than you think)
