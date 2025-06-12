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
@synopsis{Provides string-based programmatic access to a fully functional Rascal execution engine.}
module util::Eval

extend Exception;
extend util::Reflective;
import IO;

@synopsis{Results encode the output of a call to `eval`}
@description{
* `ok` reflects the execution was succesful while there was no output. For example a call to `println` would produce `ok()`.
* `result` captures a value of a certain type, which is parameterized as `&T`.
}
data Result[&T] 
  = ok() 
  | result(&T val)
  ;

@synsopsis{Normally static errors are not run-time exceptions, but `eval` wraps them due to its dynamic nature.}
@description{
`eval` will throw the first static error that is blocking the execution of a command.
}
data RuntimeException 
  = StaticError(str message, loc location)
  ; 

@synopsis{A reusable instance of the Rascal runtime system configured by a specific PathConfig.}
@description{
* `pcfg` documents the configuration parameters for this specific Rascal run-time
* `reset()` when called clears the heap of imported modules and all the local declarations in the top-level module instance.
* `eval(typ, command)` evaluates the command, expecting a result of type `typ`. 
   * The `typ` parameter must be a supertype of any output expected from the `eval` command. If this is not the case, then a run-time exception will be thrown. It is always safe to put `#value` there.
   * The `command` parameter can be one of Statement, Declaration, Import, Extend or SyntaxDefinition.
   * For evaluating an Expression simply add a semicolon to the end: `1 + 1;`
* staticTypeOf(command) computes the static type of the entire command and returns it as a reified type.
* setTimeout(duration) sets the timeout in milliseconds for both the eval and the staticTypeOf function. A 
negative number disables the timeout feature. By default the timeout is off.
}
@examples{
```rascal-shell
import util::Eval;
e = createRascalRuntime();
e.eval(#int, "2 * 3;");
```

A run-time can keep state between calls of `eval`:
```rascal-shell
import util::Eval;
e = createRascalRuntime();
e.eval(#void, "import IO;");
e.eval(#int, "int a = 1;");
e.eval(#void, "println(a)");
```
}
@benefits{
* Creating a single run-time engine is an expensive operation. By reusing it you
can safe a lot of time and space. Use `.reset()` to reuse the configuration while dropping
all other state and returning to an initial runtime.
* The PathConfig parameter completelu defines the configuration of the ((RascalRuntime)).
}
@pitfalls{
* To turn a value string into an actual value, it's better and faster to use ((readTextValueString)) or ((readTextValueFile)).
* Parsing file paths is better done using ((lang::paths::Unix::parseUnixPath)) and ((lang::paths::Windows::parseWindowsPath)).
* A ((RascalRuntime)) is neither thread-safe nor thread-friendly. 
* `staticTypeOf` is an abstract interpreter which can take as much time as running the program. 
}
data RascalRuntime
  = evaluator(
      PathConfig pcfg,
      void () reset,
      Result[&T] (type[&T] typ, str command) eval,
      type[value] (str command) staticTypeOf,
      void (int duration) setTimeout 
  );

@synopsis{Workhorse to instantiate a working ((RascalRuntime))/}
@javaClass{org.rascalmpl.library.util.Eval}
@description{
See ((RascalRuntime)) on how to use a configured and stateful runtime
to evaluate Rascal commands or the static type checker.
}
java RascalRuntime createRascalRuntime(PathConfig pcfg=pathConfig());

@synopsis{Evaluate a single command and return its return value.}
@description{
This creates a ((RascalRuntime)), uses it to evaluate one command, and then discards the runtime again.
}
@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.}
Result[&T] eval(type[&T] typ, str command, int duration=-1, PathConfig pcfg=pathConfig()) 
  throws Timeout, StaticError, ParseError
  = eval(typ, [command], pcfg=pcfg, duration=duration);

