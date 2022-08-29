# CountInLine1

.Synopsis
Count words in a line.

.Syntax

.Types

.Function

.Details


.Description

.Examples
We count words using a regular expression match in a for loop.
Each time that the pattern `/[a-zA-Z0-9_]+/` matches, the body of the loop is executed
and `count` is incremented.
```rascal
include::{LibDir}demo/common/WordCount/CountInLine1.rsc[tags=module]
```

                
Let's try it:
```rascal-shell
import demo::common::WordCount::CountInLine1;
countInLine1("Jabberwocky by Lewis Carroll");
```

.Benefits

.Pitfalls

