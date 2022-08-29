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

public data Result[&T] = ok() | result(&T val);

public data Exception 
  = StaticError(str message, loc location)
  ; 
 
@doc{
.Synopsis
Evaluate a (list of) Rascal commands and return the value of the last command.

.Description
Evaluate a command or a list of commands and return the value of the last command that is executed.

Note that a command can be one of:

*  Statement
*  Declaration
*  Import
*  Extend
*  SyntaxDefinition
   

The notable exclusion are ((Expressions)). An Expression is not allowed as a command to the eval function. You can easily make
a Statement from an Expression by adding a semi-colon.
 
An optional `duration` argument may be present to limit the time
(in milliseconds) the execution may take. By default, the duration is set to 1000 ms.

.Examples

```rascal-shell
import util::Eval;
eval("2 * 3;");
eval(["X = 2 * 3;", "X + 5;"]);
```

}
// --- eval with default duration (1000 ms)

// -- Give input string to the Rascal evaluator and return its value
@javaClass{org.rascalmpl.library.util.Eval}
public java Result[&T] eval(type[&T] typ, str command) throws Timeout, StaticError, ParseError;
public Result[value] eval(str command)  = eval(#value, command);

// -- Give list of commands to the Rascal evaluator and return value of the last one.
@javaClass{org.rascalmpl.library.util.Eval}
public java Result[&T] eval(type[&T] typ, list[str] commands) throws Timeout, StaticError, ParseError;
public Result[value] eval(list[str] commands)  = eval(#value, commands);
 
// --- eval with given duration (in milliseconds)

// -- Give input string to the Rascal evaluator and return its value within duration ms.
@javaClass{org.rascalmpl.library.util.Eval}
public java Result[&T] eval(type[&T] typ, str command, int duration) throws Timeout, StaticError, ParseError;
public Result[value] eval(str command, int duration)  = eval(#value, command, duration);

// -- Give list of commands to the Rascal evaluator and return value of the last one within duration ms.
@javaClass{org.rascalmpl.library.util.Eval}
public java Result[&T] eval(type[&T] typ, list[str] commands, int duration) throws Timeout, StaticError, ParseError;
public Result[value] eval(list[str] commands, int duration) = eval(#value, commands, duration);


@doc{
.Synopsis
Evaluate a (list of) Rascal commands and return the type of the last command.

.Description
Evaluate a command or a list of commands and return the type of the value of the last command that is executed.
An optional `duration` argument may be present to limit the time
(in milliseconds) the execution may take. By default, the duration is set to 1000 ms.

.Examples
```rascal-shell
import util::Eval;
evalType("2 * 3;");
evalType("[1, 2, 3];");
```
}
// --- evalType with default duration (1000 ms)

// -- Give input string to the Rascal evaluator and return its type as string.
@javaClass{org.rascalmpl.library.util.Eval}
public java str evalType(str command) throws Timeout, StaticError, ParseError;

// -- Give list of commands to the Rascal evaluator and return the type of the last one.
@javaClass{org.rascalmpl.library.util.Eval}
public java str evalType(list[str] commands) throws Timeout, StaticError, ParseError;

// --- evalType with given duration (in milliseconds)

//-- Give input string to the Rascal evaluator and return its type as string within duration ms.
@javaClass{org.rascalmpl.library.util.Eval}
public java str evalType(str command, int duration) throws Timeout, StaticError, ParseError;

// -- Give list of commands to the Rascal evaluator and return the type of the last one within duration ms.
@javaClass{org.rascalmpl.library.util.Eval}
public java str evalType(list[str] commands, int duration) throws Timeout, StaticError, ParseError;

