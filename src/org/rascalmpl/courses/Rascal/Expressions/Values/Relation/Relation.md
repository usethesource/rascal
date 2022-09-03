# Relation

.Synopsis
Relation values.

.Index
{ } rel

.Syntax
`{ < Exp~11~, Exp~12~, ... > , < Exp~21~, Exp~22~, ... > , ... }`

.Types


| `Exp~11~` |  `Exp~12~` |  ...  | `{ < Exp~11~, Exp~12~, ... > , ... }`   |
| --- | --- | --- | --- |
| `T~1~`    |    `T~2~`  |  ...  |  `rel[T~1~, T~2~, ... ]`               |


.Usage

.Function

.Details

.Description
A relation is a set of elements with the following property:

*  All elements have the same static tuple type.


Relations are thus nothing more than sets of tuples, but since they are used so often we provide a shorthand notation for them.
Relations are represented by the type `rel[T~1~ L~1~, T~2~ L~2~, ... ]`, where _T_~1~, _T_~2~, ... are arbitrary types and
_L_~1~, _L_~2~, ... are optional labels. It is a shorthand for `set[tuple[T~1~ L~1~, T~2~ L~2~, ... ]]`.

An n-ary relations with m tuples is denoted by
 `{< E~11~, E~12~, ..., E~1n~ >,< E~21~, E~22~, ..., E~2n~ >, ..., < E~m1~, E~m2~, ..., E~mn~ >}`, 
where the _E_~ij~ are expressions that yield the desired element type _T_~i~.

Since relations are a form of set all operations (see ((Values-Set))) and functions
(see ((Prelude-Set))) are also applicable to relations.

The following additional operators are provided for relations:
(((TOC)))

There are also [library functions]((Library:Relation)) available for Relations.


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

