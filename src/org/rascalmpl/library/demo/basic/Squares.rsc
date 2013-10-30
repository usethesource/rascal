@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
//START
module demo::basic::Squares

import IO;                                   /*1*/

// Print a table of squares

public void squares(int N){
  println("Table of squares from 1 to <N>"); /*2*/
  for(int I <- [1 .. N + 1])
      println("<I> squared = <I * I>");      /*3*/
}

// a solution with a multi line string template:

public str squaresTemplate(int N) 
  = "Table of squares from 1 to <N>
    '<for (int I <- [1 .. N + 1]) {>
    '  <I> squared = <I * I><}>
    ";

