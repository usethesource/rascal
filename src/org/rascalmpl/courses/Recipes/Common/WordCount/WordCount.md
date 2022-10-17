---
title: Word Count
---

#### Synopsis

Counting words in strings.

#### Syntax

#### Types

#### Function

#### Description

#### Examples

The purpose of WordCount is to count the number of _words_ in a list of lines (strings).
A word is here defined as one or more letters (lowercase or uppercase), digits and the underscore character (`_`).

We split the problem in two parts:

*  Count the words in a single line. We explore three ways to do this in an imperative (((CountInLine1))], ((CountInLine2)))
  and a functional style (((CountInLine3))).
*  Next we apply the single line counter to all the lines.


`wordCount` is a function with two arguments:
*  A list of lines.
*  A function that returns the number of words in a line.


The main task of `wordCount` is to loop over all lines and to add the word counts per line.


```rascal-include
demo::common::WordCount::WordCount
```

                
<1> An [enumerator]((Rascal:Comprehensions-Enumerator)) is used to generated all the lines in the list of lines.
<2> The argument function `countInLine` is applied to count the number of words in each line.

Let's now do some experiments using the ((Jabberwocky)) poem by Lewis Carrol as input.

```rascal-shell
import demo::common::WordCount::WordCount;
import demo::common::WordCount::CountInLine1;
import demo::common::WordCount::CountInLine2;
import demo::common::WordCount::CountInLine3;
import demo::common::WordCount::Jabberwocky;
wordCount(Jabberwocky, countInLine1);
wordCount(Jabberwocky, countInLine2);
wordCount(Jabberwocky, countInLine3);
```
It is satisfactory that the three ways of counting words all yield the same result.

If you are into one-liners, we can include everything you learned from this example
in the following alternative `wordCount2` function:
```rascal-shell,continue
int wordCount2(list[str] lines) = (0 | it + (0 | it + 1 | /\w+/ := line) | str line <- lines);
wordCount2(Jabberwocky);
```
The function body contains two nested [reducers]((Rascal:Expressions-Reducer)).
The inner reducer counts the number of words in a line, the outer reducer accumulates all line word counts.

```rascal-shell,continue
```


#### Benefits

#### Pitfalls

