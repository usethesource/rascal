# ListRelation

.Synopsis
ListRelation values.

.Index
[ ] < >

.Syntax
`[ < _Exp_~11~, _Exp_~12~, ... > , < _Exp_~21~, _Exp_~22~, ... > , ... ]`

.Types

//

[cols="20,20,20,40"]
|====
| `_Exp~11~_` |  `_Exp~12~_` |  ...  | `{ < _Exp~11~_, _Exp~12~_, ... > , ... }`  

| `_T~1~_`    |    `_T~2~_`  |  ...  |  `lrel[_T~1~_, _T~2~_, ... ]`              
|====

.Usage

.Function

.Details

.Description
A list relation is a list of elements with the following property:

*  All elements have the same static tuple type.


ListRelations are thus nothing more than lists of tuples, but since they are used so often we provide a shorthand notation for them.
ListRelations are represented by the type `lrel[_T_~1~ _L_~1~, _T_~2~ _L_~2~, ... ]`, where _T_~1~, _T_~2~, ... are arbitrary types and
_L_~1~, _L_~2~, ... are optional labels. It is a shorthand for `list[tuple[_T_~1~ _L_~1~, _T_~2~ _L_~2~, ... ]]`.

An n-ary list relation with m tuples is denoted by
 `[< _E_~11~, _E_~12~, ..., _E_~1n~>,< _E_~21~, _E_~22~, ..., _E_~2n~>, ..., < _E_~m1~, _E_~m2~, ..., _E_~mn~>]`, 
where the _E_~ij~ are expressions that yield the desired element type _T_~i~.

Since list relations are a form of list all operations (see ((Values-List))) and functions
(see ((Prelude-List))) are also applicable to relations.

The following additional operators are provided for list relations:
loctoc::[1]

There are also [library functions]((Libraries:Prelude-ListRelation)) available for ListRelation.


.Examples
[source,rascal-shell]
----
[<1,10>, <2,20>, <3,30>]
----
instead of lrel[int,int] we can also give `list[tuple[int,int]]` as type of the above expression
remember that these types are interchangeable.
[source,rascal-shell,continue]
----
[<"a",10>, <"b",20>, <"c",30>]
[<"a", 1, "b">, <"c", 2, "d">]
----

.Benefits

.Pitfalls

