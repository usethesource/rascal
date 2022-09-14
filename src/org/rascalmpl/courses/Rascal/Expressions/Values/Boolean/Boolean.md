---
title: "Boolean"
keywords: "true,false"
---

.Synopsis
Boolean values.

.Syntax
`true`, `false`

.Usage

.Types
`bool`

.Function

.Description
The Booleans are represented by the type `bool` which has two values: `true` and `false`.

The Boolean operators (to be more precise: operators with a value of type Boolean as result) have _short-circuit_ semantics. 
This means that the operands are evaluated until the outcome of the operator is known.

Most operators are self-explanatory except the match (:=) and no match (!:=) operators that are also the main reason to treat Boolean operator expressions separately. Although we describe patterns in full detail in ((Patterns)), a preview is useful here. A pattern can

*  match (or not match) any arbitrary value (that we will call the _subject value_);

*  during the match variables may be bound to subvalues of the subject value.


The _match_ operator
```rascal
_Pat_ := _Exp_
```
is evaluated as follows:

*  _Exp_ is evaluated, the result is a subject value;

*  the subject value is matched against the pattern _Pat_;

*  if the match succeeds, any variables in the pattern are bound to subvalues of the subject value and the match expression yields `true`;

*  if the match fails, no variables are bound and the match expression yields `false`.


This looks and _is_ nice and dandy, so why all this fuss about Boolean operators?
The catch is that--as we will see in ((Patterns))--a match need not be unique. This means that there may be more than one way of matching the subject value resulting in different variable bindings. 

This behaviour is applicable in the context of all Rascal constructs where a pattern match determines the flow of control of the program, in particular:

*  Boolean expressions: when a pattern match fails that is part of a Boolean expression, further solutions are tried in order to try to make the Boolean expression true.

*  Tests in ((For)), ((While)), ((Do)) statements.

*  Tests in ((Boolean-Any)) and ((Boolean-All)) expressions.

*  Tests and ((Enumerator))s in comprehensions.

*  Pattern matches in cases of a ((Expressions-Visit)).

*  Pattern matches in cases of a ((Switch)).


The following operators are provided for Boolean:
(((TOC)))

There are also [library functions]((Library:Boolean)) available for Booleans.

.Examples
Consider the following match of a list
```rascal-shell
[1, *int L, 2, *int M] := [1,2,3,2,4]
```
By definition `list[int] L` and `list[int] M` match list elements that are part of the enclosing list in which they occur. If they should match a nested list each should be enclosed in list brackets.

There are two solutions for the above match:

*  `L` = `[]` and `M` =` [2, 3, 2, 4]`; and

*  `L` = `[2,3]` and `M` =` [4]`.

```rascal-shell
import IO;
for ([1, *int L, 2, *int M] := [1,2,3,2,4])
  println("L: <L>, M: <M>");
```

Depending on the context, only the first solution of a match expression is used, respectively all solutions are used.
If a match expression occurs in a larger Boolean expression, a subsequent subexpression may yield false and -- depending on the actual operator -- evaluation backtracks to a previously evaluated match operator to try a next solution. Let's illustrate this by extending the above example:

```rascal
[1, *int L, 2, *int M] := [1,2,3,2,4] && size(L) > 0
```
where we are looking for a solution in which L has a non-empty list as value. Evaluation proceeds as follows:

*  The left argument of the `&&` operator is evaluated: the match expression is evaluated resulting in the bindings `L = []` and `M = [2, 3, 2, 4]`;

*  The right argument of the `&&` operator is evaluated: `size(L) > 0` yields `false`;

*  Backtrack to the left argument of the `&&` operator to check for more solutions: indeed there are more solutions resulting in the bindings `L = [2,3]` and `M = [4]`;

*  Proceed to the right operator of `&&`: this time `size(L) > 0` yields `true`;

*  The result of evaluating the complete expression is `true`.

```rascal-shell
import IO;
import List;
```
for prints them all:
```rascal-shell,continue
for ([1, *int L, 2, *int M] := [1,2,3,2,4] && size(L) > 0)
  println("L: <L>, M: <M>");
```
if prints the first
```rascal-shell,continue
if ([1, *int L, 2, *int M] := [1,2,3,2,4] && size(L) > 0)
  println("L: <L>, M: <M>");
```

.Benefits

.Pitfalls

