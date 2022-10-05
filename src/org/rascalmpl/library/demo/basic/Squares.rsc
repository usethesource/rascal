@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{Demo of printing a table of squares}
module demo::basic::Squares

import IO; // <1>

@synopsis{Print a table of squares using a for loop and single line string templates}
void squares(int n) {
  println("Table of squares from 1 to <n>\n"); // <2>
  for (int I <- [1 .. n + 1])
      println("<I> squared = <I * I>");        // <3>
}

@synopsis{a solution with one multi line string template}
str squaresTemplate(int N) // <4>
  = "Table of squares from 1 to <N>
    '<for (int I <- [1 .. N + 1]) {>
    '  <I> squared = <I * I><}>";

