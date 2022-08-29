# Set Command

.Synopsis
Set parameters that control options of RascalShell.

.Syntax
* `:set`
* `:set _Option_ _TrueOrFalse_`

.Description

The shell provides a number of options to control its behaviour.
The `set` command manages their value.

In the first form, the list of current settings is printed.

In the second form a specific option is set to true or false.

The options are:

* `profiling`: record execution times while executing subsequent Rascal code and print
  the results after each RascalShell command.
* `tracing`: while executing Rascal code, print a trace of all function calls.
* `errors`: print more diagnostic stack traces if available (of internal functionality)

.Examples
Turn `tracing` on and execute a function:
```rascal-shell
import demo::basic::Factorial;
:set tracing true
fac(5)
```

Turn trace off and execute the same function:

```rascal-shell,continue
:set tracing false
fac(5)
```

.Pitfalls
The `set` command is completely unrelated to Rascal's built-in `set` type.
