---
title: Eval0
---

#### Synopsis

A Func interpreter that does not support let-expressions and pointers.

#### Syntax

#### Types

#### Function

#### Description

Interpreter Eval0 supports the following features of Func:


| Feature              | Eval0 |
| --- | --- |
| function declaration | y |
| integer constant     | y |
| variable             | y |
| arithmetic operators | y |
| comparison operators | y |
| call                 | y |
| if                   | y |
| let                  |
| sequence             |
| assignment           |
| address operator     |
| dereference operator |


#### Examples

Here is the code for Eval0:
```rascal
include::{LibDir}demo/lang/Func/Eval0.rsc[tags=module]
```

Some points to note:

<1> `PEnv` is used as an alias for a map from names to functions. Such maps are used to represent the function definitions in the program.
<2> Here the top level interpreter `eval0` is defined. It takes the name of the main function, a list of actual parameters, and the complete Func program. Binding of variables is done by substitution.
<3> The substitution function is defined. It takes an expression, a list of variables, and a list of integer values to be substituted for them. Note how a [Rascal:Visit] is used to find all the variables in the expression and to replace them.
<4> The versions of `eval0` for each implemented construct. They all have a `PEnv` argument that is needed
    to resolve calls.
<5> The if expression is defined: the then-branch is taken when the test evaluates to a non-zero integer.
<6> The call expression is interpreted. It contains the following steps:
    *  The actual parameters are evaluated.
    *  A substitution is made in the body of the called function, replacing formal parameters by actual values.
    *  The result of this substitution is evaluated.


Let's try this on example `F0`:
```rascal
include::{LibDir}demo/lang/Func/programs/F0.func[]
```

                
```rascal-shell
import demo::lang::Func::Load;
import demo::lang::Func::Eval0;
import demo::lang::Func::programs::F0;
eval0("fact", [10], load(F0));
```

#### Benefits

#### Pitfalls

