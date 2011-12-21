@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Jeroen van den Bos - Jeroen.van.den.Bos@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
//START
module Exception

/*
 * This data type declares all exceptions that are thrown by the
 * Rascal run-time environment which can be caught by a Rascal program.
 */

data RuntimeException = 
      EmptyList()
    | EmptyMap() 
    | EmptySet()
    | IndexOutOfBounds(int index)
    | AssertionFailed() 
    | AssertionFailed(str label)
    | NoSuchElement(value v)
    | IllegalArgument(value v, str message)
    | IllegalArgument(value v)
    | IllegalArgument()
    | IO(str message)
    | PathNotFound(loc l)
    | FileNotFound(str file)
    | SchemeNotSupported(loc l)
    | HostNotFound(loc l)
    | AccessDenied(loc l)
    | PermissionDenied()
    | PermissionDenied(str message)
    | ModuleNotFound(str name)
    | NoSuchKey(value key)
    | NoSuchAnnotation(str label)
    | Java(str message)
    | ParseError(loc location)
    | IllegalIdentifier(str name)
    | MissingCase(value x)
    | Subversion(str message)
    | StackOverflow()
    | Timeout()
	;
