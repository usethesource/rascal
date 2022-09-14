---
title: Eval2
---

#### Synopsis

Like Eval1 but with support for sequences and assignments.


#### Syntax

#### Types

#### Function

#### Description

Interpreter Eval2 supports the following features of Func: 


| Feature              | Eval2 |
| --- | --- |
| function declaration | y |
| integer constant     | y |
| variable             | y |
| arithmetic operators | y |
| comparison operators | y |
| call                 | y |
| if                   | y |
| let                  | y |
| __sequence__         | y |
| __assignment__       | y |
| address operator     |
| dereference operator |




The main additions are local side effects and the sequence operator.

#### Examples

```rascal
include::{LibDir}demo/lang/Func/Eval2.rsc[tags=module]
```

                
<1> The alias `Result` is introduced: a pair of an environment and an integer value.
    All evaluator functions are changed from returning an integer (the result of evaluation) to
   `Result` (the result of evaluation _and_ the local side effects).
<2> The effect of this change can be seen in all functions. For instance, when evaluating
    multiplication, the environment produced by the left operand ahs to be passed as 
    argument to the right operand of the multiplication. This is needed, to propagate any side effects
    caused by the left operand to propagate to the right one.
<3> Assignment is implemented.
<4>  Sequencing is implemented. Observe that that the value of the left operand is ignored and that
  the value of the right operand is returned.


We apply `eval2` to example `F2`:
```rascal
include::{LibDir}demo/lang/Func/programs/F2.func[]
```

                
Let's try this.
```rascal-shell
import demo::lang::Func::Load;
import demo::lang::Func::Eval2;
import demo::lang::Func::programs::F2;
eval2("fact", [10], load(F2));
```

#### Benefits

#### Pitfalls

