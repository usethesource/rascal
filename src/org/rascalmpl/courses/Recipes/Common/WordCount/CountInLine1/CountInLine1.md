---
title: CountInLine1
---

#### Synopsis

Count words in a line.

#### Syntax

#### Types

#### Function

#### Description

#### Examples

We count words using a regular expression match in a for loop.
Each time that the pattern `/[a-zA-Z0-9_]+/` matches, the body of the loop is executed
and `count` is incremented.
```rascal-include
demo::common::WordCount::CountInLine1
```

                
Let's try it:
```rascal-shell
import demo::common::WordCount::CountInLine1;
countInLine1("Jabberwocky by Lewis Carroll");
```

#### Benefits

#### Pitfalls

