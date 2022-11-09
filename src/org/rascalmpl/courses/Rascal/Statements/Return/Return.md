---
title: Return
keywords:
  - return

---

#### Synopsis

Return a value as result of a [Function].

#### Syntax

```rascal
return;

return Exp;
```

#### Types

#### Function

#### Description

A return statement comes in four variants: without and with an expression,
both variants end the execution of the current function. 
The first variant applies to functions with `void` as return type. 
The second variants applies to non-void functions and returns the value of _Exp_ as result of the function invocation.
The following rules apply:

*  The static type of _Exp_ should be compatible with the declared return type of the function in 
  which the return statement occurs.

*  In each function with a return type that is not void, every possible execution path through the body of 
  the function should end in a return statement.


In each function with a return type that is void, a return statement is implicitly assumed at the end of each execution path through the function body.

#### Examples

```rascal-shell
int twice(int n) { 
  return 2 * n; 
}
twice(5);
```

Functions that only return a value can be abbreviated (and the return is implicit):
```rascal-shell,continue
int twiceb(int n) = 2 * n;
twiceb(5);
```

Sometimes we return the result of a for loop or a visit:
```rascal-shell
list[int] even1(int n) {
  return for (i <- [0..n + 1], i % 2 == 0) {
    append i;
  };
}
even1(10)
// although that could be written easier using a comprehension
list[int] even2(int n) = [i | i <- [0..n+1], i %2 == 0];
even2(10);
```

#### Benefits

#### Pitfalls

