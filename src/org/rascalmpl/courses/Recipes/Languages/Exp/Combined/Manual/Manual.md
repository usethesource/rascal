---
title: Manual
---

#### Synopsis

An Exp evaluator that uses a manually written conversion from parse tree to abstract syntax tree.

#### Syntax

#### Types

#### Function

#### Description

#### Examples

First we define a `parse` function for Exp:

```rascal
include::{LibDir}demo/lang/Exp/Combined/Manual/Parse.rsc[tags=module]
```

and test it:
```rascal-shell
import demo::lang::Exp::Combined::Manual::Parse;
parseExp("2+3");
```

Next, we define a `load` function:
```rascal
include::{LibDir}demo/lang/Exp/Combined/Manual/Load.rsc[tags=module]
```

Some comments:

<1> We reuse the previously defined concrete syntax with layout.
<2> We also reuse the previously defined abstract syntax.
<3> Import the `Parse` module defined above.
<4> The top level `load` function that converts a string to an abstract syntax tree.
<5> The conversion from parse tree to abstract syntax tree start here. Note that we
    explicitly use `demo::lang::Exp::Abstract::Syntax::Exp` in these
    rules to distinguish from `demo::lang::Exp::Concrete::WithLayout::Syntax::Exp`.


Let's try it:
```rascal-shell
import demo::lang::Exp::Combined::Manual::Load;
loadExp("2+3");
```


What remains is to write the interpreter using the above components:
```rascal
include::{LibDir}demo/lang/Exp/Combined/Manual/Eval.rsc[tags=module]
```

                
Here is how it works:
```rascal-shell
import demo::lang::Exp::Combined::Manual::Eval;
eval("2+3");
```

#### Benefits

#### Pitfalls

