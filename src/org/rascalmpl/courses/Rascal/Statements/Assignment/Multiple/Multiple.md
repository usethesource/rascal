---
title: "Multiple"
keywords: "<,>,="
---

.Synopsis
Assign to multiple assignables.

.Syntax

.Types

.Function
       
.Usage

.Description
First the value _Exp_ is determined and should be a tuple of the form `< V~1~, V~2~, ..., V~n~ >`.
Next the assignments `Assignable~i~ = V~i~` are performed for 1 \<= i \<= n.

.Examples
```rascal-shell
<A, B, C> = <"abc", 2.5, [1,2,3]>;
A;
B;
C;
```

.Benefits

.Pitfalls

