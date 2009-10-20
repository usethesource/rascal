module IO

@doc{Print a list of values on the output stream.}
@javaClass{org.meta_environment.rascal.library.IO}
public void java println(value V...);

@doc{Print and return true, for debugging complex expressions}
public bool print(value V...) 
{
  println(V);
  return true;
}

@doc{Print a list of values on the output stream, but do not convert parse trees or remove quotes from strings}
@javaClass{org.meta_environment.rascal.library.IO}
public void java rawPrintln(value V...);

@doc{Read a named file as list of strings.}
@deprecated{Use @see str readFile(loc file)}
@javaClass{org.meta_environment.rascal.library.IO}
public list[str] java readFile(str filename)
throws NoSuchFileError(str msg), IOError(str msg);

@doc{Read the contents from a file location}
@javaClass{org.meta_environment.rascal.library.IO}
public str java readFile(loc file)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);

@doc{Read the contents from a file location into a list of lines.}
@javaClass{org.meta_environment.rascal.library.IO}
public list[str] java readFileLines(loc file)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);

@doc{Write a textual representation of some values to a file
   * If a value is a simple string, the quotes are removed and the contents are de-escaped.
   * If a value has a non-terminal type, the parse tree is unparsed to produce a value.
   * All other values are printed as-is.
   * Each value is terminated by a newline character
}
@javaClass{org.meta_environment.rascal.library.IO}
public void java writeFile(loc file, value V...)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);

@doc{Write a textual representation of some values to a file
   * If a value is a simple string, the quotes are removed and the contents are de-escaped.
   * If a value has a non-terminal type, the parse tree is unparsed to produce a value.
   * All other values are printed as-is.
   * Each value is terminated by a newline character
}
@javaClass{org.meta_environment.rascal.library.IO}
public void java appendToFile(loc file, value V...)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);
