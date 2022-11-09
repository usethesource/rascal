---
title: If
keywords:
  - if
  - else

---

#### Synopsis

Conditional statement.

#### Syntax

```rascal
if (Exp)
  Statement

if (Exp) {
  Statements
}

if (Exp) 
  Statement~1~ 
else 
  Statement~2~

if (Exp) {
  Statements~1~
}
else {
  Statements~2~
}
```

#### Types

| `Exp` | `if ( Exp ) Statement;`  |
| --- | --- |
| `bool`  |  `void`                      |



| `Exp` | `Statement~1~` | `Statement~2~` | `if ( Exp ) Statement~1~ else Statement~2~;`  |
| --- | --- | --- | --- |
| `bool`  |  T~1~        | T~2~         | `lub(T~1~, T~2~)`                               |


#### Function

#### Description

The test _Exp_ is evaluated and its outcome determines the statement to be executed: 
_Statement_~1~ if _Exp_ yields `true` and _Statement_~2~ otherwise. 
The value of an if-then statement is equal to _Statement_ when its test is true. Otherwise it is void.
The value of an if-then-else statement is the value of the statement that was executed.

#### Examples

```rascal-shell
if (3 > 2) {
  30; 
} else {
  40;
}
x = if (3 > 2) {
  30; 
} else {
  40;
}
if (3 > 2) 
  30;
```
An if-then statement yields `void`  when its test is false
(demonstrated by the __ok__ that is printed by the Rascal system):
```rascal-shell,continue
if( 2 > 3 ) 
  30;
```

Here we use ((Fail)) to backtrack over the possible matches of the `if`:
```rascal-shell
import IO;
Label: if ([*_, 1, *_] := [1,2,1]) {
  println("yep"); 
  fail Label;
}
```

#### Benefits

* backtracking is a powerful and easy way to search for solutions
* nested if-then-else's are often not necessary with the `,` notation

#### Pitfalls

* backtracking does not _undo_ side-effects like `println`
