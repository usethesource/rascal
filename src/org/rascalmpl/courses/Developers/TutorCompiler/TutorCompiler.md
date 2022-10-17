---
title: Working on the Tutor Compiler
---

#### Synopsis

How to compile, run and test the tutor compiler.

#### Description

The tutor compiler translates Rascal modules and Markdown files to Docusaurus Markdown files. The main features are:

* Flattening and fusing a hierarchical tree of markdown files that each describe a single concept
* Indexing sub-concepts and resolving links to them (internally)
* Implementing local tables of contents for listing nested subconcepts in the parent file
* Collecting and linking local image files
* Supporting subscripts and superscripts as in `Type~1~` and `Type^21^` by translation MDX Text tags.
* Collecting Rascal source modules and the function and data declarations in them to generating API documentations in markdown notation
* Running `rascal-shell` blocks on the Rascal REPL and collecting resulting HTML visualizations as screenshots (unfinished)
* Executing the questions DSL to produce embedded interactive questions (unfinished)

The tutor compiler is located here: [src/org/rascalmpl/library/lang/rascal/tutor](https://github.com/usethesource/rascal/tree/main/src/org/rascalmpl/library/lang/rascal/tutor), with each main feature in a sub-folder. You will find the main compiler file in `Compiler.rsc`. Some of the tutor compiler is written in Java, in particular the interface with the REPL (See `lang/rascal/tutor/repl/TutorCommandExecutor.java`.

#### Examples

* clone the rascal project first: `git clone git@github.com:usethesource/rascal.git`
* compile it, but skip the type-checking of the library: `mvn -Drascal.compile.skip -DskipTests package`
* run can be done in several ways:
   1. use VScode run command to execute `RascalShell` in debug mode
   2. use Eclipse to "Run as Java Program", also `RascalShell`
   4. use `java -jar target/rascal-<version>-SNAPSHOT.jar`

 
Note that it's indeed best to run the rascal REPL as described above, otherwise you might miss fixes in the Java-implemented part of the tutor, or other related changes in the interpreter that needed fixing to build the tutor.


#### Pitfalls

* In `src/org/rascalmpl/library/lang/rascal/tutor` you will find "throwaway" scripts for translating asciidoctor markdown notation to docusaurus markdown notation. Sometimes it requires running the same script twice or three times to see the desired effects. This is because some rules generate the input for other rules to be transformed again.
