module Scripting

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the RascalShell and return its output}
public list[str] java shell(str commands);

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of input strings to the RascalShell and return its output}
public list[str] java shell(list[str] commands);

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the Rascal evaluator and return its value}
public value java eval(str command);

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of commands to the Rascal evaluator and return value of the last one}
public value java eval(list[str] commands);

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give input string to the Rascal evaluator and return its type as string}
public str java evalType(str command);

@javaClass{org.rascalmpl.library.Scripting}
@doc{Give list of commands to the Rascal evaluator and return the type of the last one}
public str java evalType(list[str] commands);