
@synopsis{Symbolic representation for types that occur in programming languages.}
@description{
M3 provides a general mechanism to associate types, symbolically, with source code artifacts or even run-time artifacts.

The `TypeSymbol` type is a general concept which needs to be extended for specific programming languages. One language will 
class and interface definitions which coincide with types, another may have higher order function types etc.

As a basic principle, the symbols for declared types always link to their definition in the source code using a location value, while other implicit types do not have such a link (i.e. `int` and `void`).

We cater for languages to have a subtype relation to be defined, and a least upper bound computation. 

You will find an interesting examples in ((lang::java::m3::TypeSymbol)).
}
@benefits{
* symbolic types can be analyzed and manipulated symbolically, i.e. to instatiate parametrized types.
* symbolic types can be used directly as constraint variables for type inference or type-based refactoring purposes.
}
@pitfalls{
* If you import extensions to this M3 model for two different languages, ambiguity and other confusion may arise 
because the subtype and lub functions of the two languages will merge.
* TypeSymbols are sometimes confused with their predecessors in the analysis pipeline: abstract syntax trees of `Type`.
   * If `Type` is the syntax of types, then `TypeSymbol` represents their semantics.
   * Programmers can only type in `Type` instances, while programming languages may also feature types which do not have syntax (such as union an intersection types during inference).
   * Type and TypeSymbol constructors are the same when they are related, i.e. Type::\int() will produce TypeSymbol::\int().
   * Often different ways of writing the same type in syntax, produce a normal and simplified form in TypeSymbol representation.
   * Abstract or generic (parameterized) TypeSymbols can be instantiated during abstract interpretation and other analysis algorithms.
   * Instances of syntactic `Type` always have their `src` location with them, while most TypeSymbols are deduced and inferred and do not
   have a clear source origin.
}
module analysis::m3::TypeSymbol

data TypeSymbol
    = unresolved()
    ;

