Thanks for your interest in the Rascal MPL project!

Rascal is a (meta) programming language with the following features:

1. type-checker, 
2. interpreter that type-checks, 
3. compiler
4. standard library
4. parser generator
4. top-down context-free general parsing algorithm
4. hash-trie based implementation of relational calculus (via [capsule](https://github.com/usethesource/capsule))
4. immutable values (sets, lists, numbers, maps, algebraic data-types), via [vallang](https://github.com/usethesource/vallang)
5. REPL
6. Eclipse IDE with IDE generator based on Eclipse IMP (see https://github.com/usethesource/rascal-eclipse)
7. VScode IDE with IDE generator based on LSP (see https://github.com/cwi-swat/rascal-vscode)
8. Interactive documentation (tutor) compiler
8. Interactive documentation content
8. Libraries for
   * Java analysis
   * C++ analysis
   * PHP analysis
   * Python analysis
   * Javascript analysis
   * HTML5-based UI implementations (salix library)
   * Git analysis
   * etc.

 
Contributing to libraries or the standard library is preferred over contributions to the core implementation features since
features in the core are tangled and therefore complex. We do have a lot of tests, so if you plan to contribute a bug-fix or
a new feature don't forget to run `mvn test`. If you are a parser, type-checking, or functional programming/term-rewriting expert, 
please do not hesitate to give us feedback or suggestions, we love those topics and it's always good to talk to a fellow 
enthusiast and learn something new.

If you'd like to contribute a new library or library function do not hesitate to contact us: open an enhancement or bug issue 
[here](https://github.com/usethesource/rascal/issues) and we can start discussing the ins and outs of your idea! Also
we'd be able to help out; you don't have to do it all alone.

