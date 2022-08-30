# Relation

.Synopsis
Relation values.

.Index
{ } rel

.Syntax
`{ < _Exp~11~_, _Exp~12~_, ... > , < _Exp~21~_, _Exp_~22~, ... > , ... }`

.Types


| `_Exp~11~_` |  `_Exp~12~_` |  ...  | `{ < _Exp~11~_, _Exp~12~_, ... > , ... }`   |
| --- | --- | --- | --- |
| `_T~1~_`    |    `_T~2~_`  |  ...  |  `rel[_T~1~_, _T~2~_, ... ]`               |


.Usage

.Function

.Details

.Description
A relation is a set of elements with the following property:

*  All elements have the same static tuple type.


Relations are thus nothing more than sets of tuples, but since they are used so often we provide a shorthand notation for them.
Relations are represented by the type `rel[_T_~1~ _L_~1~, _T_~2~ _L_~2~, ... ]`, where _T_~1~, _T_~2~, ... are arbitrary types and
_L_~1~, _L_~2~, ... are optional labels. It is a shorthand for `set[tuple[_T_~1~ _L_~1~, _T_~2~ _L_~2~, ... ]]`.

An n-ary relations with m tuples is denoted by
 `{< _E_~11~, _E_~12~, ..., _E_~1n~ >,< _E_~21~, _E_~22~, ..., _E_~2n~ >, ..., < _E_~m1~, _E_~m2~, ..., _E_~mn~ >}`, 
where the _E_~ij~ are expressions that yield the desired element type _T_~i~.

Since relations are a form of set all operations (see ((Values-Set))) and functions
(see ((Prelude-Set))) are also applicable to relations.

The following additional operators are provided for relations:
(((TOC)))

There are also [library functions]((Libraries:Prelude-Relation)) available for Relations.


.Examples
```rascal-shell
{<1,10>, <2,20>, <3,30>}
```
instead of `rel[int,int]` we can also give `set[tuple[int,int]]` as type of the above expression
remember that these types are interchangeable.
```rascal-shell,continue
{<"a",10>, <"b",20>, <"c",30>}
{<"a", 1, "b">, <"c", 2, "d">}
```

.Benefits

.Pitfalls

