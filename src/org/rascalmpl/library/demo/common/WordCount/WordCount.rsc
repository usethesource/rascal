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
module demo::common::WordCount::WordCount

// wordCount takes a list of strings and a count function
// that is applied to each line. The total number of words is returned

public int wordCount(list[str] input, int (str s) countInLine)
{
  count = 0;
  for(str line <- input){           /*1*/
     count += countInLine(line);    /*2*/
  }
  return count;
}