---
title: "Slice"
keywords: "[,..,],="
---

.Synopsis
Assign to a slice of a list or string.

.Syntax

*   `Assignable [ Exp~1~ .. Exp3_ ] = Exp~4~`
*   `Assignable [ Exp~1~, Exp~2~ .. Exp~3~ ] = Exp~4~`


`Exp~1~` and `Exp~3~` are optional
.Types

.Function
       
.Usage

.Description

A slice assignment is defined for ((Expressions-Values-List)), ((Values-String)) and ((Values-Node)) 
and aims to replace a slice from the old value of the assignable by a new value. 
See ((List-Slice)), ((String-Slice)) or ((Node-Slice)) for a more detailed explanation of slicing.

Let _V_ be the current value of _Assignable_.

*  `Assignable [ Exp~1~ .. Exp~3~ ] = Exp~4~`:
   The slice `[ Exp~1~ .. Exp~3~ ]` determines two indices `begin` (inclusive) and `end`   
  (exclusive) in _V_.
  A new value _V_' is computed that is a copy of _V_ but with all the elements in _V_ with `begin <= index < end` replaced by the elements of the value of _Exp_~4~.
  Note that the size of _V_ and _V_' may differ.
  _V_' is assigned to the _Assignable_. 

*  `Assignable [ Exp~1~, Exp~2~ .. Exp~3~ ] = Exp~4~`:
  The slice `[ Exp~1~, Exp~2~ .. _Exp~3~ ]` determines two indices `begin` (inclusive) and `end` (exclusive)
  and a `step` between indices in _V_.
  A new value _V_' is computed that is a copy of _V_ but with all the elements in _V_ with indices 
  `begin`, `begin+step`. ... `end-step` <= `index < end` replaced by the successive elements of the value of _Exp_~4~.
  Note that the size of _V_ and _V_' may differ.  _V_' is assigned to the _Assignable_. 
  If the number of indices in the slice and the number of elements in the value of _Exp_~4~ is not equal the following is done:
  **  If the number of elements in the slice is larger: the elements of _Exp~4~_ are used in a circular manner.
  **  If the number of elements in the slice is smaller: the remaining elements of _Exp~4~_ is inserted after the last index in the slice.

.Examples
Replace the elements with index 3, 4, 5 in `L`:
```rascal-shell,continue
L = [0,1,2,3,4,5,6,7,8,9];
L[3..6] = [100,200,300,400,500];
```
Replace the elements with index 1, 3, 5, 7 in `L` (note how the elements from `[100,200]` are used in a circular way):
```rascal-shell,continue
L = [0,1,2,3,4,5,6,7,8,9];
L[1,3..8] = [100,200];
```
Replace the elements with index 1, 3, 5, 7 in `L` (note how the unused elements from `[100,200,300,400,500]` 
are insert at index 7):
```rascal-shell,continue
L = [0,1,2,3,4,5,6,7,8,9];
L[1,3..8] = [100,200,300,400,500];
```
Similar examples for slicing assignment on strings:
```rascal-shell,continue
S = "abcdefghij";
S[3..6] = "UVWXYZ";
S = "abcdefghij";
S[1,3..8] = "XY";
S = "abcdefghij";
S[1,3..8] = "UVWXYZ";
```
Replace the elements with index 3, 4, 5 in node `N`:
```rascal-shell,continue
N = "f"(0,true,2,"abc",4,5.5,6,{7,77},8,{9,99,999});
N[3..6] = [100,200,300,400,500];
```
Replace the elements with index 1, 3, 5, 7 in `L` (note how the elements from `[100,200]` are used in a circular way):
```rascal-shell,continue
N = "f"(0,true,2,"abc",4,5.5,6,{7,77},8,{9,99,999});
N[1,3..8] = [100,200];
```
Replace the elements with index 1, 3, 5, 7 in `L` (note how the unused elements from `[100,200,300,400,500]` 
are insert at index 7):
```rascal-shell,continue
N = "f"(0,true,2,"abc",4,5.5,6,{7,77},8,{9,99,999});
N[1,3..8] = [100,200,300,400,500];
```

.Benefits

.Pitfalls

