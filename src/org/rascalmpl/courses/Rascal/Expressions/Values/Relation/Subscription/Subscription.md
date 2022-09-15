---
title: Relation Subscription
keywords:
  - "["
  - "]"
  - "##"
  - Syntax
  - "*"
  - `Exp0_
  - "["
  - Exp~1~
  - 
  - Exp~2~
  - 
  - "..."
  - Exp~n~]`
  - "*"
  - `Exp0_
  - "["
  - Exp~1~]`

---

## Synopsis
Indexing of a relation via tuple values.

#### Types

### Single Value Variant 1

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| `_Exp~0~`                         | `Exp~1~` | `Exp~2~` | ... | `Exp0_ [ Exp~1~, Exp~2~, ... ]`  |
| `rel[T~1~, T~2~, ... T~m~]`    | `int`     |  `int`    | ... | `rel[T~n~, _T~n+1~_, ... T~m~]`  |


### Set of Values Variant 

| `Exp~0~`                         | `Exp~1~`     | `Exp~0~ [ Exp~1~ ]`             |
| --- | --- | --- |
| `rel[T~1~, T~2~, ... T~m~]`    | `set[T~1~]`  | `rel[T~2~, T~2~, ... T~m~]`    |



## Description

Relation resulting from subscription of a relation _Exp_~0~.

### Single Value Variant

Subscription with the index values of _Exp_~1~, _Exp_~2~, .... 
The result is a relation with all tuples that have these index values as first elements 
with the index values removed from the tuple. 
If the resulting tuple has only a single element, a set is returned instead of a relation. 
A wildcard `_` as index value matches all possible values at that index position.

### Set of Values Variant 

Subscription with a set of the index values of _Exp_~1~.
The result is a relation with all tuples that have these index values as first element
with the index values removed from the tuple. 

#### Examples

```rascal-shell
R = {<1,10>, <2,20>, <1,11>, <3,30>, <2,21>};
R[1];
R[{1}];
R[{1, 2}];
RR = {<1,10,100>,<1,11,101>,<2,20,200>,<2,22,202>,
              <3,30,300>};
RR[1];
RR[1,_];
```
Introduce a relation with economic data and assign it to `GDP`:
```rascal-shell,continue
rel[str country, int year, int amount] GDP =
{<"US", 2008, 14264600>, <"EU", 2008, 18394115>,
 <"Japan", 2008, 4923761>, <"US", 2007, 13811200>, 
 <"EU", 2007, 13811200>, <"Japan", 2007, 4376705>};
```
and then retrieve the information for the index `"Japan"`:
```rascal-shell,continue
GDP["Japan"];
```
or rather for the indices `"Japan"` and `2008`:
```rascal-shell,continue
GDP["Japan", 2008];
```


