---
title: Reporting
---

.Synopsis
How to format and report errors, warnings and info messages.

.Description

Reporting may be done both in the Collector and the Solver. 
It uses values of the datatype `FailMessage` that can be created as follows:

```rascal
FailMessage error(value src, str msg, value args...);
FailMessage warning(value src, str msg, value args...);
FailMessage info(value src, str msg, value args...);
```

Here, 

* `src` is a `Tree` or `loc` that is the subject of the report.
* `msg` is an informative string to be printed. It may contain _insertion directives_.
* `args` are zero or more values to be inserted in the insertion directives in `msg`.

The following insertion directives are supported:

* `%t`: insert the type of the next element of `args`.
* `%v`: insert the value of the next element of `args`.
* `%q`: quote and insert the type or value of the next element of `args`.
* `%%`: insert the character `%`.
