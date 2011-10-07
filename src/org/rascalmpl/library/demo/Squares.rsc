@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
//START
module demo::Squares

import IO;                                   /*1*/

// Print a table of squares

public void squares(int N){
  println("Table of squares from 1 to <N>"); /*2*/
  for(int I <- [1 .. N])
      println("<I> squared = <I * I>");      /*3*/
}

