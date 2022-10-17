---
title: NoKeywordParameters
---

#### Synopsis

A function that is declared without keyword parameters is called with keyword parameters.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

Functions maybe declared with or without keyword parameters.
This error is generated when a function has been declared without keyword parameters but is called with a keyword parameter.

Remedies:

*  Replace the keyword parameter in the call by a positional parameter.
*  Add a keyword parameter to the function declaration.

#### Examples

```rascal-shell,error
int incr(int x) = x + 1;
incr(3, delta=5);
```
Here is one solution:
```rascal-shell
int incr(int x, int delta=1) = x + delta;
incr(3, delta=5);
```

#### Benefits

#### Pitfalls

