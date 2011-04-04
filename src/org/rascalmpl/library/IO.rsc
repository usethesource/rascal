@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bas Basten - Bas.Basten@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module IO

import Exception;

@doc{Print a value on the output stream and add a newline.}
@javaClass{org.rascalmpl.library.IO}
@reflect{for getting IO streams}
public void java println(value arg);

@doc{Print a value on the output stream.}
@javaClass{org.rascalmpl.library.IO}
@reflect{for getting IO streams}
public void java print(value arg);

@doc{Print and return true, for debugging complex Boolean expressions or comprehensions}
public bool bprintln(value arg) 
{
  println(arg);
  return true;
}

@doc{Print a in indented representation of a value and add a newline at the end}
@reflect{for getting IO streams}
@javaClass{org.rascalmpl.library.IO}
public bool java iprintln(value arg); 

@doc{Print a in indented representation of a value}
@reflect{for getting IO streams}
@javaClass{org.rascalmpl.library.IO}
public bool java iprint(value arg); 

@doc{Print and return true, for debugging complex Boolean expressions or comprehensions}
public bool bprint(value arg) 
{
  print(arg);
  return true;
}
    
@doc{Print a value on the output stream, but do not convert parse trees or remove quotes from strings}
@javaClass{org.rascalmpl.library.IO}
@reflect{for getting IO streams}
public void java rprintln(value arg);

@doc{Print a value on the output stream, but do not convert parse trees or remove quotes from strings}
@javaClass{org.rascalmpl.library.IO}
@reflect{for getting IO streams}
public void java rprint(value arg);

@doc{Read a named file as list of strings.}
@deprecated{Use @see str readFile(loc file)}
@javaClass{org.rascalmpl.library.IO}
public list[str] java readFile(str filename)
throws NoSuchFileError(str msg), IOError(str msg);

@doc{Read the contents from a file location}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public str java readFile(loc file)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);

@doc{Check whether a certain location exists}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public bool java exists(loc file);

@doc{Check last modification time of location}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public datetime java lastModified(loc file);

@doc{Check whether a certain location is a directory}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public bool java isDirectory(loc file);

@doc{Create a directory}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public bool java mkDirectory(loc file);

@doc{Check whether a certain location is a file}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public bool java isFile(loc file);

@doc{List the entries in a directory}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public list[str] java listEntries(loc file);

@doc{Read the contents from a file location into a list of lines.}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public list[str] java readFileLines(loc file)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);

@doc{Read the contents from a file location into a list of bytes.}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public list[int] java readFileBytes(loc file)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);


@doc{Write a textual representation of some values to a file
   * If a value is a simple string, the quotes are removed and the contents are de-escaped.
   * If a value has a non-terminal type, the parse tree is unparsed to produce a value.
   * All other values are printed as-is.
   * Each value is terminated by a newline character
}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public void java writeFile(loc file, value V...)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);

@doc{Write a textual representation of some values to a file
   * If a value is a simple string, the quotes are removed and the contents are de-escaped.
   * If a value has a non-terminal type, the parse tree is unparsed to produce a value.
   * All other values are printed as-is.
   * Each value is terminated by a newline character
}
@javaClass{org.rascalmpl.library.IO}
@reflect{Uses URI Resolver Registry}
public void java appendToFile(loc file, value V...)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);

@doc{locate a (file) name in a certain path}
public loc find(str name, list[loc] path) {
  if (dir <- path, f := dir + "/<name>", exists(f)) { 
    return f;
  }
  throw FileNotFound(name);
}
