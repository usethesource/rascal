@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
//START
// tag::module[]
module demo::common::WordCount::WordCount

import demo::common::WordCount::CountInLine1;
import demo::common::WordCount::CountInLine2;
import demo::common::WordCount::CountInLine3;
import demo::common::WordCount::Jabberwocky;

import String;

// wordCount takes a list of strings and a count function
// that is applied to each line. The total number of words is returned

int wordCount(list[str] input, int (str s) countInLine)
{
  count = 0;
  for(str line <- input){ // <1>
     count += countInLine(line); // <2>
  }
  return count;
}
// end::module[]

test bool tstWordCount1() = wordCount(Jabberwocky, countInLine1) == wordCount(Jabberwocky, countInLine2);

test bool tstWordCount2() = wordCount(Jabberwocky, countInLine1) == wordCount(Jabberwocky, countInLine3);

test bool tstWordCount3() = wordCount(Jabberwocky, countInLine2) == wordCount(Jabberwocky, countInLine3);

test bool tstWordCount4(str txt) {
    lines = split(txt, "\n");
    return wordCount(lines, countInLine1) == wordCount(lines, countInLine2);
}    
    
test bool tstWordCount5(str txt) {
    lines = split(txt, "\n"); 
    return wordCount(lines, countInLine1) == wordCount(lines, countInLine3); 
}

    
test bool tstWordCount6(str txt) {
    lines = split(txt, "\n");  
    return wordCount(lines, countInLine2) == wordCount(lines, countInLine3);
}
