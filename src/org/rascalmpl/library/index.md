---
title: Rascal Standard Library
---

#### Synopsis

The Rascal Standard Library contains basic utility functions in relation to all built-in data structures
and language support for a number of common exchange formats and programming languages.

#### Description

The basic "prelude" modules that contain utility functions for daily usages are right here in the 
root of the library. To include them all in one go, use ((Prelude)):

```rascal-shell
import Prelude;
println("Hello Rascal!");
```

The ((IO)) module is otherwise most commonly used, because it contains ((println)).

#### Benefits

* the standard library comes with "batteries included", a number of reusable data-types and data-structures for building code analysis and manipulation tools. See for example ((M3-Core)).
* utility functions are sorted per data-type. So if you need something that operators on ((Rascal:Values-List))then look in the ((List)) module of the library. For ((Rascal:Values-Set)) look in ((Set)), etc.
* to process [CSV]((lang::csv)), [XML](lang::xml)), [JSON](lang::json), [YAML](lang::yaml) you will find utilities here, even even support to access [SQL]((JDBC)) databases via JDBC.

#### Pitfalls

* some languages in ((lang)) are not 100% tested and up-to-date.