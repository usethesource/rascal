---
title: CountInLine3
---

#### Synopsis

Count words in a line.

#### Syntax

#### Types

#### Function

#### Description

#### Examples

Here is a clever, albeit rather dense, solution that illustrates several Rascal concepts.
```rascal-include
demo::common::WordCount::CountInLine3
```

                
We use a [reducer]((Rascal:Expressions-Reducer)) that is a recipe to reduce the values produced by one or more generators
  to a single value:
  
* `0` is the initial value of the reducer
*  The pattern match `/\w+/ := S` matches all words in `S`.
*  Reduction is done by `it + 1`. In the latter `it` is a keyword that refers to the
   value that has been reduced sofar. Effectively, the matches are reduced to a match count.


Let's try it:
```rascal-shell
import demo::common::WordCount::CountInLine3;
countInLine3("Jabberwocky by Lewis Carroll");
```

#### Benefits

#### Pitfalls

