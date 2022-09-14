---
title: "While"
keywords: "while"
---



#### Synopsis

While loop.

#### Syntax

`while ( Exp ) Statement;`

#### Types

#### Function

#### Description

The Boolean expression _Exp_ is evaluated repeatedly and _Statement_ is executed when the test is true. 
Execution ends the first time that the test yields false. 
The test _Exp_ is executed from scratch in each repetition and only the first `true` value (if any) is used.
This is relevant when _Exp_ contains a ((Boolean-Match)) or ((Boolean-NoMatch)) operator.

By default, the value of a while statement is the empty list. In general, the value of a while statement 
consists of all values contributed by ((Statements-Append)) statements that are executed during the repeated execution 
of its body _Statement_.

#### Examples

```rascal-shell
import IO;
int n = 3;
while( n > 0 ) { println("n = <n>"); n -= 1; }
```
Now build a list result using the `append` statement:
```rascal-shell,continue
n = 3;
while (n > 0) { append n * n; n -= 1; }
```

Just to be sure, a ((List-Comprehension)) is the superior way to write this:
```rascal-shell
[n * n | n <- [3 .. 1]];
```

#### Benefits

#### Pitfalls

