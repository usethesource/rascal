---
title: Set Command
keywords:
  - set
  - tracing
  - profiling
---

#### Synopsis

Set parameters that control options of RascalShell.

#### Syntax

* `:set`
* `:set Option TrueOrFalse`

#### Description

The shell provides a number of options to control its behaviour.
The `set` command manages their value.

In the first form, the list of current settings is printed.

In the second form a specific option is set to true or false.

The options are:

* `profiling`: record execution times while executing subsequent Rascal code and print
  the results after each RascalShell command.
* `tracing`: while executing Rascal code, print a trace of all function calls.
* `errors`: print more diagnostic stack traces if available (of internal functionality)

#### Examples

Turn `tracing` on and execute a function:
```rascal-shell
import demo::basic::Factorial;
:set tracing true
fac1(5)
```

Turn trace off and execute the same function:

```rascal-shell,continue
:set tracing false
fac1(5)
```

#### Benefits

* `profiling` provides an accurate and non-invasive profile using a stack sampling method. With high probability the operations that appear to be taking the most time are indeed a bottleneck.
* `tracing` is helpful to see which of the overloaded functions have been called and what their result was.

#### Pitfalls

* The `set` command is completely unrelated to Rascal's built-in `set` type.
* `tracing` is expensive because of the high amount of IO it generates. Better use with care on small examples.
* `profiling` gives insight into the Rascal program's behavior but not necessarily in the underlying cost of interpreting a 
Rascal program.
* `errors` is a window into the implementation of Rascal rather than the notion of Rascal programs as they run. It is used for developers of the compiler and interpreter.
