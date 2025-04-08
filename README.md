# Rascal - Metaprogramming Language
[![Build and Deploy](https://github.com/usethesource/rascal/actions/workflows/build.yaml/badge.svg)](https://github.com/usethesource/rascal/actions/workflows/build.yaml)

This is the core implementation of the Rascal meta-programming language. It contains the interpreter, the parser generator, the parser run-time,
the (documented) standard library, the type checker, the compiler, and the documentation compiler ("tutor").

Other relevant repositories:

* https://github.com/usethesource/rascal-language-servers - for the language service protocol implementations for Rascal and DSLs written in Rascal
* https://github.com/usethesource/rascal-maven-plugin - provides pom-configurable _mvn_ goals for rascal type checking (`mvn rascal:compile`), documentation compilation (tutor) (`mvn rascal:tutor`), running code generators written in Rascal (`mvn rascal:generate-sources`) and starting the Rascal console REPL (`mvn rascal:console`). Dependencies of the pom are interpreter as Rascal library dependencies.
* https://github.com/usethesource/rascal-website - for the online documentation
* https://github.com/usethesource/vallang - for the core of the Rascal runtime values, binary and textual IO, and the core of the dynamic type system.

Please visit http://www.rascal-mpl.org for all information regarding Rascal.

For questions use the [rascal tag on StackOverflow](http://stackoverflow.com/questions/tagged/rascal).
