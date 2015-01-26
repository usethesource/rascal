@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}

@doc{
Synopsis: Execute and manage external processes.
}
module util::ShellExec

@doc{
Synopsis: Start a new external process.
Description:
#  Start a new external process.
#  Start a new external process in a given working directory.
#  Start a new external process with the given arguments.
#  Start a new external process with the given arguments in the given working directory.
#  Start a new external process with the given environment variables.
#  Start a new external process with the given environment variables in the given working directory.
#  Start a new external process with the given arguments and environment variables.
#  Start a new external process with the given arguments and environment variables in the given working directory.

}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand);

@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, loc workingDir);

@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, list[str] args);

@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, list[str] args, loc workingDir);

@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, map[str,str] envVars);

@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, map[str,str] envVars, loc workingDir);

@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, list[str] args, map[str,str] envVars);

@javaClass{org.rascalmpl.library.util.ShellExec}
public java PID createProcess(str processCommand, list[str] args, map[str,str] envVars, loc workingDir);

@doc{
Synopsis: Kill a running process.
}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java void killProcess(PID processId);

@doc{
Synopsis: Read from an existing process's output stream. This is non-blocking.
}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readFrom(PID processId);

@doc{
Synopsis: Read from an existing process's error output stream. This is non-blocking.
}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readFromErr(PID processId);

@doc{
Synopsis: Read the entire stream from an existing process's output stream. This is blocking.
}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readEntireStream(PID processId);

@doc{
Synopsis: Read the entire error stream from an existing process's output stream. This is blocking.
}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readEntireErrStream(PID processId);

@doc{
Synopsis: Write to an existing process's input stream.
}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java void writeTo(PID processId, str msg);

@doc{
Synopsis: Process Identifiers (PID).

Description: Process IDentifiers (PIDs) are used to identify external processes.
A PID is returned by [createProcess] and is required for any further interaction with the created process.
}
public alias PID = int;
