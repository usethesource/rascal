---
title: Read Eval Print Loop (REPL)
---

#### Synopsis

The Rascal Read Eval Print Loop (REPL)

#### Description

The Rascal shell is an interactive console to experiment with Rascal code. You can write
your own expressions, statements and declarations right there. Or, you can import library
modules and try out their functionality.

#### Examples

Rascal code can be a trivial expression
```rascal-shell
1+2
```
Or a more complex list comprehension:
```rascal-shell
[ n * n | int n <- [0..10] ]
```
Or importing a module and using a function declared in it:
```rascal-shell
import List;
size([ n * n | int n <- [0..10] ])
```
Another use is to declare variables
```rascal-shell
int x = 2;
int y = 3;
```
and use them later on:
```rascal-shell,continue
x * y
```

#### Pitfalls

* Rascal is quite demanding as far as the proper placement of semicolons (`;`) is concerned.
* Rascal is a statically typed language but we have not released the type checker yet. Sometimes this
is confusing because the interpreter will try to run otherwise broken code and produce an error message.
