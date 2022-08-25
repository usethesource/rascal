# Concepts and Definitions

.Synopsis
The concepts and definitions used in TypePal.

== Identifier
The syntax of a source language may impose various restrictions on the identifiers 
that can occur in a program. They amount to including or excluding specific characters 
for various occurrences of names in the program. One example is the requirement in Java that class names
start with an upper case letter. TypePal is agnostic of such conventions and represents 
each name as a string. _Qualified names_ are also supported and are represented by a list of strings.

== Tree

The Rascal data type `Tree` (REF) is used to represent all parse trees that can be generated for any syntax described in Rascal.
`Tree` is also a super type of any syntactic construct that may occur in a parse tree. 
In TypePal we interchangeably use `Tree` and the source area (a source location) associated with it to uniquely 
identify program parts, definitions, uses and scopes.

== Scope
A _scope_ is a region of a program that delimits where definitions of identifier are applicable.
An identifier is defined in the scope where it is defined and in all nested subscopes, unless one of these subscopes
redefines that same identifier. In that case, the inner definition applies inside that nested scope (and its subscopes).
Scopes are identified by the subtree of the parse tree that introduces them such as, for instance, a module, a function declaration or a block.
Special rules may apply such as _define-before-use_ or _scopes-with-holes_.

== Scope Graph
The scope graph is one of the the oldest methods to describe the scope of names in a program.
We use a version of scope graphs as described by Kastens & Waite, _Name analysis for modern languages: a general solution_, SP&E, 2017.
This model uses text ranges in the source text (happily represented by Rascal's `loc` data type) to identify 
and define all aspects of names. 
A scope graph provides lookup operations on names that take both syntactic nesting and semantic linking (via _paths_) into account,
as well as the specific roles of identifiers and paths (described below).

== Identifier definition
The _definition_ of an identifier is inside TypePal characterized by a `Define`:
[source,rascal]
----
alias Define  = tuple[loc scope, str id, IdRole idRole, loc defined, DefInfo defInfo];
----
where

* `scope` is the scope in which the definition occurs;
* `id` is the text representation of the identifier;
* `idRole` is the role in which the identifier is defined, see <<Identifier Role>>;
* `defined` is source code area of the definition;
* `defInfo` is any additional information associated with this definition, see ((DefInfo)),

== Identifier Use
The _use_ of an identifier is characterized by a `Use`:

[source,rascal]
----
data Use
    = use(str id, loc occ, loc scope, set[IdRole] idRoles)
    | useq(list[str] ids, loc occ, loc scope, set[IdRole] idRoles, set[IdRole] qualifierRoles)
    ;
----
where `use` represents the use of a simple name and `useq` that of a qualified name.
In the latter case, a list of strings is given; the last string is a simple name in given `idRoles` and the preceeding strings are its qualifiers in `qualifierRoles`.

== Path
TypePal is based on scope graphs that are not only based on syntactic containment of scopes but can also express semantic 
connections between parse trees.
While scopes are strictly determined by the hierarchical structure of a program (i.e., its parse tree),
_paths_ provide an escape from this restriction and define a semantic connection between syntactic 
entities that are not hierarchically related and may even be part of different syntax trees.
Connections between syntactic entities are paths labelled with user-defined roles.
Paths are represented by the Rascal datatype `PathRole`.
An example is the import of a module _M_ into another module _N_ that makes the entities in _M_ known inside _N_.
Here is an example of a path role to mark an import path between two parse trees.

[source,rascal]
----
data PathRole
    = importPath()
    ;
----
Paths are, amongst others, used in the resolution of qualified names.

== Name Resolution
Name resolution is based on the principle: __syntactic resolution first, semantic resolution second__.
This means that we first search for a definition in the current parse tree and only when that fails 
we follow semantic path to other trees (either in the current tree or in other trees):

* First the current scope in which the name is used is searched for a definition.
* If this fails surrounding scopes are searched.
* If this fails semantic paths in the same parse tree or to other parse trees are searched, such as, for instance, 
  provided by an import statement.

This is illustrated below, where a name occurrence _O_ 
can be resolved to definitions _D1_ (syntactic resolution), _D2_ (semantic resolution) and/or _D3_ (semantic resolution).

![]((NameResolution.png))

IMPORTANT: Name resolution need not have a unique solution. 
Therefore the author of a TypePal-based type checker can provide functions to 
(a) filter valid solutions; (b) determine which identifiers may be overloaded.

== Role

Identifiers, scopes and path can play different _roles_ that determine how they will be handled.
They are represented by various Rascal datatypes that can be extended by the author of a typechecker.

=== Identifier Role

Identifier roles are modelled by the data type `IdRole`.
Here is an example where roles are introduced for constants, variables, formal parameters and functions:

[source,rascal]
----
data IdRole
    = constantId()
    | variableId()
    | formalId()
    | functionId()
    ;
----

When _defining_ an identifier, the specific role of that identifier has to be given, e.g. as `constantId()`.
When _using_ an identifier, the set of acceptables roles has to be given. For instance, an identifier
used in an expression may accept the roles `{ constantId(), variableId(), formalId() }`.

=== Scope Role

Scope roles are modelled by the data type `ScopeRole` and are used to distinguish different kinds of scopes.
Later (REF) we will see that this can be used, for instance, to search for the innermost scope with a specific role,
say the innermost function scope. Here is an example that introduces scopes for functions and loops:

[source,rascal]
----
data ScopeRole
    = functionScope()
    | loopScope()
    ;
----

=== Path Role
Path roles are modelled by the data type `PathRole`:

[source,rascal]
----
data PathRole
    = importPath()
    | extendPath()
    ;
----

== Types
The type to be associated with names varies widely for different programming languages and has to be provided by the typechecker author.
TypePal provides the data type `AType` that provides already some built-in constructors:

[source,rascal]
----
data AType
   = tvar(loc tname)                                    //<1>
   | atypeList(list[AType] atypes)                      //<2>
   | overloadedAType(rel[loc, IdRole, AType] overloads) //<3>
   | lazyLub(list[AType] atypes)                        //<4>
   ;
----

<1> `tvar` represents a type variable (used for type inference) and is only used internally. 
<2> `atypeList` represents a list of `AType`s and is used both internally in TypePal but can also be used in typechecker definitions.
<3> `overloadedAType` represents overloaded types.
<4>  `lazyLub` represents a lazily computed LUB of a list of types.

AType that has to be extended with language-specific types.

The typechecker author also has to provide a function to convert `AType` to string (it is used create readable error messages):
[source,rascal]
----
str prettyAType(AType atype);
----

== DefInfo

When defining a name, we usually want to associate information with it such as the type of the defined name.
TypePal provides the data type `DefInfo` for this purpose:

[source,rascal]
----
data DefInfo
    = noDefInfo()                                                                           //<1>
    | defType(value contrib)                                                                //<2>
    | defTypeCall(list[loc] dependsOn, AType(Solver s) getAType)                            //<3>
    | defTypeLub(list[loc] dependsOn, list[loc] defines, list[AType(Solver s)] getATypes)   //<4>
    ;
----
<1> No information associated with this definition.
<2> Explicitly given AType contribution associated with this definition. `contrib` can either be an `AType`, or a `Tree`. 
    In the latter case, the type of that tree is used (when it becomes available) for the current definition.
<3> Type of this definition depends on the type of the entities given in `dependsOn`, when those are known, 
    `getAType` can construct the type of this definition. 
    `getAType` will only be called by TypePal during constraint solving.
<4> Refine a set of definitions by taking their LUB, mostly used for local type inference.

The ((Solver)) argument of `getAType` and `getATypes` is the current constraint solver being used.

WARNING: noDefInfo may be removed.
