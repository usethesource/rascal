@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module lang::csv::IO

@doc{
Synopsis: Read a relation from a CSV (Comma Separated Values) file.
}
@javaClass{org.rascalmpl.library.lang.csv.IO}
@reflect{Uses URI Resolver Registry}
public java &T readCSV(type[&T] result, loc location);

@javaClass{org.rascalmpl.library.lang.csv.IO}
@reflect{Uses URI Resolver Registry}
public java &T readCSV(type[&T] result, loc location, str separator);