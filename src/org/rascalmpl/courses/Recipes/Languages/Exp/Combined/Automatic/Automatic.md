---
title: Automatic
---

#### Synopsis

Use implode to translate an Exp parse tree to an abstract syntax tree.

#### Syntax

#### Types

#### Function

#### Description

[implode]((Library:ParseTree-implode)) is a function that automates the mapping between parse trees and abstract syntax trees.
It takes two arguments:

*  The _reified_ type of the desired abstract syntax. (In Rascal, types can not be used freely as values.
  A reified type, is a type that is wrapped in such a way that it can be passed as an argument to a function.)
*  The parse tree to be converted.


`implode` is smart in trying to find a mapping, but it needs some guidance.
A necessary step is therefore to label the rules in the grammar with the name of the 
constructor to which it has to be mapped.

#### Examples

Let's first label the syntax rules of the Exp grammar with constructor names:
```rascal
include::{LibDir}demo/lang/Exp/Combined/Automatic/Syntax.rsc[tags=module]
```
            
Observe that at image:{images}/1.png[], image:{images}/2.png[] and image:{images}/3.png[] these labels have been added.

It is good practice to introduce separate modules for parsing and for the conversion itself:

*  A `Parse` module defines a parse function and returns a parse tree. It imports only the concrete syntax.
*  A `Load` module defines a load function that first calls the above `parse` function and then applies `implode` to it.
  This is the only module that imports both concrete and abstract syntax at the same time and is therefore the only place to be
  concerned about name clashes. (If I mention `Exp`, do you know which one I mean?).


Here is the `Parse` module for Exp ...
```rascal
include::{LibDir}demo/lang/Exp/Combined/Automatic/Parse.rsc[tags=module]
```

and this is how it works:
```rascal-shell
import demo::lang::Exp::Combined::Automatic::Parse;
parseExp("2+3*4");
```

We can use `parse` to define `load`:
```rascal
include::{LibDir}demo/lang/Exp/Combined/Automatic/Load.rsc[tags=module]
```

Notes:

<1> We also need the `parse` function, as defined above.
<2> We also need the abstract syntax as already defined earlier in [Exp/Abstract].
<3> We need [Rascal:ParseTree] since it provides the [Rascal:implode] function.


Let's try it:
```rascal-shell
import demo::lang::Exp::Combined::Automatic::Load;
load("2+3*4");
```

Remains the definition of the `eval` function:
```rascal
include::{LibDir}demo/lang/Exp/Combined/Automatic/Eval.rsc[tags=module]
```

                
Here is the end result:
```rascal-shell
import demo::lang::Exp::Combined::Automatic::Eval;
eval("2+3*4");
```

#### Benefits

#### Pitfalls

