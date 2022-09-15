---
title: Reducer
keywords:
  - (
  - |
  - )
  - it

---

#### Synopsis

Reduce generated values to a single value.

#### Syntax

`( InitExp | RedExp | Gen~1~, Gen~2~, ... )`

#### Types

#### Function

#### Description

A reducer resembles the http://en.wikipedia.org/wiki/Fold_(higher-order_function)[fold] function
found in most functional languages.

A reducer is equivalent to the following code:
```rascal,subs="verbatim,quotes"
it = _InitExp_; // <1>
for(_Gen~1~_, _Gen~2~_, ... ) // <2>
    it = _RedExp_; // <3>
it; // <4>
```
and is executed as follows:

<1> A fresh variable `it` is initialized with _InitExp_. 
    We call the variable `it` since we use `it` to initialize the reducer, to make changes to `it`,
    and to return `it` as result.
<2> A for loop iterates over all values produced by the generators `Gen~1~`, `Gen~2~`, ... .
<3> In the body of the loop, variable `it` is updated to reflect a new reduced value.
    Note that `it` itself and variables introduced in _Gen_~1~, _Gen_~2~, ... may occur in _RedExp_.
<4> The value of `it` is the result of the reducer.

#### Examples

```rascal-shell
L = [1, 3, 5, 7];
(0 | it + e | int e <- L);
(1 | it * e | int e <- L);
```

#### Benefits

#### Pitfalls

