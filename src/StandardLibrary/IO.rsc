module IO

public void java println(value V...)
@doc{println -- print a list of values on the output stream.}
@javaClass{org.meta_environment.rascal.std.IO};

public list[str] java readFile(str filename)
throws NoSuchFileError(str msg), IOError(str msg)
@doc{readFile -- read a named file as list of strings.}
@javaClass{org.meta_environment.rascal.std.IO};


