---
title: Parse Error
---

.Synopsis
Parse of a syntactically incorrect string.


.Types
`data RuntimeException = ParseError(loc parseloc) | ParseError(loc parseloc, str nt, str s);`
       
.Usage
`import Exception;` (only needed when `ParseError` is used in `catch`)


.Description
This error is generated when during the execution of a Rascal program the
[parse]((Library:ParseTree-parse)) function is applied to a syntactically incorrect input sentence.

Remedies:

*  Correct the input sentence.
*  Adapt the grammar so that it accepts the input sentence.
*  Catch the ParseError yourself, see [try catch]((Rascal:Statements-TryCatch)).


.Examples
Define the non-terminal `As` that accepts one or more letters `a`:
```rascal-shell
syntax As = "a"+;
```
Then import `ParseTree` so that we can use the `parse` function:
```rascal-shell,continue
import ParseTree;
```
Now we can parse sentences consisting of letters `a`:
```rascal-shell,continue
parse(#As, "aaaaaaaa");
```
But we get an error when parsing syntactically incorrect input  (i.e., that does not
consists of letters `a` only):
```rascal-shell,continue,error
parse(#As, "aaaabaaa");
```
We can also catch the ParseError but first import the Rascal modules `Exception` and `IO`:
```rascal-shell,continue
import Exception;
import IO;
try 
  parse(#As, "aaaabaaa"); 
catch ParseError(e): 
  println("Your input cannot be parsed: <e>");
```


.Benefits

.Pitfalls

