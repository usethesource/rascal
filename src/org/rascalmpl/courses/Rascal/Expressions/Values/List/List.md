# List

.Synopsis
List values.

.Index
[ ]

.Syntax
`[ _Exp_~1~, _Exp_~2~, ... ]`

.Types

//

[cols="20,20,20,40"]
|====
|  `_Exp~1~_` |  `_Exp~2~_` |  ...  |  `[ _Exp~1~_, _Exp~2~_, ... ]`   

| `_T~1~_`    | `_T~2~_`    |  ...  |  `list[lub(_T~1~_, _T~2~_, ... )]` 
|====

.Usage

.Function

.Details

.Description
A list is an ordered sequence of values and has the following properties:

*  All elements have the same static type.
*  The order of the elements matters.
*  A list may contain an element more than once.


The type of a list has the form `list[_T_]`,
where `_T_` is an arbitrary type.

When a value or variable of type list occurs inside a list, that list value is inserted as list element.
To achieve _splicing_ of these elements, i.e., the insertion of the elements of the list value rather than the whole list,
it has to be prefixed by the splice operator `*`.

The following operators are provided on list:
(((TOC)))

There are also [library functions]((Libraries:Prelude-List)) available for List.

.Examples
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

.Benefits

.Pitfalls

