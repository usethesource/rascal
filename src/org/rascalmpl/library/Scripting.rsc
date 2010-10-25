module Scripting

// --- shell with default duration (1000 ms)

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the RascalShell and return its output}
public list[str] java shell(str commands);

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of input strings to the RascalShell and return its output}
public list[str] java shell(list[str] commands);

// -- shell with given duration (in milliseconds)

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the RascalShell and return its output within duration ms}
public list[str] java shell(str commands, int duration);

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of input strings to the RascalShell and return its output within duration ms}
public list[str] java shell(list[str] commands, int duration);

// --- eval with default duration (1000 ms)

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the Rascal evaluator and return its value}
public value java eval(str command) throws Timeout;

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of commands to the Rascal evaluator and return value of the last one}
public value java eval(list[str] commands) throws Timeout;

// --- eval with given duration (in milliseconds)

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the Rascal evaluator and return its value within duration ms}
public value java eval(str command, int duration) throws Timeout;

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of commands to the Rascal evaluator and return value of the last one within duration ms}
public value java eval(list[str] commands, int duration) throws Timeout;

// --- evalType with default duration (1000 ms)

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the Rascal evaluator and return its type as string}
public str java evalType(str command) throws Timeout;

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of commands to the Rascal evaluator and return the type of the last one}
public str java evalType(list[str] commands) throws Timeout;

// --- evalType with given duration (in milliseconds)

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the Rascal evaluator and return its type as string within duration ms}
public str java evalType(str command, int duration) throws Timeout;

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of commands to the Rascal evaluator and return the type of the last one within duration ms}
public str java evalType(list[str] commands, int duration) throws Timeout;

