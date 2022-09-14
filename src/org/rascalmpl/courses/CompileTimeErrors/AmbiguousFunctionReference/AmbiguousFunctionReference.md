---
title: AmbiguousFunctionReference
---

.Synopsis
An ambiguous function name

.Syntax

.Types

.Function
       
.Usage

.Description
Warning: How to generate this error? 

.Examples
```rascal-shell
data D = d(int x);
data D2 = d(str x);
d(3).x
d("a").x
```

.Benefits

.Pitfalls

