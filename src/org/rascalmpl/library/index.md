---
title: Standard Library
sidebar_position: 9
---

#### Synopsis

The Rascal Standard Library contains basic utility functions in relation to all built-in data structures,
reusable tools (intermediate formats) for analysis and visualization of software languages,
and language support for a number of common exchange formats and programming languages.

#### Description

The basic `Prelude` modules that contain utility functions for daily usage are right here in the 
root of the library. To include them all in one go, use the ((Prelude)) module:

```rascal-shell
import Prelude;
println("Hello Rascal!");
```

The ((Library:module:IO)) module is otherwise most commonly used, because it contains ((println)).

The standard library has its own location scheme `std:///` which can be used to explore
its folder structure on the REPL. Note that source locations have auto-complete for the path component.

```rascal-shell
|std:///|.ls
```

These are the entry points to the entire library:
(((TOC)))

#### Benefits

* The standard library comes with "batteries included", a number of reusable data-types and data-structures for building code analysis and manipulation tools. See for example ((analysis::m3::Core)).
* Utility functions are sorted per data-type. So if you need something that operates on lists then look in the ((Library:module:List)) module of the library. For sets look in ((Library:module:Set)), etc.
* To process [CSV]((lang::csv)), [XML]((lang::xml)), [JSON]((lang::json)), [YAML]((lang::yaml)) you will find utilities here, even support to access [SQL]((resource::jdbc::JDBC)) databases via JDBC.

#### Pitfalls

* ((Prelude)) is a rather big collection of functions to have imported. It is sometimes better to cherry-pick the modules you need.
* Some languages in ((Library:package:lang)) are not 100% tested and up-to-date. We are extracting these one-by-one to experimental library packages.
