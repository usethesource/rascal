
## Release 0.28.1 - December 2022

* vis::Charts, vis::Graphs and vis::Text were added to the standard library for visualizing charts, graphs and pretty textual values.
* for Eclipse, several older issues and newer issues were fixed in the run-time of the Figure library
* in Eclipse and VScode the new logo was applied, as well as on http://www.rascal-mpl.org
* util::Validator was added to the standard library: a generic validation module for loading node values produced by reading in XML, JSON or YAML (for example) as verified constructors of algebraic data-types.
* In lang::json::IO  the writing of Rascal values to JSON streams was simplified and rationalized. Now every constructor application corresponds to one object, and the fields of Rascal constructors and nodes (keyword fields as well as positional) are always mapped one-to-one to JSON fields by name. Mapping back from JSON values to Rascal can be hard if there are more than one constructors for a data type. In this case have a look at util::Validator.
* Most documentation was moved from the rascal project to the rascal-website project; this will make it easier and faster to contribute to (fixes in) the Rascal documentation.
* Rascal-tutor compiler was improved in many dimensions: screenshots of visuals were added and the compiler is incremental now per Markdown/Rascal file.


## Release 0.26.5 - November 8, 2022

* the documentation compiler, a.k.a. course compiler, a.k.a. tutor, was separated into its own top-level project called [rascal-tutor](https://www.github.com/usethesource/rascal-tutor) 
* 80% of the documentation was reviewed and fixed
   * ported from asciidoctor to Docusaurus markdown 
   * using new features of the new tutor compiler 
   * all code examples run again
   * all broken links fixed
   * broken rascal-eclipse library documentation moved to the rascal-eclipse project
* in `util::Benchmark` several overloaded functions were renamed to fix static typing issues.
* in `IO` read and write functions with positional encoding parameters were made `@deprecated` in favor of their simpler counterparts with keywordparameters for the encoding.
* rascal no longer depends on jruby or asciidoctor 
* `Node::unsetRec` was optimized which led to a large speed improvement of the parser generator
* added `PATH:///` logical file resolver which works well in combination with `util::ShellExec` for finding executables in the system `PATH` variable to run.
* `util::ShellExec` can now deal with `loc` values anywhere the API expects a file location. This goes also for file parameters to executables. Before they are passed to the executable, Rascal finds or creates and absolute file system path with the contents of the file.

## Release 0.25.x 

Release 0.25.x were intermediate releases required to eliminate the old tutor from the rascal package. They never made it into an IDE like VScode or Eclipse and no stable commandline release was distributed either.

## Release 0.24.0 - June 21, 2022

Release 24.x is a maintenance release. A lot of changes happened between 0.17.0 and 0.23.x, so if you have not looked here for a while, go to the [release notes for 0.23.x](/release-notes/rascal-0-23-x-release-notes)

* ParseTree::parser and ParseTree::parsers now generate parsing closures that do not need a JVM lock to synchronize on the Evaluator anymore. 
* Also both functions now capture the generated Class<?> instance instead of a handle to the grammar, for efficiency's sake
* The interpreter's implementation of concrete syntax does not do its own parser caching anymore. Instead it relies on the aforementioned
parsing closures. This saves memory (fewer hash-tables) and also more sharing is possible between parsers for different modules that happen to have the same grammar.
* Issue #1615 was solved
* Issue #1614 was solved
* Issue #1613 was solved by providing `loc` based interfaces to files and programs passed to util::ShellExec. The old `str` based interfaces are now deprecated.

## Release 0.23.0 - April 10th, 2022

The 0.23.0 release marks the end of the migration of all Rascal supporting libraries (vallang, capsule) and the Rascal interpreter to **Java 11**. The contributors include Paul Klint, Jouke Stoel, Mauricio Verano Merino, Tijs van der Storm, Rodin Aarssen, Arnold Lankamp, Davy Landman, Pieter Olivier and Jurgen Vinju.

We report on intermediate releases 0.18.x, 0.19.x, 0.20.x, 0.21.x and 0.22.x as well, as these had not lead to a release of the Rascal Eclipse plugins or a stable release of the commandline version. 

**Generating Eclipse and VScode Plugins**

This port to Java 11 implies that the current [stable release of the Rascal Eclipse plugin](https://update.rascal-mpl.org/stable/) works again with the latest releases of Eclipse from 2021 and 2022. In the meantime
releases of the [rascal-language-server extension](https://marketplace.visualstudio.com/items?itemName=usethesource.rascalmpl) for VScode have been developed. 

Porting Rascal's IDE generator features to VScode has had minor impact on the modules in the standard library concerning interaction with DSLs and interactions with the IDE (`util::IDEServices`, `util::Monitor` and `util::IDE`). The old API still works and it is backward compatible in Eclipse. However, if you want to port your DSL to VScode, there are minor changes in how to wrap your extension and new interaction possibilities with the IDE which are not present in Eclipse. Not all new IDE API is available in Eclipse yet either. Both directions of consolidation (VScode <-> Eclipse) are interesting directions of future work.

This release works best with Java 11, but not with higher versions. There are still illegal uses of reflective access which need to be resolved before Rascal runs correctly on Java 13 and higher.

**Hosting Releases and Continuous Integration**

* http://releases.usethesource.io is the current place for all Rascal related releases. 
* The Eclipse stable update site is still at https://update.rascal-mpl.org/stable/. 
* http://nexus.usethesource.io/ is still functional but officially deprecated. 
* If you want to test release candidates, then the URL is https://update.rascal-mpl.org/release, but we must warn you that regressing to an earlier version is not supported, so you'd have to reinstall Eclipse from scratch for that.
* We migrated our continuous integration scripts to GitHub actions. Thanks to Jenkins for the many years of service and the support from CWI staff and management to keep our servers funded, safe and sound.

**Sponsoring**

It has become possible to [sponsor](https://github.com/sponsors/usethesource) the Rascal project. If you are considering, please do not hesitate to contact us.

**Other changes**

* the `private` access modifier is now idempotent for a name of a function, data-type or syntax non-terminal within single module: in other words, if one of the alternatives is marked private, then all of them are private.
* caching using the @memo tag is done smarter (faster and cleaner cache clearing)
* run-time bugs around type-parametrized ADTs were fixed
* support for constructing the right classpaths by calling `mvn` if pom.xml files are present. This affects the JUnit test runners, the REPL shell, the compiler and type-checker configurations, the setup for terminals in Eclipse projects.
* added functions for inspecting current memory availability and usage
* fixed bugs around prefix matching on concrete trees, simplified its implementation by removing code clones.
* fixed serious bug with prefix shared nullable rules in the parser implementation. 
* fixed race conditions around parser generation for asynchronous usage by the language server protocol
* backtracking over `<==>` and `==>` and `<==` boolean operators is now limited (as limited as backtracking over the `!` boolean operator) 
* case insensitive literals in grammars, such as `'if' Exp 'then' Stat;` are supported better now, also as follow/precede restrictions and keyword reservations (like `Id !>> 'if'`, `Id \ 'if'`) for which an implementation was missing are now supported.
* many regression tests were added as side-effect of the development on the Rascal compiler in the rascal-core project.
* added file watch feature in `IO` module
* added the `lib://libName` scheme for use in `Required-Libraries` in `META-INF/RASCAL.MF` and other places (pathConfig). It resolves to open `project://libName/target/classes` locations or otherwise the jar file that the library is released in and on the classpath.
* added features for recursive copy and recursive move in `IO`
* added lenient option to the JSON parser in `lang::json::IO`
* added utility functions to `util::SystemExec` for easy process creation and termination.
* parse tree filter functions are not called implicitly anymore by the generated parser when building up the tree. Instead a `filter` function is passed to the generated `parse` function where the user can provide filter functions by composing them with `+`. This removes the need for the generated parser function to learn from the current calling scope which functions are available for filtering, and makes the entire parser generator independent of the internals of the interpreter. The new `parser` generator functions work both in the compiled and the interpreted context. Of course, under the hood the implementation reuses the interpreter, until we have bootstrapped the parser generator using the new compiler.
* all Rascal REPLS now support HTML output next to normal string output. Each REPL also serves as a web application host. When a value of type `Content` is the result of evaluation, then the REPL hosts that (Response (Request)) callback until 30 minutes of inactivity. The UI context of the REPL decides how/when and where to show the client side.
* many fixes around higher-order and type-parametrized functions
* moved from the interpreter-specific ICalleableValue to the more abstract IFunction interface to run-time function values.
* rewrote all builtin functions in the standard library to become independent of the interpreter's internals. This is done via constructor arguments of the `@javaClass` implementation classes. These can receive `TypeStore`, `PrintStream` and `IValueFactory` parameters to configure themselves with necessary context of the Rascal runtime.
* `@reflect` is not used anymore by standard library modules. It is deprecated but still supported for now. If you want to make your code robust against compilation by the Rascal compiler, you have to rewrite uses of `@reflect` and receive arguments in your `@javaClass` class constructor.
* All Rascal REPLS print the current release version of the Rascal run-time before anything else.
* Added SourceLocationClassLoader which can construct a JVM classloader instance from references encoded as source location URI. It tries to group file URIs to jar files and forward to URLClassLoader for efficiency's sake. Only ISourceLocationInputResolver schemes which also implement IClassLoaderResolver are supported. We added implementations for all the standard resolvers on the commandline and the necessary ones in the Eclipse context.
* All static errors produced by the new typechecker were fixed in the standard library
* Cleaned up error messages and exceptions in standard library modules (issues largely detected by the typechecker)
* REPLs refactored for use within Bacata (http://github.com/cwi-swat/bacata)

## Release 0.17.0 - March 16th, 2020

The 0.15.x releases served as daily pre-releases for 0.17.0, while 0.16.x was a stable release which included all patches since 0.10.0. The releases between 0.10.0 and 0.16.0 have mainly been concerned with bootstrapping and minor bugfixes.

The current release 0.17.x is a step towards bootstrapping the Rascal compiler and making the new static checker (type checker) available. 

The static checker:

* checks all features of Rascal
* is written in Rascal using the [TypePal framework](https://github.com/usethesource/typepal)
* is activated on "project clean" and "file save"
* runs for now in the Rascal interpreter and may be slow on bigger projects (this is transitional, so bear with us)
* enables editor features such as hover-help and jump-to-definition in Eclipse, based on the state of the file after the last "save"
* using the Eclipse Preferences: Eclipse > Preferences > Rascal > Enable Rascal Compiler, type checking can be turned on or off

Also in this release significant steps have been made towards Rascal project deployment:

* In RASCAL.MF, Use `Required-Libraries: |lib://myDependency|` to declare projects you depend on
* Declare a project using `Project-Name: projectName` in RASCAL.MF
* The `|lib://name|` scheme always resolves to the root of a jar or target folder for a project with that name
* Eclipse, maven and Junit use the above information to configure interpreter and compiler search paths correctly
* The starting point is the JVM classpath: every RASCAL.MF file found on this path leads to a loadable library
* IDEs such as Eclipse dynamically resolve `lib://` references to respective project's target folders if so required

Other notable changes:

* all static errors and warnings in the standard library have been resolved
* general code cleanup
* resolved many issues (thanks all for reporting!)
* various performance improvements

# Releases 0.9.x

In this post we report on the Rascal release 0.9.0, which includes all changes since the 0.7.x releases. We have not written release notes for the 0.8.x series (the details are included here). We expect a number of patch releases for 0.9.x, which we report on by updating this post (at its end) when necessary until we move on to 0.10.x. 

The Rascal release 0.9.x includes the following main components:

* Parser and interpreter: parsing and running Rascal programs
* Parser generator: generating parsers for programming languages and domain specific languages
* Eclipse plugin: an IDE for Rascal, partially supported by the new type checker and compiler
* Eclipse plugin generator for DSLs: an IDE generator based on Rascal programs
* Rascal commandline shell: to run Rascal independently from Eclipse
* Standard library: utilities for programming in Rascal, including several (stable) programming language front-ends and general analysis facilities.
* Rascal compiler: the experimental compiler for Rascal
* Rascal type checker: the experimental type checker for Rascal
* TypePal: experimental generic name and type analysis framework
* Salix: a html5-based GUI interaction framework for Rascal
* Shapes: a html5 back-end for the Figure library
* Capsule: the hash-trie set, map and multimap Java library supporting the implementation of Rascal's maps, sets and relations
* Clair: a C++ analysis framework based on the CDT parser
* Split main Rascal Eclipse plugin release from a number of additional libraries, located at a new update site: <https://update.rascal-mpl.org/libs/>
 
The 0.9 release is a landmark release, including a big number of bug fixes and mainly preparations towards releasing a compiled version of Rascal. 

* The most notable new features are:
   1. Rascal type checker
   2. Experimental Rascal compiler
   3. Faster and leaner implementations of maps and relations under-the-hood, based on hash-tries (unfinished)
   4. Fully lazy and streaming string templates
   5. TypePal: a powerful generic type and name analysis framework
   6. Re-implementation of the M3 java Jar extractor
   7. Very fast streamed binary value (de)serialization
   8. Full re-implementation of the tutor documentation generator based on asciidoctor (unfinished)
   9. Typed import/export of JSON data
   10. New "common keyword parameters" support; `data X(int x = 0)` where the field `x` with default `0` is made available to all constructors of the data-type `X` in the current scope.
   11. Calling compiled Rascal functions from Java code using simple Java interface proxy. 
   12. M3 models now use keyword fields instead of annotations
  
* Temporarily disabled features:
   1. syntax highlighting of concrete syntax fragments in the Eclipse Rascal editor is currently turned off
 
* Deprecated features:
   1. annotations as in `anno int X@name;` and their usage `x()@name` is deprecated. Please start using keyword parameters as in: `data X(str name = "")` and `x.name`
   2. asType: `[Type] "string"` in patterns and expressions. Please use the `parse` function for now.


* Other things that were added, improved or fixed:
   1. subscript on lrels added
   2. many, many tests added
   3. refactoring of the REPL api to enable reuse in notebook implementations (see Bacata)
   4. windows compatibility improved
   5. Pattern matchin a bound variable now ignores keyword fields of constructors unless explicitly mentioned
   5. steps towards less dependency on the annotation feature
   6. several bugs around the annotion feature and map indexing fixed
   7. faster field access in the interpreter
   8. compiler is automatically and continuously bootstrapped and validated (no binary code needs committing in the repository)
   9. rationalization and consistency improvement of all the input and output parameters of the compiler and the interpreter via the PathConfig data-type. 
   10. quickcheck random value generator also simplified and made consistent and reusable
   11. clear separation of REPL functionalities and reuse of Eclipse tm.terminal 
   12. clarified and fixed UTF8 encoding details in I/O functions
   13. clarified and fixed URI encoding details in I/O functions
   14. optimized reading from (nested) jar files
   15. much improved efficiency of function calling by the Rascal compiler
   16. additional API for managing external processes
   17. compiler and type-checker support incremental compilation
   18. much faster comparison of source locations
   19. rename of rascal.values (used to be pdb.values) to independent project called "vallang"
   20. support for Java NIO File Channels for (some of the) location schemes.
   21. modular re-implementation of type reification
   22. new modular subsystem to generate classloader instances from URI source locations, e.g. `|system:///|`, `|classpath:///|` and `|file:///path/to/jarFile.jar|` and `|file:///path/to/classFolder|`
   23. interpreter and compiler made maximally code-independent
   24. all projects fully "mavenized"
   25. advanced interactive debugging features of compiled Rascal
   26. REPL and IDE features based on compiled Rascal
   27. integrated compiled Rascal modules as JUnit test runners. 
   28. several fixes in datetime behavior
   29. full implementation of JVM bytecode generation by compiler
   30. parsing ambiguity is now by default an "error" (an exception is raised by the parse function). Using the `allowAmbiguity=true` flag an ambiguous parse forest can still be produced.
   31. added syntax highlighting of parsed source code fragments in the REPL
   32. a `jar+<scheme>:///path/to/jar!/path/in/jar` scheme supports files in jars.
    
### Contributors to the 0.8.x and 0.9.x releases

Thanks! In no particular order the following people have contributed to the 0.9.x releases of Rascal and its supporting libraries or the libraries it supports:

* Paul Klint
* Davy Landman
* Bert Lisser
* Michael Steindorfer
* Mark Hills
* Jurgen Vinju
* Ferry Rietveld
* Tijs van der Storm
* Thomas Degueule
* Lina Maria Ochoa Venegas
* Mauricio Verano
* Rodin Aarssen
* Anya Helene Bagge
* Tim Soethout
* Aiko Yamashita
* Nick Lodewijks
* Jouke Stoel
* Rodrigo Bonifacio
* Yoan-Alexander Grigorov
* Vadim Zaytsev
* Mats Stijlaart
* Magiel Bruntink
* Kevin van der Vlist

Also thanks to all the people who have (clearly) reported bugs and provided smaller pull requests! Your help is much appreciated. 

### Patch releases

* Patch 0.9.1. bootstrap release to introduce new pattern match semantics for `var1 := var2`
* Patch 0.9.2. bootstrap release to fix version number reporting in generated kernels

## Release 0.27.2 - November 23, 2022

* the tutor compiler now takes screenshots if an interactive visual is generated in a rascal-shell code block.
* the JSON serializer maps objects to constructors one-to-one now. Only [AlgebraicDataTypes](/docs/rascalopedia/algebraicdatatype/) with single constructors are allowed, or lists of nullary constructors (for enums).
* added vis::Chart with 8 basic chart styles based on chart.js
* added util::Validator which can validate any `node` instances against an [AlgebraicDataType](/docs/rascalopedia/algebraicdatatype/) using matching and memoized backtracking. Useful for reading in complex XML, JSON or YAML data.
* issue with variablescope leakage in the visit statement was fixed.

## Release 0.7.0

In this post we report on the Rascal release 0.7.0. We expect a number of patch releases as well, which we report on by updating this post (at its end) when necessary. The Rascal release includes the following main components:

* Parser and interpreter: parsing and running Rascal programs
* Parser generator: generating parsers for programming languages and domain specific languages
* Eclipse plugin: an IDE for Rascal
* Eclipse plugin generator for DSLs: an IDE generator based on Rascal programs
* Rascal commandline shell: to run Rascal independently from Eclipse
* Standard library: utilities for programming in Rascal, including several (stable) programming language front-ends and general analysis facilities.

Compared to 0.6.x the 0.7 release is mainly a bug fix release, with a number of extensions to the libraries and one new language feature. 

* The new language feature is keyword parameters for both functions and data constructors. 
* In terms of library funcionality the big new things is *M3*, a common intermediate format for facts about source code. This includes a standard set of relations (containment, def/use, etc.) and a preferred way of modeling abstract syntax trees.

### M3

### Keyword parameters

### Looking forward to the 0.8.x, 0.9.x and 1.0 releases

The 0.8 release is planned with:

* Re-implemented general top-down parsing algorithm with on-demand lexing
* Fast regular expressions and better language integrated pattern matching
* Concrete syntax features completed and streamlined due to new parser integration
* Standard library component for communicating with SMT solvers easily

The 0.9 release is planned with:

* Keyword parameters replacing annotations completely
* New Figure library based on javascript

The 1.0 release is a big bang release which includes the following new components which have been developed over the last two years:

* The Rascal type checker
* Rascal compiler to RVM
* RVM interpreter

In 1.0 the old Rascal interpreter will still be included, but from here on its usage will be deprecated. We will be working to switch the IDE support to using the new infra-structure for a while and when this is finished the interpreter will not be released any longer. Note that this does not mean that we will not have a REPL for Rascal anymore. We will always have a REPL.

### Patch releases

* Patch 0.7.1. includes an update to the documentation on M3
* Patch 0.7.2. bug fixes
* Patch 0.7.3. bug fixes and memory optimizations (more than 50%)

