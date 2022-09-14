---
title: Object-Oriented and Imperative Languages
---

.Synopsis
Rascal explained for OO and imperative programmers.

.Syntax

.Types

.Function

.Description

Rascal is an imperative domain-specific language (DSL) for meta-programming.
It provides high level statements and expressions specifically targeted at the domain of analysis and transformation of source code.

How does Rascal differ from an OO language or an imperative language?

*  Unlike OO languages, Rascal does not provide classes. Rascal has modules which can best be compared with a static class in Java.

*  Objects in an OO language (class instances) can have mutable local state (i.e, each object can have instance variables 
  that can be modified after object creation). In Rascal all values are immutable after creation. Sharing a value does 
  not introduce a coupling like in OO, simply because changes are only visible to the code that changes the values.
  Without mutability it is easy to combine stages of programs that perform different tasks.

*  Rascal does provide a mechanism for introducing user-defined types. 
  An _Algebraic Data Type_ introduces a new type and is defined by a number of _constructor functions_ to construct 
  values of that type. The constructed values are immutable. Although they are both called "constructors"
  there is a big difference between constructors in OO languages and in Rascal. The former create mutable objects while the
  latter create immutable values.

*  Variables can, however, be associated with different immutable values during their lifetime. This is why we say that Rascal is
a weakly imperative language. Rascal is, however, closer, to functional languages than to OO languages or imperative languages.

*  Rascal is safe: there are no null values.

*  Rascal is even more safe: it has a type system that prevents casting exceptions and other run-time failures. Still the type system specifically allows many kinds of combinations. For example, unlike in Java a set of integers is a subtype of a set of numbers (co-variance), which allows you to reuse algorithm for sets of numbers on sets of integers. It also provides true polymorphic and functions (no erasure), and functions can safely be parameters to other functions.

*  Rascal supports unchecked exceptions, throw and catch statements are available, but exceptions do not _have_ to be declared in function headers (but they _may_ be declared for documentation purposes).

*  Rascal provides high-level statements and expressions for:
** 	Visitors in all kinds of orders, expressed very concisely, and type safe.
** 	Pattern matching and construction (with and without concrete syntax!).
** 	Equation/constraint solving.
** 	Relational calculus.
** 	Rewrite rules for normalization/canonicalization of any kind of data-structure
**     Support for parser generation and parsing using context-free grammars.
** 	(De)Serialization of values.
** 	Communication with databases.

*  Rascal provides typed data constructors for common mathematical structures, such as:
** 	Terms (a.k.a. tree nodes or abstract data types).
**     Parse trees (derivations of context-free grammars, for concrete syntax and direct manipulation of source code).
**     Lists, tuples, maps, sets, relations, and graphs.

*  In Rascal you can implement high-fidelity source-to-source transformations. Without too much overhead, programs can do extensive rewriting of the source code without the loss of particular layout standards or source code comments.

*  Rascal is syntax-safe. When you use Rascal to generate or transform source code, it statically detects whether the resulting source code is syntactically correct.

*  Rascal is executed by an interpreter written in Java, or it can be compiled to Java classes.

.Examples

.Benefits

.Pitfalls

