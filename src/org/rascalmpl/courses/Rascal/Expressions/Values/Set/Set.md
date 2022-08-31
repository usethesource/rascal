# Set

.Synopsis
Set values.

.Index
{ }

.Syntax
`{ Exp~1~, Exp~2~, ... }`

.Usage

.Types


|  `Exp~1~` |  `Exp~2~` |  ...  |  `{ Exp~1~, Exp~2~, ... }`    |
| --- | --- | --- | --- |
| _T~1~_    | _T~2~_    |  ...  |  set[ lub(_T~1~_, _T~2~_, ... ) ]  |


.Function

.Details

.Description
A set is an unordered sequence of values and has the following properties:

*  All elements have the same static type.
*  The order of the elements does not matter.
*  A set contains an element only once. In other words, duplicate elements are eliminated and no 
  matter how many times an element is added to a set, it will occur in it only once.


The type of a set has the form `set[T]`,
where `T` is an arbitrary type.

When a value or variable of type set occurs inside a set, that set value is inserted as set element. 
To achieve splicing of these elements, i.e., the insertion of the elements of the set value rather than the whole set,
it has to be prefixed by the splice operator `*`.

The following operators are provided on sets:
(((TOC)))

There are also [library functions]((Libraries:Prelude-Set)) available for Sets.
.Examples
##  Set types 

```rascal-shell
{1, 2, 3};
{<1,10>, <2,20>, <3,30>};
{1, "b", 3};
{<"a", 10>, <"b", 20>, <"c", 30>}
{{"a", "b"}, {"c", "d", "e"}}
```
Note that

*  `{1, 2, 3}` and `{3, 2, 1}` are identical sets (since order is not relevant).
*  `{1, 2, 3}` and `{1, 2, 3, 1}` are also identical sets (since duplication is not relevant).



##  Set splicing 

Introduce a set variable `S`
```rascal-shell,continue
S = {1, 2, 3};
```
and observe how the value of `S` is added as single element in another set:
```rascal-shell,continue
{10, S, 20};
```
or how its elements are added as elements to the other set:
```rascal-shell,continue
{10, *S, 20};
```

.Benefits

.Pitfalls

