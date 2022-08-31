# TypeDescriptor

.Synopsis
Description of a Rascal type used in type and value questions.

.Syntax

A _TypeDescriptor_ is one of

*  `bool`
*  `int`
*  `int[Max]`
*  `int[Min,Max]`
*  `real`
*  `real[Max]`
*  `real[Min,Max]`
*  `num`
*  `num[Max]`
*  `num[Min,Max]`
*  `str`
*  `loc`
*  `datetime`
*  `list[TypeDescriptor]`
*  `set[TypeDescriptor]`
*  `map[TypeDescriptor,TypeDescriptor]`
*  `tuple[TypeDescriptor~1~, TypeDescriptor~2~, ...]`
*  `void`
*  `value`
*  `arb`
*  `arb[Int]`
*  `arb[Int, TypeDescriptor~1~, TypeDescriptor~2~]`
*  `arb[TypeDescriptor~1~, TypeDescriptor~2~]`
*  `same[TypeName]`

.Types

.Function

.Details

.Description
A TypeDescriptor is used to describe Rascal types and values in questions and are used to automatically generate
values of the described type. TypeDescriptors largely follow the types as available in Rascal, with the following
extensions that are helpfull when generating values:

*  `int`, `real` and `num` may specify a minimal and maximal value for the values to be generated.
*  `arb` describes an arbitrary type. The choice can be restricted by:
   **  given an integer that defines the maximal depth of the type.
   **  given an explicit list of types to choose from.
*  `same[Name]` refers back to a type that was used earlier on in the same question.

.Examples

| TypeDescriptor      | Generated value |
| --- | --- |
| `int`               | Arbitrary integer |
| `int[-5,10]`        | An integer between -5 and 10 |
| `arb[int,bool,str]` | An arbitrary integer, boolean or string |
| `list[int]`         | A list of integers |
| `set[int[0,10]]`    | A set of integers between 0 and 10  |


.Benefits

.Pitfalls

*  There is currently an arbitrary built-in limit that restricts generated lists, sets,
   maps and relations to at most 5 elements.
*  There is no support for labeled tuples.

