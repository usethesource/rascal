---
title: Enumerator
keywords:
  - <-

---

#### Synopsis

Enumerate all values in a given subject value.

#### Syntax

`Pattern <- Exp`

#### Types

#### Function

#### Description

An enumerator generates all the values in a given subject value. 
For elementary types (`bool`, `int`, `real`, `num`, `loc`, `datetime`, `str`) this is just a singleton.
For composite types (`list`, `set`, `map`, `tuple`, `rel`, `node`) the values of their elements, 
respectively, their direct children are enumerated. An enumerator is evaluated as follows:

*  Expression _Exp_ is evaluated and may have an arbitrary value _V_.

*  The elements of _V_ are enumerated one by one.

*  Each element value is matched against the pattern _Pattern_. There are two cases:

   ** The match succeeds, any variables in _Pattern_ are bound, and the next generator in the comprehension is evaluated. 
      The variables that are introduced by an enumerator are only available to generators that appear later (i.e., to the right) 
      in the comprehension. When this enumerator is the last generator in the comprehension,
      the contributing expressions of the comprehension are evaluated.

   ** The match fails, no variables are bound. If _V_ has more elements, a next element is tried. 
      Otherwise, a previous generator (i.e., to the left) is tried. If this enumerator is the first generator in the comprehension,
      the evaluation of the comprehension is complete.

Type information is used to check the validity of an enumerator and guard you against mistakes.
An impossible enumerator like 

```rascal
int N <- {"apples", "oranges"}
```
will be flagged as an error since the pattern can never match.

#### Examples

Here are some examples of enumerators:

*  `int N <- {1, 2, 3, 4, 5}`.

*  `str K <- KEYWORDS`, where `KEYWORDS` should evaluate to a value of `set[str]`.

*  `<str K, int N> <- {<"a",10>, <"b",20>, <"c",30>}`.

*  `<str K, int N> <- FREQUENCIES`, where `FREQUENCIES` should evaluate to a value of type `rel[str,int]`.

*  `<str K, 10> <- FREQUENCIES`, will only generate pairs with `10` as second element.

*  `int N <- 17`, will only generate the value `17`.


Here are examples of enumerators in action:
```rascal-shell
[ N * N | int N <- [1, 2, 3, 4, 5] ];
{<N, K> | <str K, int N> <- {<"a",10>, <"b",20>, <"c",30>}};
```

#### Benefits

#### Pitfalls

