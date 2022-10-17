---
title: For
keywords:
  - for

---

#### Synopsis

For loop.

#### Syntax

`for ( Exp~1~ , Exp~2~ , ... , Exp~n~ ) Statement;`

#### Types

#### Function

#### Description

The for-statement executes _Statement_ for all possible combinations of values generated, and filtered, by the expressions _Exp_~i~.

Some of the expressions can generate bindings (((Enumerator)), <<Values,Boolean,Match>>), and some can filter them (((Values))). 
The for loop will iterate over the cartesian product of all the generating expressions, and filter the combinations which fail the conditional expressions. 

By default, the value of a for statement is the empty list. In general, 
the value of a for statement consists of all values contributed by ((Statements-Append)) statements that are executed during the repeated execution of its body Statement.

#### Examples

```rascal-shell
import IO;
for(int n <- [1 .. 5]) println("n = <n>");
for(int n <- [1 .. 5]) append n * n;
```

#### Benefits

#### Pitfalls

