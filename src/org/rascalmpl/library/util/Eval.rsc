@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module util::Eval

extend Exception;
extend util::Reflective;

@synopsis{Results encode the output of a call to `eval`}
@description{
* `ok` reflects the execution was succesful while there was no output. For example a call to `println` would produce `ok()`.
* `result` captures a value of a certain type, which is parameterized as `&T`.
}
data Result[&T] = ok() | result(&T val);

@synsopsis{Normally static errors are not run-time exceptions, but `eval` wraps them due to its dynamic nature.}
@description{
`eval` will throw the first static error that is blocking the execution of a command.
}
data Exception 
  = StaticError(str message, loc location)
  ; 

@synopsis{A reusable instance of the Rascal runtime system configured by a specific PathConfig.}
@description{
* `pcfg` documents the configuration parameters for this specific Rascal run-time
* `reset()` when called clears the heap of imported modules and all the local declarations in the top-level module instance.
* `eval(typ, command, duration=1000)` evaluates the command, expecting a result of type `typ`. 
   * The optional `duration` keyword parameterized will limit execution time to the given amount of milliseconds.
   * The `typ` parameter must be a supertype of any output expected from the `eval` command. If this is not the case, then a run-time exception will be thrown. It is always safe to put `#value` there.

For `eval` note that a command can be one of:
*  Statement
*  Declaration
*  Import
*  Extend
*  SyntaxDefinition 

For evaluating an Expression simply add a semicolon to the end: `1 + 1;`
}
@examples{
```rascal-shell
import util::Eval;
e = createRascalRuntime()
e.eval(#int, "2 * 3;");
```

A run-time can keep state between calls of `eval`:
```rascal-shell
import util::Eval;
e = createRascalRuntime()
e.eval(#void, "import IO");
e.eval(#int, "int a = 1;");
e.eval(#void, "println(a)");
```

}
data RascalRuntime
  = evaluator(
      PathConfig pcfg,
      void () reset,
      Result[&T] (type[&T] typ, str command /* int duration=-1 */) eval
  );

@synopsis{Workhorse to instantiate a working ((RascalRuntime))/}
@javaClass{org.rascalmpl.library.util.Eval}
java RascalRuntime createRascalRuntime(PathConfig pcfg=pathConfig());

@synopsis{Evaluate a single command and return its return value.}
@description{
This creates a ((RascalRuntime)), uses it to evaluate one command, and then discards the runtime again.

For efficiency's sake it may be better to use ((createRascalRuntime)), and call it's `eval` closure
as often as you need it. 
}
public Result[&T] eval(type[&T] typ, str command, int duration=-1, PathConfig pcfg=pathConfig()) 
  throws Timeout, StaticError, ParseError
  = eval(typ, [command], pcfg=pcfg, duration=duration);

@synopsis{Evaluate a list of command and return the value of the last command.}
@description{
This creates a ((RascalRuntime)), uses it to evaluate some commands, and then discards the runtime again.

For efficiency's sake it may be better to use ((createRascalRuntime)), and call it's `eval` closure
as often as you need it. 
}
Result[&T] eval(type[&T] typ, list[str] commands, int duration=-1, PathConfig pcfg=pathConfig()) 
  throws Timeout, StaticError, ParseError {
    e = createRascalRuntime(pcfg=pcfg);

    for (command <- commands[..-1]) {
      e.eval(#value, command, duration=duration);
    }

    return e.eval(typ, commands[-1], duration=duration);
}

@synopsis{Evaluate a list of command and return the value of the last command.}
@description{
This creates a ((RascalRuntime)), uses it to evaluate some commands, and then discards the runtime again.

For efficiency's sake it may be better to use ((createRascalRuntime)), and call it's `eval` closure
as often as you need it. 
}
Result[value] eval(list[str] commands, int duration=-1, PathConfig pcfg=pathConfig())  
  throws Timeout, StaticError, ParseError
  = eval(#value, commands, duration=duration, pcfg=pcfg);
 


