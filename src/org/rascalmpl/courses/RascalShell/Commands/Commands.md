---
title: Commands
---

#### Synopsis

RascalShell commands

#### Description

The RascalShell provides several built-in commands:

(((TOC)))  

Next to these commands, the shell accepts all toplevel module ((Declarations)), ((Statements)) and ((Expressions)).

#### Examples

Here we just show some random shell commands:

```rascal-shell
int a = 0; // statement
int f(int i) = 2 * i; // function declaration
syntax Exp = Exp "+" Exp; // syntax declaration
f(2) * f(2) // expression
:help // builtin command
```
