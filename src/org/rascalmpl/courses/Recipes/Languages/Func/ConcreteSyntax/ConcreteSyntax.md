# Concrete Syntax

.Synopsis
The concrete syntax of Func.

.Syntax

.Types

.Function

.Details

.Description

.Examples
```rascal
include::{LibDir}demo/lang/Func/Func.rsc[tags=module]
```

                
The concrete syntax of Func uses many features of Rascal's syntax definitions. Some notes:

*   The definition of lexical syntax follows the pattern:
**  Define lexical symbols (`Ident`, `Natural`).
**  Define rules for layout.
**  Use follow restrictions (`!>>`) to enforce the longest match of lexical symbols.
*  The definition of lexical also follows a common pattern:
**  List of non-terminal is defined with their alternatives.
**  One non-terminal is designated as start symbol (`Prog`).
**  Each alternative has a label, this is for the benefit of converting parse trees to abstract syntaxt trees.
**  Each alternative spells out its priority and associativity.

.Benefits

.Pitfalls

