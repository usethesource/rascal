# Relation Subscription

.Synopsis
Indexing of a relation via tuple values.

.Index
[ ]

.Syntax

#  `_Exp_0_ [ _Exp~1~_, _Exp~2~_, ... _Exp~n~_]`

#  `_Exp_0_ [ _Exp~1~_]`

.Types
## Variant 1


| `_Exp_0_`                         | `_Exp~1~_` | `_Exp~2~_` | ... | `_Exp_0_ [ _Exp~1~_, _Exp~2~_, ... ]`  |
| --- | --- | --- | --- | --- |
| `rel[_T~1~_, _T~2~_, ... _T~m~_]`    | `int`     |  `int`    | ... | `rel[_T~n~_, _T~n+1~_, ... _T~m~_]`  |


## Variant 2

| `_Exp~0~_`                         | `_Exp~1~_`     | `_Exp~0~_ [ _Exp~1~_ ]`             |
| --- | --- | --- |
| `rel[_T~1~_, _T~2~_, ... _T~m~_]`    | `set[_T~1~_]`  | `rel[_T~2~_, _T~2~_, ... _T~m~_]`    |


.Function

.Details

.Description
Relation resulting from subscription of a relation _Exp_~0~.

## Variant 1

Subscription with the index values of _Exp_~1~, _Exp_~2~, .... 
The result is a relation with all tuples that have these index values as first elements 
with the index values removed from the tuple. 
If the resulting tuple has only a single element, a set is returned instead of a relation. 
A wildcard `_` as index value matches all possible values at that index position.

## Variant 2

Subscription with a set of the index values of _Exp_~1~.
The result is a relation with all tuples that have these index values as first element
with the index values removed from the tuple. 

.Examples
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

.Benefits

.Pitfalls

