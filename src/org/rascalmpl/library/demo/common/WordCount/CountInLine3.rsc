@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{Count words in a string}
module demo::common::WordCount::CountInLine3

@synopsis{Use a ((Reducer)) to flip through all the possible matches and count them.}
int countInLine3(str s) {
  return (0 | it + 1 | /\w+/ := s);
}

test bool tstCountInLine3a() = countInLine3("") == 0;

test bool tstCountInLine3b() = countInLine3("Jabberwocky by Lewis Carroll") == 4;


