---
title: RegExp Syntax Error
---

.Synopsis
Syntax error in regular expression at run time

.Types
`data RuntimeException = RegExpSyntaxError(str message);`
       
.Usage
`import Exception;` (only needed when `RegExpSyntaxError` is used in `catch`)

.Description
At the moment, the parsing and compilation of regular expression is done at run time.
This exception is thrown when a syntactically incorrect regular expression is used.

.Examples
The following regular expression `/+/` is incorrect (maybe `/a+/` was meant?):
```rascal-shell,error
/+/ := "aaaa";
```
