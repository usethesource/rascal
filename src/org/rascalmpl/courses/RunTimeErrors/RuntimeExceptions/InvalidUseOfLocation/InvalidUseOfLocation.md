---
title: Invalid Use Of Location
---

.Synopsis
Invalid use of a source location at runtime.

.Types
`data RuntimeException = InvalidUseOfLocation(str message);`
       
.Usage
`import Exception;` (only needed when `InvalidUseOfLocation` is used in `catch`)

.Description
Thrown by operations on [source locations]((Rascal:Values-Location)) that would 
lead to an inconsistent or incomplete location value.

.Examples

```rascal-shell,error
someLoc = |home:///abc.txt|;
someLoc.begin = <1, 2>;
```
