# Pattern Matching

.Synopsis
Pattern matching.

.Syntax

.Types

.Function

.Details

.Description

Pattern matching determines whether a given pattern matches a given value. 
The outcome can be `false` (no match) or `true` (a match). A pattern match that succeeds may bind values to variables.

Pattern matching is _the_ mechanism for case distinction 
([Switch]((Rascal:Statements-Switch)) statement) and search ([Visit]((Rascal:Expressions-Visit)) statement) in Rascal. 
Patterns can also be used in an explicit match operator `:=` and can then be part of larger boolean expressions. 
Since a pattern match may have more than one solution, local backtracking over the alternatives of a match is provided. 
Patterns can also be used in [Enumerators]((Rascal:Comprehensions-Enumerator))s and control structures like 
[For]((Rascal:Statements-For)) and [While]((Rascal:Statements-While)) statement.

A very rich pattern language is provided that includes string matching based on regular expressions, 
matching of abstract patterns, and matching of concrete syntax patterns. 
Some of the features that are provided are list (associative) matching, 
set (associative, commutative, idempotent) matching, and deep matching of descendant patterns. 
All these forms of matching can be used in a single pattern and can be nested. 
Patterns may contain variables that are bound when the match is successful. 
Anonymous (don't care) positions are indicated by the underscore (`_`). 
See [Patterns]((Rascal:Rascal-Patterns)) for more details.

.Examples

Here is a _regular expression_ that matches a line of text, finds the first alphanumeric word in it, and extracts the word itself as well as the before and after it (`\W` matches all non-word characters; `\w` matches all word characters):

[source,rascal]
----
/^<before:\W*><word:\w+><after:.*$>/
----

Regular expressions follow the Java regular expression syntax with one exception: instead of using numbered groups to refer to parts of the subject string that have been matched by a part of the regular expression we use the notation:

[source,rascal,subs="quotes"]
----
<_Name_:_RegularExpression_>
----

If `_RegularExpression_` matches, the matched substring is assigned to string variable `_Name_`.

The following abstract pattern matches the abstract syntax of a while statement defined earlier:

[source,rascal]
----
whileStat(EXP Exp, list[STAT] Stats)
----

Variables in a pattern are either explicitly declared in the pattern itself---as done in the example---or they may be declared in the context in which the pattern occurs. So-called multi-variables in list and set patterns are declared by a `*` suffix: `X*` is thus 
an abbreviation for `list[...] X` or `set[...] X`, where the precise element type depends on the context. The above pattern can then be written as

[source,rascal]
----
whileStat(EXP Exp, Stats*)
----
or, if you are not interested in the actual value of the statements as

[source,rascal]
----
whileStat(EXP Exp, _*)
----

When there is a grammar for this example language, we can also write concrete patterns as described in
[Concrete Patterns]((Rascal:Patterns-Concrete)).



.Benefits

.Pitfalls

