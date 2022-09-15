---
title: Do
keywords:
  - do
  - while

---

#### Synopsis

Repeat statements while condition holds.

#### Syntax

`do Statement while ( Exp );`

#### Types

#### Function

#### Description

_Statement_ is executed repeatedly, as long as the Boolean expression _Exp_ yields true. 
Expression _Exp_ is executed from scratch in each repetition and only the first true value (if any) is used.

By default, the value of a do statement is the empty list. 
In general, the value of a do statement consists of all values contributed by ((Statements-Append)) statements 
that are executed during the repeated execution of its body Statement.

#### Examples

```rascal-shell
import IO;
int n = 3;
do { println("n = <n>"); n -= 1; } while (n > 0);
```
Now build a list result using the `append` statement:
```rascal-shell,continue
n = 3;
do { append n * n; n -= 1; } while (n > 0);
```

#### Benefits

#### Pitfalls

