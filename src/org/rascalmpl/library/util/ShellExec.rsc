@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module util::ShellExec

@doc{Start a new external process.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public PID java createProcess(str processCommand);

@doc{Start a new external process with the given arguments.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public PID java createProcessWithArgs(str processCommand, list[str] args);

@doc{Kill a runnning process.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public void java killProcess(PID processId);

@doc{Read from an existing process's output stream.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public str java readFrom(PID processId);

@doc{Write to an existing process's input stream.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public void java writeTo(PID processId, str msg);

@doc{Process Identifiers}
public alias PID = int;