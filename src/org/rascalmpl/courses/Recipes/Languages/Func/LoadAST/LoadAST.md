---
title: Load AST
---

.Synopsis
Parse Func program from string or file and convert to an abstract syntax tree.

.Syntax

.Types

.Function

.Description
To simplify later processing, Func programs are converted to an abstract syntax tree.

.Examples
The concrete syntax for Func is described in ((Func-ConcreteSyntax)) and its
abstract syntax in ((Func-AbstractSyntax)).
Rather than manually writing conversion rules from Func parse trees to Func abstract syntax trees
we use our secret weapon: [implode]((Library:ParseTree-implode)) that performs the mapping for us.
As you see when you compare the concrete and abstract syntax, the ground work has already been done
by appropriately labelling concrete rules with constructor names of the abstract syntax.

Here is the code for the `load` funcion:

```rascal
include::{LibDir}demo/lang/Func/Load.rsc[tags=module]
```

                
This looks simple but also slightly intimidating due to the many qualified names.
The issue is that the names in the concrete and abstract syntax are (on purpose) overloaded.
A name like `Prog` can be the one from the concrete syntax(i.e., `demo::lang::Func::Func::Prog`)
or the one from the abstract syntax (i.e., `demo::lang::Func::AST::Prog`).

For instance, the local version of `implode` defined here get a concrete `Prog` as argument and returns an abstract one.
Both `load` function return an abstract `Prog`.

Let's try this on example `F0`:
```rascal
include::{LibDir}demo/lang/Func/programs/F0.func[]
```

                
```rascal-shell
import demo::lang::Func::Load;
import demo::lang::Func::programs::F0;
load(F0);
```
We get the original program and its __abstract syntax tree__ of type `Prog` back.
In case of doubt, compare this with the result in ((Func-Parse)) where we did obtain a parse tree.
Next, we try the same from a file:
```rascal-shell,continue
load(|std:///demo/lang/Func/programs/F0.func|);
```

.Benefits

.Pitfalls

