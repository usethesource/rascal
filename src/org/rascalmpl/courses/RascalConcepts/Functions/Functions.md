---
title: Functions
---

#### Synopsis

Functions and pattern-directed invocation.

#### Syntax

#### Types

#### Function

#### Description

Functions allow the definition of frequently used operations. They have a name and formal parameters. They are explicitly declared and are fully typed. 
Functions can also be used as values thus enabling higher-order functions. 
Rascal is a higher-order language in which functions are first-class values.

See [Function Declaration]((Rascal:Declarations-Function)) for details.

#### Examples

Here is an example of a function that counts the number of assignment statements in a program:
```rascal
int countAssignments(PROGRAM P){
    int n = 0;
    visit (P){
    case asgStat(_, _):
         n += 1;
    }
    return n;
}
```

Consider the following use of higher-order functions:
```rascal
int double(int x) { return 2 * x; }

int triple(int x) { return 3 * x; }

int f(int x, int (int) multi){ return multi(x); }
```

The functions `double` and `triple` multiply their argument with a constant. 
Function `f` is, however, more interesting. 
It takes an integer `x` and a function `multi` (with integer argument and integer result) as argument and 
applies multi to its own argument. `f(5, triple)` will hence return `15`. 
Function values can also be created anonymously as illustrated by the following, alternative, 
manner of writing this same call to `f`:
```rascal
f(5, int (int y){return 3 * y;});
```

Here the second argument of `f` is an anonymous function.

#### Benefits

#### Pitfalls

