---
title: Typecheck
---

.Synopsis
Typechecker for Pico programs.

.Syntax

.Types

.Function
       
.Usage

.Description

.Examples

Recall the following properties of Pico that are relevant for type checking:

*  There are two types: natural numbers and strings.

*  Variables have to be declared.

*  Expressions may contain naturals, strings, variables, addition (`+`), subtraction (`-`) and concatenation (`||`).

*  The operators `+` and `-` have operands of type natural and their result is natural.

*  The operator `||` has operands of type string and its results is also of type string.

*  Tests in if-then-else statement and while-statement should be of type natural.


The type checker is going to check these rules and will produce an error message when they are violated.

```rascal
include::{LibDir}demo/lang/Pico/Typecheck.rsc[tags=module]
```

                
Notes:

<1>  We will use `TENV` (short for type environment, as an alias for a tuple that contains all relevant type information:
     **  `symbols`: a map from Pico identifiers to their declared type.
     **  `errors`: a list of error messages. An error message is represented by its location (where the error occurred) and a textual message.
<2>  `addError` is an auxiliary function to add in a given type environment an error message to the list of errors. It returns a new type environment.
<3>  `required`is an auxiliarty function to produce readable messages, e.g., `"Required natural, got string"`.
<4>  The actual type checking is done by the functions `checkExp`, `checkStat`, `checkStats`, `checkDecls` and `checkProgram`. They all have three arguments:
     **  the program fragment (an abstract syntax tree) to be checked.
     **  the required type of that fragment.
     **  the type environment.
     
     
     `checkExp` checks expressions. For instance, checking a natural constant (`natCon`) is ok when type `natural` is expected but will give an error message when a `string` is expected. Observe how all the arguments of the check functions have a labeled pattern as first argument, here `exp:natCon(int N)`. The benefit is that the whole argument is available inside the function (as value of variable `exp`) and this can be used to retrieve the location information from it (`exp@location`) when an error has to be created.

<5>  An important case is to check whether an identifier has been defined and, if so, whether it is defined with the expected type.

<6>  Check `add`.

<7>  Check `sub`.

<8> Check `conc`.

<9>  An assignment statement is checked: the identifier on the left-hand side should have been declared and should be type compatible with the expression on the right-hand side.

<10>  Checking if- and while-statements amounts to checking the embedded statements and ensuring that the type of the test is natural.

<11>  Checking a list of statements amounts to checking each statement in the list.

<12>  Checking declarations amounts to extracting each (id, type) pair form the declarations and using a map comprehension to build a type environment.

<13>  Checking a complete Pico program is achieved by first checking the declarations of the program and using the resulting type environment to check its body.

<14>  `checkProgram` defines how to check the source code of a given Pico program.


Checking an erroneous program goes like this:
```rascal-shell
import demo::lang::Pico::Typecheck;
checkProgram("begin declare  x : natural; x := \"abc\" end");
```

The error location will be use later to give specific messages in the IDE.

.Benefits

.Pitfalls

