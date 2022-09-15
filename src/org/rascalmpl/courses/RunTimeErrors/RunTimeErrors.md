---
title: Runtime Errors 
details:
  - RuntimeExceptions
  - OtherRuntimeErrors

---

#### Synopsis

All exceptions and errors a Rascal program can generate during execution (run time).

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

Rascal can generate various kinds of exceptions and errors during execution (run time):

* ((Runtime Exceptions)) are discovered during execution of the Rascal program and can be caught by the Rascal program.
  When a runtime exception is not caught in the program, execution is aborted.
* ((Other Runtime Errors)) are discovered during execution of the Rascal program and cannot be caught by the Rascal program.
  They always lead to abortion of execution. These are the errors which can be prevented by fixing statically detected errors by the type checker.

#### Examples

#### Benefits

#### Pitfalls

* The structure and naming of exceptions and error messages is being revised 
so actual exceptions may differ from what is presented here.
* Since the type checker is not available yet, the interpreter throws more run-time exceptions than necessary.
