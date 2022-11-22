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
module demo::common::WordCount::CountInLine2

@synopsis{use a while loop to slowly walk through the string word-by-word}
int countInLine2(str S){
  int count = 0;
  
  // \w matches any word character
  // \W matches any non-word character
  // <...> are groups and should appear at the top level.
  while (/^\W*\w+<rest:.*$>/ := S) { 
    count += 1; 
    S = rest; 
  }
  return count;
}
// end::module[]
test bool tstCountInLine2a() = countInLine2("") == 0;

test bool tstCountInLine2b() = countInLine2("Jabberwocky by Lewis Carroll") == 4;
