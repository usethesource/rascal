---
title: Load
---

.Synopsis
Convert a Pico parse tree into a Pico abstract syntax tree.

.Syntax

.Types

.Function
       
.Usage

.Description

.Examples
The mapping between parse tree and abstract sybtax tree is achieved as follows:
```rascal
include::{LibDir}demo/lang/Pico/Load.rsc[tags=module]
```

                
Notes:

*  The function `load` takes a string as argument (supposedly the source code of a Pico program) and returns a value of type `PROGRAM`,
the abstract syntax tree of the input program. In case the input program is syntactically incorrect, a `ParseError` exception will be thrown,
see [RuntimeException]((Library:RunTimeException)).

*  `parse(#Program, txt)`: parse `txt` according to the non-terminal `Program`. Note that `#Program` is a _reified type_, i.e., the type `Program` is represented as an ordinary Rascal value and passed as argument to the `parse` function,
see [reified types]((Rascal:Values-ReifiedTypes)).
The `parse` function returns a parse tree of the input program.

*  `implode(#PROGRAM, parse(#Program, txt))`: transform the parse returned by `parse` into an abstract syntax tree of type `PROGRAM`. The [$Rascal:implode] function performs the automatic mapping between elements in the parse tree and their counterpart in the abstract syntax.


The function `load` can be used as follows:
```rascal-shell
import demo::lang::Pico::Load;
load("begin declare x : natural; x := 3 end");
```

Observe how the various parts of the abstract syntax tree are annotated with location attributes.

.Benefits

.Pitfalls

