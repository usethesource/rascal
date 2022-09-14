---
title: Compiler Error
---

.Synopsis
An internal error in the Rascal compiler

.Description
An internal error condition occurred while running the Rascal compiler.
This exception may be generated due to:

* An actual internal inconsistency.
* Incorrect information as computed by the type checker.
* Incorrect configuration of paths (PathConfig) when calling the compiler.
* Causes outside control of the compiler.

.Pitfalls
At the moment this error category is too wide and should be narrowed.
