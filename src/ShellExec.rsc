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
public java PID createProcess(str processCommand);

@doc{Start a new external process in the given working directory.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, loc workingDir);

@doc{Start a new external process with the given arguments.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, list[str] args);

@doc{Start a new external process with the given arguments in the given working directory.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, list[str] args, loc workingDir);

@doc{Start a new external process with the given environment variables.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, map[str,str] envVars);

@doc{Start a new external process with the given environment variables in the given working directory.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, map[str,str] envVars, loc workingDir);

@doc{Start a new external process with the given arguments and environment variables.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, list[str] args, map[str,str] envVars);

@doc{Start a new external process with the given arguments and environment variables in the given working directory.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, list[str] args, map[str,str] envVars, loc workingDir);

@doc{Kill a runnning process.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java void killProcess(PID processId);

@doc{Read from an existing process's output stream.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readFrom(PID processId);

@doc{Write to an existing process's input stream.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java void writeTo(PID processId, str msg);

@doc{Process Identifiers}
public alias PID = int;
