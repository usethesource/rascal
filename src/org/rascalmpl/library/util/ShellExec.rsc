@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}

@synopsis{Execute and manage external processes.}
module util::ShellExec

@synopsis{Start a new external process.}
@deprecrated{Use the createProcess function that takes `loc` for processCommand for better portability behavior between operating systems.}
@javaClass{org.rascalmpl.library.util.ShellExec}
java PID createProcess(str processCommand, loc workingDir=|cwd:///|, list[str] args = [], map[str,str] envVars = ());

@synopsis{Start a new external process.}
@description{
The file schemes that are allowed for the `processCommand`` are limited to the `file:///` schemes
and all logical schemes that directly resolve to `file:///` such as `cwd:///` and `tmp:///`.

The arguments to `args` given are all converted to strings before passing them into the command.
Special treatment is given to `loc` arguments, which are first resolved to `file:///` schemes and
then printed to OS-specific absolute path names.

For environment variables in `envVars` the same treatment is given to convert values to strings.
}
@javaClass{org.rascalmpl.library.util.ShellExec}
java PID createProcess(loc processCommand, loc workingDir=|cwd:///|, list[value] args = [], map[str, value] envVars = ());

@synopis{start, run and kill an external process returning its output as a string.}
@deprecrated{Use the `exec`` function that takes `loc` for processCommand for better portability behavior between operating systems.}
str exec(str processCommand, loc workingDir=|cwd:///|, list[str] args = [], map[str, str] env = ()) {
   pid = createProcess(processCommand, workingDir=workingDir, args=args, envVars=env);
   result = readEntireStream(pid);
   killProcess(pid);
   return result;
}

str exec(loc processCommand, loc workingDir=|cwd:///|, list[value] args = [], map[str, value] env = ()) {
   pid = createProcess(processCommand, workingDir=workingDir, args=args, envVars=env);
   result = readEntireStream(pid);
   killProcess(pid);
   return result;
}

@deprecrated{Use the `execWithCode` function that takes `loc` for processCommand for better portability behavior between operating systems.}
tuple[str output, int exitCode] execWithCode(str processCommand, loc workingDir=|cwd:///|, list[str] args = [], map[str, str] env = ()) {
    pid = createProcess(processCommand, workingDir=workingDir, args=args, envVars=env);
    result = readEntireStream(pid);
    code = exitCode(pid);
    killProcess(pid);

    return <result, exitCode(pid)>;
}

tuple[str output, int exitCode] execWithCode(loc processCommand, loc workingDir=|cwd:///|, list[value] args = [], map[str, value] env = ()) {
    pid = createProcess(processCommand, workingDir=workingDir, args=args, envVars=env);
    result = readEntireStream(pid);
    code = exitCode(pid);
    killProcess(pid);

    return <result, exitCode(pid)>;
}


@synopsis{Kill a running process, or a zombie process (a process which is not alive yet not killed)}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java int killProcess(PID processId, bool force=false);

@synopsis{Check whether a process is still alive}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java bool isAlive(PID processId);

@synopsis{Check whether a process is still registered but not actually running anymore. A zombie process may be cleaned up using killProcess.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java bool isZombie(PID processId);

@synopsis{Waits for the process to exit and then returns its return code. This is a blocking operation.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java int exitCode(PID processId);

@synopsis{Read from an existing process's output stream. This is non-blocking.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readFrom(PID processId);

@synopsis{Read from an existing process's output stream with a given wait timeout. Some processes are a little slower in producing output. The wait is used to give the process some extra time in producing output. This is non-blocking apart from the waiting.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readWithWait(PID processId, int wait);

@synopsis{Read from an existing process's error output stream. This is non-blocking.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readFromErr(PID processId);

@synopsis{Read from an existing process's error output stream. This blocks until a full line is read and waits for one second maximally for this line to appear.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readLineFromErr(PID processId, int wait=200, int maxTries=5);

@synopsis{Read the entire stream from an existing process's output stream. This is blocking.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readEntireStream(PID processId);

@synopsis{Read the entire error stream from an existing process's output stream. This is blocking.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java str readEntireErrStream(PID processId);

@synopsis{Write to an existing process's input stream.}
@javaClass{org.rascalmpl.library.util.ShellExec}
public java void writeTo(PID processId, str msg);

@synopsis{Process Identifiers (PID).}
@description{
A PID is returned by ((createProcess)) and is required for any further interaction with the created process.
}
public alias PID = int;
