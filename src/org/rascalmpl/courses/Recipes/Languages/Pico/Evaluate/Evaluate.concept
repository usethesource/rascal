# Evaluate

.Synopsis
Evaluate a Pico program.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

.Examples

A complete evaluator (interpreter) for Pico is defined below.
[source,rascal]
----
include::{LibDir}demo/lang/Pico/Eval.rsc[tags=module]
----

                
Notes:

<1> First we introduce a data type `PicoValue` that wraps all possible values that can occur at run-time.
<2> Compared to [Pico/Typecheck], we use `VENV`, a value environment (a map from Pico identifiers to Pico values).
*   The actual evaluator consists of the functions `evalExp`, `evalStat`, `evalStats`, `evalDecls` and `evalProgram`.
    They are written in a similar style as the typechecker.
*  `evalProgram` evaluates a given Pico program.


Here is how to evaluate a Pico program:
[source,rascal-shell]
----
import demo::lang::Pico::Eval;
evalProgram("begin declare x : natural, y : natural; x := 1; y := x + 5 end");
----

.Benefits

.Pitfalls

