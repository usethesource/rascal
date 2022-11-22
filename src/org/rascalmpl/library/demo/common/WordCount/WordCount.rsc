@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{demonstrates different ways of counting words in a string}
@description{
This not only demonstrates counting words using regular expressions and pattern matching with backtracking,
but also highlights the use of functions as parameters to other functions (higher-order functions)
and the concept of ((Reducer))s.
}
module demo::common::WordCount::WordCount

import demo::common::WordCount::CountInLine1;
import demo::common::WordCount::CountInLine2;
import demo::common::WordCount::CountInLine3;
import demo::common::WordCount::Jabberwocky;

import String;
import List;


@synopsis{Count the total amount of words in a list of strings}
@description{
wordCount takes a list of strings and a `countInLine` function
that is applied to each line. The total number of words is returned
}
int wordCount(list[str] input, int (str s) countInLine)
{
  count = 0;
  for (str line <- input){ // <1>
     count += countInLine(line); // <2>
  }
  return count;
}

@synopsis{Count the total amount of words in a list of strings}
@description{
wordCountReduce takes a list of strings and a `countInLine` function
that is applied to each line. The total number of words is returned.
It uses a ((Reducer)) instead of a for loop for brevity.
}
int wordCountReduce(list[str] input, int (str s) countInline)
  = (0 | it + countInline(line) | str line <- input);

@synopsis{Count the total amount of words in a list of strings}
@description{
wordCountMapSum takes a list of strings and a `countInLine` function
that is applied to each line. The total number of words is returned.
It uses a traditional -in functional programming- `map` ((List::mapper) and ((List::sum)) functions from the ((Library)).
}
int wordCountMapSum(list[str] input, int (str s) countInLine)
  = sum(mapper(input, countInLine));


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
