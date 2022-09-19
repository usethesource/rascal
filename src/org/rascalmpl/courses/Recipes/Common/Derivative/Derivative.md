---
title: Derivative
---

#### Synopsis

Symbolic differentiation.

#### Syntax

#### Types

#### Function

#### Description

Computing the http://en.wikipedia.org/wiki/Differentiation_(mathematics)[derivative] of an expression with respect to some variable is a classical calculus problem. Loosely speaking, a derivative can be thought of as how much one quantity is changing in response to changes in some other quantity; for example, the derivative of the position of a moving object with respect to time is the object's instantaneous velocity.

We present here rules for determining the derivative `dE/dX` of simple expressions `E` for a given variable `X`. Recall that for number `N`, variables `X` and `Y`, and expressions `E1` and `E2` the following rules apply:

*  `dN / dX = 0`.
*  `dX / dX = 1`.
*  `dX / dY = 0`, when `X != Y`.
*  `d(E1 + E2) /dX = dE1 / dX + d E2 /dX`.
*  `d(E1 * E2) / dX =  (d E1 / dX  * E2) + (E1 * d E2 /dX)`.


#### Examples

Here is our solution followed by a list of explanations:

```rascal-include
demo::common::Derivative
```

<1> Define a data type `Exp` to represent expressions.
<2> Introduce an example expression `E` for later use.
<3> Define the actual differentiation function `dd`. Observe that this definition depends on the use of patterns in function declarations, see [Rascal:Function].
<4> Define simplification rules. 
<5> A default rule is give for the case that no simplification applies.
<6> Define the actual simplication function `simplify` that performs a bottom up traversal of the expression, application simplification
rules on the up.

                
Let's differentiate the example expression `E`:
```rascal-shell
import demo::common::Derivative;
dd(E, var("x"));
```
As you can see, we managed to compute a derivative, but the result is far more complex than we would like.
This is where simplification comes in. First try a simple case:
```rascal-shell,continue
simplify(mul(var("x"), add(con(3), con(5))));
```
Now apply simplification to the result of differentiation:
```rascal-shell,continue
simplify(dd(E, var("x")));
```

#### Benefits

#### Pitfalls

