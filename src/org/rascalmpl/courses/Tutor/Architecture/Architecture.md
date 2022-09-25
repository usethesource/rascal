---
title: Architecture
---

#### Synopsis

The global architecture of the Rascal Tutor

#### Description

The Rascal Tutor is a Markdown and Rascal source file pre-processor. As input it takes [Docusaurus Markdown](https://docusaurus.io/docs/markdown-features/) files organized in hierarchical folders, and Rascal modules organized in hierarchical packages. One root folder is called a "course". As output the pre-processor produces per course 
a folder hierarchy again, where each folder has its own `index.md` file (generated or written). After the tutor
compiler has generated such a consistent folder of interconnected markdown files, other downstream processors can turn them into (static) html websites, pdf files or otherwise. The standard way of processing is to use the [Docusaurus](https://docusaurus.io) static website generator.

The important features of the pre-processor, "compiler", are:
1. The compiler is configured via ((util::Reflective::PathConfig)), where:
   * each entry in the `srcs` list is a single _course_
   * each entry in the `libs` list, be it a jar file or not, is searched for an `index.value` file to augment the current index. 
1. Concept hierarchy - each folder `/X` has its own index file, called either `X/X.md` or `X/index.md`. Nested folders equal nested concepts.
1. Indexing - to help in easy cross-linking between concepts:
   * Each concept of course `A` stored in `X/Y/Z/Z.md` may be linked via `Z`, `Y-Z`, `X-Y-Z`, `A:Z`, `A:Y-Z`, `A:X-Y-Z`
   * Each Rascal module of course `A` named `a::b::C` may be linked via `C`, `b-C`, `a::b::C`, `module:a::b::C`, `A:C`, `A:b-C`, `A:a::b::C`, `A:module:a::b::C`
   * Each Rascal package of course `A` named `a::b` may be linked via `b`, `a::b`, `package:a::b`, `A:b`, `A:a::b`, `A:module:a::b`
   * Each image `x.{png,svg,jpg,jpeg}` stored in `X/Y/Z` may be linked as though it were a concept file.
   * The index is a `rel[str,str]` from the above described links to the `index.md` files in the generated hierarchy.
   * The compiler reports missing links and ambiguous links as errors.
   * A single `index.value` file is written in the output folder for future reference by depending projects.
1. Code execution ensures lively demonstrations which are checked for correctness. 
   * Code blocks marked `rascal-shell` are executed line-by-line on the ((RascalShell)) prompt. Each prompt starts with a fresh environment. All error and standard output is captured and printed back. ((Library:module:Content)) that serves HTML or any other file is also inlined in the output Markdown file.
   * Code blocks marked `rascal-shell,continue` are executed in the previously constructed environment (from top to bottom in the Markdown file) and behave the same otherwise.
   * Code blocks marked `rascal-prepare` with or without `continue` behave the same as above, except that no output or input is printed back into the output file.
1. Links written between `((` and `))` are resolved using the previously described index. 
1. Table of contents are generated for the word TOC between three `(((` brackets `)))`; the content of each concept is the concept nested under it in the hierarchy.
1. Each Rascal file is indexed to list the declarations it contains and information is extracted:
   * data declarations with their `doc`, `synopsis`, `examples` etc. tags
   * (overloaded) function declarations with their `doc`, `synopsis`, `examples` etc. tags
   * alias declarations with their `doc`, `synopsis`, `examples` etc. tags

Features on the TODO list are:
1. Interactive Questions (have to be revived)
1. Screenshots of interactive ((Library:module:Content) visualizations

To implement these featues the following components are relevant:
* `lang::rascal::tutor::Indexer` implements indexing and lookup
* `lang::rascal::tutor::Compiler` implements linking, code execution, toc, and the generation of index files.
* `lang::rascal::tutor::apidoc::ExtractInfo` parses Rascal code and generates an intermediate overview
* `lang::rascal::tutor::apidoc::GenerateMarkdown` generates a single Markdown file for each Rascal module, and also reuses `Compiler` to process embedded markdown with code examples, links, etc.
* `lang::rascal::tutor::repl::TutorCommandExecutor` encapsulates a Rascal REPL as a set of closures (`eval`, `reset`, and `prompt`) 
   * for efficiency's sake a single executor is shared among all Markdown and Rascal files that are compiled in a single run. Between every file the executor is reset for a new environment, but previously loaded modules remain in the heap available for reuse. 

#### Benefits

* Index creation is modular: an index is created per course and these indices are combined at runtime.
* Code execution is reasonably time efficient, although it may require quite some memory because eventually an entire course will be loaded.

#### Pitfalls

* Courses are compiled on a per-course basis. Support for incremental courses compilation is not yet available.
* Badly balanced code block quotes are not detected by this pre-processor but will fail HTML compilation downstream.
* Manual links are not checked at compile-time by this pre-processor but fill fail HTML compilation downstream.
