# Abstract

.Synopsis
Abstract syntax for Pico.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

.Examples
Here is the complete abstract syntax for Pico:

[source,rascal]
----
include::{LibDir}demo/lang/Pico/Abstract.rsc[tags=module]
----

                
Notes:

<1> The types that may occur in a Pico program are either natural or string.
<2> Introduce `PicoId` as an alias for Rascal's `str` datatype.
<3> Define the various data types that constitute an AST for Pico. Observe that the constructor names match the names used in the concrete syntax, e.g., `strCon`, `add`, `ifElseStat`.
<4> Define an annotation with name `location` and of type `loc` (source code location) for all AST types. This will be used when imploding
    a parse tree into an abstract syntax tree.
<5> Introduce `Occurrence` as a genereic way of describing the location of various items in the AST.

.Benefits

.Pitfalls

