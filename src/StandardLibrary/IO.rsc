module IO

@doc{Print a list of values on the output stream.}
@javaClass{org.meta_environment.rascal.std.IO}
public void java println(value V...);

@doc{Read a named file as list of strings.}
@javaClass{org.meta_environment.rascal.std.IO}
public list[str] java readFile(str filename)
throws NoSuchFileError(str msg), IOError(str msg);

@doc{Print and return true, for debugging complex expressions}
public bool print(value V...) 
{
  println(V);
  return true;
}
