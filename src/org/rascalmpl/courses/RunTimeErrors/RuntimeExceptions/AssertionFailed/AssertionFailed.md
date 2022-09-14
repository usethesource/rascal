---
title: Assertion Failed
---

.Synopsis
An assertion in the Rascal code is false.

.Syntax

.Types

.Function
`data RuntimeException = AssertionFailed(str label);`

       
.Usage

.Description
An [Assert]((Rascal:Assert)) statement can be used to check assumptions during the execution of a Rascal program.
This error is generated if an assertion is not true.

Remedies:

*  Modify your code to make the assertion true.
*  Modify your assertion to reflect the current behaviour of your code.
*  Catch the `AssertionFailed` yourself, see [try catch]((Rascal:TryCatch)).


.Examples
A false assertion gives an error:
```rascal-shell,error
assert 3 > 4;
```
Define a function that only increments positive integers:
```rascal-shell,continue,error
int incrPositive(int n) { assert n > 0: "n should be greater than 0"; return n + 1; }
```
Calling it with a positive integer is fine:
```rascal-shell,continue,error
incrPositive(3);
```
But a negative argument gives an error:
```rascal-shell,continue,error
incrPositive(-3);
```
We can also catch the `AssertionFailed` error. First import the Rascal exceptions (which are also included in `Prelude`)
and `IO`:
```rascal-shell,continue,error
import Exception;
import IO;
try println(incrPositive(-3)); catch AssertionFailed(msg): println("incrPositive: <msg>");
```

.Benefits

* Enables the precise expression of assumptions in your code.
* Asserts are actually executed when the compiler option 
  `enableAsserts` is set to true (by default set to `false`).
* In the RascalShell, `enableAsserts` is always true.

.Pitfalls

