---
title: Word Replacement
---

#### Synopsis

Replace words in a string.

#### Syntax

#### Types

#### Function

#### Description

Suppose you are a book editor and want to ensure that all chapter
and section titles are properly capitalized. Here is how to do this. 

#### Examples

```rascal-include
demo::common::WordReplacement
```

                
<1> We start by introducing a helper function `capitalize` that does the actual capitalization of a single word.
    See [Regular Pattern]((Rascal:Patterns-Regular)) for details about regular expression patterns.
    Next we give two versions of a capitalization functions for a sentence:

<2> `capAll1` uses a while loop to find subsequent words and to replace them by a capitalized version.
<3> `capAll2` uses a ((Rascal:Statements-Visit)) to visit all words in the sentence and replace them by a capitalized version.


Here are some examples:

```rascal-shell
import demo::common::WordReplacement;
capitalize("rascal");
capAll1("turn this into a capitalized title")
capAll2("turn this into a capitalized title")
```

#### Benefits

#### Pitfalls

