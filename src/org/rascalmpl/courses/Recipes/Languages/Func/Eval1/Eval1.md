---
title: Eval1
---

.Synopsis
Like Eval0 but with support for let-expressions.


.Syntax

.Types

.Function

.Description
Interpreter Eval1 supports the following features of Func: 


| Feature              | Eval1 |
| --- | --- |
| function declaration | y |
| integer constant     | y |
| variable             | y |
| arithmetic operators | y |
| comparison operators | y |
| call                 | y |
| if                   | y |
| __let__              | y |
| sequence             |
| assignment           |
| address operator     |
| dereference operator |




In particular, the let construct is supported and this requires the addition
of an extra environment for <name, value> bindings.

.Examples
```rascal
include::{LibDir}demo/lang/Func/Eval1.rsc[tags=module]
```

                
<1> The alias `Env` is introduced that maps strings to integers.
    All evaluation functions get an extra Env argument.
<2> The environment is used to retrieve a variable's value.
<3> The environment is extended with new bindings.


Let's try this with F1:
```rascal
include::{LibDir}demo/lang/Func/programs/F1.func[]
```

The result:
```rascal-shell
import demo::lang::Func::Load;
import demo::lang::Func::Eval1;
import demo::lang::Func::programs::F1;
eval1("fact", [10], load(F1));
```

.Benefits

.Pitfalls

