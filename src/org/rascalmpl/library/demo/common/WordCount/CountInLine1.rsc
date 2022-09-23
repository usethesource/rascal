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
module demo::common::WordCount::CountInLine1

@synopsis{regular expressions in a for loop}
int countInLine1(str s) {
  int count = 0;
  for (/[a-zA-Z0-9_]+/ := s) {
    count += 1;
  }
  return count;
}


test bool tstCountInLine1a() = countInLine1("") == 0;

test bool tstCountInLine1b() = countInLine1("Jabberwocky by Lewis Carroll") == 4;
