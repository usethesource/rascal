---
title: Working on the Interpreter
---

#### Synopsis

Things to know when working on the interpreter

#### Description

* The Rascal interpreter resides here: <http://github.com/usethesource/rascal>
* The main design patterns used are:
   * The big encapsulator class is `Evaluator`, it wraps all of the following.
   * Parser generation - the Rascal parser is bootstrapped in Rascal
   * AST generation - the AST Java classes for Rascal are generated from the grammar
      * in `org.rascalmpl.ast` we find the generated classes that use the "Generation Gap" pattern
      * in `org.rascalmpl.semantics.dynamic` we find all the hand-written sub-classes of the generated AST classes that hold the "interpreter" methods (see below) that give semantics to the AST nodes.
      * `ASTBuilder` uses reflection to build an AST instance from a ((Library:ParseTree)) instance. If a corresponding class exists in `semantics.dynamic` then we instantiate it, otherwise we instantiate a clean AST node.
   * Concrete syntax is implemented using simple reflection on the grammar that is in scope, then generating a parser and parsing all the fragments in it. Parsed fragments are mapped to Patterns or Expressions to get semantics.
   * The "Interpreter" pattern is used for:
      * type and name analysis `getType()`
      * interpretation `interpret()`
      * pattern matching generation `buildMatcher()`
   * State is modelled via the `Environment` class
      * `ModuleEnvironment` is an extension which can store more kinds of declarations
      * Closures capture `Environment` instances.
      * the heap is a map from module names to `ModuleEnvironment` instances
   * Concrete/Abstract interpretation:
      * every computation on values is done once on the actual value and once on a type abstraction of that value
      * the abstract interpreter _simulates_ a static type system
      * sometimes dispatch is done based on the static type of a value
      * sometimes dispatch is done based on the dynamic type of a value
      * the `Result` class hierarchy uses "Double Dispatch" to select the right type based on the left-hand side and right-hand side of operators. For every kind of data value there is a corresponding `Result` class, e.g. `ListResult` for lists and `RascalFunction` for functions.
   * Pattern Matching
      * For every kind of pattern there is a corresponding `Matcher` class
      * Every matcher binds variables in its own local `Environment`
   * Structured Programming
      * jumps are implemented using exceptions
      * backtracking is implemented with local loops
* The interpreter is tested with a big collection of regression/unit tests in `lang::rascal::tests`
* A single `Evaluator` is independent of other Evaluator instances provided it does not share `Environment`s.
* `Evaluator` is _not_ thread-safe. It is thread-friendly in the sense that one Evaluator can run in its own Thread without hampering other Evaluators.