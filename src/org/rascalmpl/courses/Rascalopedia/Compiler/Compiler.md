# Compiler

.Synopsis
Tranform source code to an executable form.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

A [compiler](http://en.wikipedia.org/wiki/Compiler) transforms the source code of a program (in a source langue) to an executable form
(in a target language)
and consists of the following phases:

*  ((Parser)): read the source code and build an ((Abstract Syntax Tree)).
*  ((Typechecker)): perform a semantic analysis of the code, resolve all names
  and verify that the program is type correct.
*  Optimisation: perform optimisations (e.g., constant folding, dead code elimination, call unfolding).
  This can be seen as a form of ((Refactoring)).
*  Code generation: generate the final code, this can be asembly language or directly executable code.

.Examples

.Benefits

.Pitfalls

