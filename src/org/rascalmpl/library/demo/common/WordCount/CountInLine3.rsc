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
module demo::common::WordCount::CountInLine3

public int countInLine3(str S){
  return (0 | it + 1 | /\w+/ := S);
}

test bool tstCountInLine3a() = countInLine3("") == 0;

test bool tstCountInLine3b() = countInLine3("Jabberwocky by Lewis Carroll") == 4;


