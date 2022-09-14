---
title: "Node Slice"
keywords: "[,..,]"
---

#### Synopsis

Retrieve a slice of a node's argument list.

#### Syntax

*  `Exp~1~ [ Exp~2~ .. Exp~4~]`
*  `Exp~1~ [ Exp~2~ , Exp3 .. Exp~4~]`


where _Exp_~2~ and _Exp_~4~ are optional.

#### Types


| `Exp~1~`     | `Exp~2~` |  `Exp~3~`  | `Exp~4~` | `Exp~1~ [ Exp~2~ .. Exp~4~ ]`   or  `Exp~1~ [ Exp~2~ , Exp~3~ .. Exp~4~]`  |
| --- | --- | --- | --- | --- |
| `node` | `int`     | `int`       | `int`     |  `list[value]`                                                                 |


#### Function

#### Description

A Node slice is similar to a list ((List-Slice)) and uses the integer values of _Exp_~2~ and _Exp_~4~ to determine the `begin` (*inclusive*) and `end` (*exclusive*)
of a slice from the children of the node value _ND_ of _Exp_~1~. Negative indices count from the end of the list of children backwards.
Using the second form, an extra index _Exp_~3~ is given that determines the
index of the second element in the slice and establishes the `step` between
successive elements in the slice. The default `step` is 1.
If `end` is smaller than `begin`, the slice is constructed backwards.

Let `Len` be the number of children of _ND_ and let _N_~2~, _N_~3~ and _N_~4~ be the respective values of the expressions
 _Exp_~2~, _Exp_~2~ and _Exp_~2~ when they are present.

The slice parameters `begin`, `end`, and `step` are determined as follows:

*  _Exp~2~_:
**  If _Exp~2~_ is absent, then `begin = 0`.
**  Otherwise, if _N~2~_ >= 0 then `begin = N~2~` else `begin = N~2~ + Len`. 
*  _Exp~4~_:
**  If _Exp~4~_ is absent, then `end = Len`.
**  Otherwise, if _N~4~_ >= 0, then `end = N~4~` else `end = N~4~ + Len`.
*  _Exp~3~_:
**  If _Exp~3~_ is absent, then if `begin < end` then `step = 1` else `step = -1`.
**  Otherwise, if `begin < end`, then `step = N~3~ - begin` else `step = begin - N~3~`.


Now, the constraints `0 <= begin < Len` and `0 < end < Len` should hold,
otherwise the exception `IndexOutOfBounds` is thrown.

The slice consists of the children `ND[begin]`, `ND[begin+step]`, `ND[end - step]`.
When `begin >= end`, the elements are listed in reverse order.

#### Examples

Consider the list `ND = "f"(0, "abc", 20, false, 40, [3,4,5], 60, {"a", "b"}, 80);` as running example.

Here is a view on the children of _ND_ that will help to correlate positive and negative indices:


|`i`        | 0 |     1 |  2 |     3 |  4 |       5 |  6 |          7 |  8  |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
|`ND[i]`    |`0`|`"abc"`|`20`|`false`|`40`|`[3,4,5]`|`60`|`{"a", "b"}`|`80` |
|`-i`       | -9|     -8|  -7|     -6|  -5|       -4|  -3|          -2|  -1 |

    

Some common use cases (with `begin` <= `end`):


| Slice            | Means:                                                                  |
| --- | --- |
| `ND[begin..end]` | children with indices `begin` through `end-1`                           |
| `ND[begin..]`    | children with indices `begin` through the rest of the list of children  |
| `ND[..end]`      | children with indices from the beginning through `end-1`                |
| `ND[..]`         | the whole list of children                                              |
| `ND[-1]`         | last child of the list of children                                      |
| `ND[-2..]`       | the last two children of the list of children                           |
| `ND[..-2]`       | all children except the last two.                                       |



Let's put this into practice now.

```rascal-shell,error
ND = "f"(0, "abc", 20, false, 40, [3,4,5], 60, {"a", "b"}, 80);
```
Slices with begin < end
```rascal-shell,continue,error
ND[1..3];
ND[1..];       // empty end => end of list of children
ND[..3];       // empty begin => first child of list
ND[..];        // both empty => whole list of children
```
Slices with  begin >= end
```rascal-shell,continue,error
ND[3..1];      // slice contains children with indices 3 and 2 (in that order)
ND[3..3];      // empty slice when begin == end
```
Slices with negative begin or end:
```rascal-shell,continue,error
ND[2..-2];     // equivalent to ND[2..7]
ND[2..7];
ND[-4..-2];    // equivalent to ND[5..7]
ND[5..7];
```
Slices with an explicit second index:
```rascal-shell,continue,error
ND[1,3..6];
ND[5,3..];
```
Explore error cases:
```rascal-shell,continue,error
ND[..10];
ND[1..20];
```
