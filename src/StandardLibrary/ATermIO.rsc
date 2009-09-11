module ATermIO

@doc{read an ATerm from a text file}
@javaClass{org.meta_environment.rascal.std.ATermIO}
public &T java readTextATermFile(type[&T] start, loc location);

public value readTextATermFile(loc location) {
  return readTextATermFile(#value, location);
}

@doc{write an ATerm to a text file}
@javaClass{org.meta_environment.rascal.std.ATermIO}
public void java writeTextATermFile(loc location, value v);