# Utilities

.Synopsis
Some utility functions.

.Description

TypePal provides some utility functions to address common scenarios.

== collectAndSolve

[source,rascal]
----
TModel collectAndSolve(Tree pt, TypePalConfig config = tconfig(), bool debug = false)
----
`collectAndSolve` implements the most simple type checking scenario without any customization.
For a given parse tree `pt`:

- Create a Collector and use it to create a TModel by applying `collect` to parse tree `pt`.
- Create a Solver and solve the constraints in that TModel.
- Return the extended TModel.


== getUseDef
[source,rascal]
----
rel[loc, loc] getUseDef(TModel tm)
----

Get all use-def relations in a given TModel. This may be used in an IDE for creating hyperlinks between use locations and definitions.
 

== getVocabulary
[source,rascal]
----
set[str] getVocabulary(TModel tm)
----
Get all defined names in a given TModel. This may be used in an IDE for text completion.

== getFacts
[source,rascal]
----
map[loc, AType] getFacts(TModel tm)
----
Get all the locations and their type in a given TModel.

== getMessages
[source,rascal]
----
list[Message] getMessages(TModel tm)
----
Get all the messages in a TModel (as added by the Solver).