@synopsis{Evaluate a list of command and return the value of the last command.}
@description{
This creates a ((RascalRuntime)), uses it to evaluate some commands, and then discards the runtime again.
}
@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.}
Result[&T] eval(type[&T] typ, list[str] commands, int duration=-1, PathConfig pcfg=pathConfig()) 
  throws Timeout, StaticError, ParseError {
    e = createRascalRuntime(pcfg=pcfg);
    if (duration?) {
      e.setTimeout(duration);
    }

    for (command <- commands[..-1]) {
      e.eval(#value, command);
    }

    return e.eval(typ, commands[-1]);
}

@synopsis{Evaluate a list of command and return the value of the last command.}
@description{
This creates a ((RascalRuntime)), uses it to evaluate some commands, and then discards the runtime again.
}
@deprecated{Use ((createRascalRuntime)) instead for better efficiency and configurability.}
Result[value] eval(list[str] commands, int duration=-1, PathConfig pcfg=pathConfig())  
  throws Timeout, StaticError, ParseError
  = eval(#value, commands, duration=duration, pcfg=pcfg);

@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.} 
@synopsis{Evaluate a command and return the value, unless the `duration` amount of milliseconds has passed first.}
Result[&T] eval(type[&T] typ, str command, int duration) throws Timeout, StaticError, ParseError
  = eval(typ, command, duration=duration);

@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.} 
@synopsis{Evaluate a list of commands and return the value of the last command, unless the `duration` amount of milliseconds has passed first.}
Result[&T] eval(type[&T] typ, list[str] commands, int duration) throws Timeout, StaticError, ParseError
  = eval(typ, commands, duration=duration);

@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.} 

Result[value] eval(list[str] commands, int duration) 
  = eval(#value, commands, duration=duration);

@synopsis{Give input string to the Rascal evaluator and return its type as string.}
@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.} 
str evalType(str command, PathConfig pcfg=pathConfig(), int duration = -1) throws Timeout, StaticError, ParseError {
  e = createRascalRuntime(pcfg=pcfg);
  if (duration?) {
    e.setTimeout(duration);
  }

  return "<createRascalRuntime(pcfg=pcfg).staticTypeOf(command)>";
}

@synopsis{Give input strings to the Rascal evaluator and return the type of the last command as a string.}
@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.} 
str evalType(list[str] commands, PathConfig pcfg=pathConfig(), int duration = -1) throws Timeout, StaticError, ParseError {
    e = createRascalRuntime(pcfg=pcfg);
    if (duration?) {
      e.setTimeout(duration);
    }

    for (command <- commands[..-1]) {
      e.eval(#value, command);
    }

    return "<e.staticTypeOf(commands[-1])>";
}

@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.} 
@synopsis{Return the static type of the given command unless `duration` milliseconds pass before that.}
str evalType(str command, int duration, PathConfig pcfg=pathConfig()) throws Timeout, StaticError, ParseError
  = evalType(command, pcfg=pcfg, duration=duration);

@synopsis{Give list of commands to the Rascal evaluator and return the type of the last one within duration ms.}
@deprecated{Use ((createRascalRuntime)) for better efficiency/configurability.} 
str evalType(list[str] commands, int duration, PathConfig pcfg=pathConfig()) throws Timeout, StaticError, ParseError
  = evalType(commands, pcfg=pcfg, duration=duration);

@synopsis{Tests and demonstrates how to work with an encapsulated stateful Rascal runtime.}
test bool stateFulEvalCanReset() {
  e = createRascalRuntime();

  // import works
  e.eval(#void, "import IO;");
  // declaration
  e.eval(#int, "int a = 42;");
  // use of previous declaration
  assert e.eval(#int, "2 * a;") == result(84);
  // use of previous import without error
  e.eval(#void, "println(a)");
  // clear everything
  e.reset();
  try {
     // use of undeclared variable
     e.eval(#int, "a");
     assert false;
  }
  catch StaticError(_, _):
    assert true;

  return true;
}

@synopsis{Tests and demonstrates using a Rascal runtime with a millisecond timeout feature.}
test bool evalTimeoutWorks() {
  e = createRascalRuntime();
  e.setTimeout(10);

  try {
    e.eval(#int, "(0 | it + 1 | i \<- [0..1000000])");
    assert false;
  }
  catch Timeout(): 
    assert true;

  return true;
}

@synsopis{Tests and demonstrates the use of PathConfig to configure a Rascal runtime.}
test bool evalWithOwnPathConfig() {
  e = createRascalRuntime(
    pcfg=pathConfig(
      srcs=[|memory://evalTests|]
    )
  );

  writeFile(|memory://evalTests/MyTestModule.rsc|,
    "module MyTestModule
    'extend IO;
    'public int a = 42;");
  
  e.eval(#void, "import MyTestModule;");
  e.eval(#void, "println(a)");

  return e.eval(#int, "a") == result(42);
}

test bool testStaticTypeOf() {
  e = createRascalRuntime();
  return e.staticTypeOf("1") == #int;
}


