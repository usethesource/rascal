module IO

public void java println(value V...)
@javaClass{org.meta_environment.rascal.std.IO};

public list[str] java readFile(str filename)
throws NoSuchFileError(str msg), IOError(str msg)
@javaClass{org.meta_environment.rascal.std.IO};


