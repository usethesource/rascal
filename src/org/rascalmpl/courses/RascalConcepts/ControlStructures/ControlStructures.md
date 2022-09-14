---
title: Control Structures
---

.Synopsis
Success-directed control structures.

.Syntax

.Types

.Function

.Description

The flow of Rascal program execution is completely explicit. Boolean expressions determine choices that drive the control structures. 
Only local backtracking is provided in the context of boolean expressions and pattern matching.

Control structures like [If]((Rascal:Statements-If)), [While]((Rascal:Statements-While)) 
and [For]((Rascal:Statements-For)) statement are driven by Boolean expressions.
Actually, combinations of generators and Boolean expressions can be used to drive the control structures. 
In the latter case, the Boolean expression is executed for each generated value.

.Examples
A classical if statement:
```rascal
if(N <= 0)
     return 1; 
  else
     return N * fac(N - 1);
```

A combination of a generator and a test:

```rascal
for(/asgStat(Id name, _) <- P, size(name) > 10){
    println(name);
}
```
This statement prints all identifiers in assignment statements (`asgStat`) that consist of more than 10 characters.

.Benefits

.Pitfalls

