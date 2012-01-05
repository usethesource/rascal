/*****************************/
/* DEPRECATED                */
/* Use util::Eval            */
/* DO NOT EDIT               */
/*****************************/


@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}



@deprecated{Use "import util::Eval; instead}
module Scripting



// --- eval with default duration (1000 ms)

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give input string to the Rascal evaluator and return its value}
@reflect
public java value eval(str command) throws Timeout;

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give list of commands to the Rascal evaluator and return value of the last one}
@reflect
public java value eval(list[str] commands) throws Timeout;

// --- eval with given duration (in milliseconds)

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give input string to the Rascal evaluator and return its value within duration ms}
@reflect
public java value eval(str command, int duration) throws Timeout;

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give list of commands to the Rascal evaluator and return value of the last one within duration ms}
@reflect
public java value eval(list[str] commands, int duration) throws Timeout;

// --- evalType with default duration (1000 ms)

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give input string to the Rascal evaluator and return its type as string}
@reflect
public java str evalType(str command) throws Timeout;

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give list of commands to the Rascal evaluator and return the type of the last one}
@reflect
public java str evalType(list[str] commands) throws Timeout;

// --- evalType with given duration (in milliseconds)

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give input string to the Rascal evaluator and return its type as string within duration ms}
@reflect
public java str evalType(str command, int duration) throws Timeout;

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give list of commands to the Rascal evaluator and return the type of the last one within duration ms}
@reflect
public java str evalType(list[str] commands, int duration) throws Timeout;


// --- shell with default duration (1000 ms)

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give input string to the RascalShell and return its output}
@reflect
public java list[str] shell(str commands);

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give list of input strings to the RascalShell and return its output}
@reflect
public java list[str] shell(list[str] commands);

// -- shell with given duration (in milliseconds)

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give input string to the RascalShell and return its output within duration ms}
@reflect
public java list[str] shell(str commands, int duration);

@javaClass{org.rascalmpl.library.util.Eval}
@doc{Give list of input strings to the RascalShell and return its output within duration ms}
@reflect
public java list[str] shell(list[str] commands, int duration);

