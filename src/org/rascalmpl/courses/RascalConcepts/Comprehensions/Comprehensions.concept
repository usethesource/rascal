# Comprehensions

.Synopsis
Comprehensions for generating values.

.Syntax

.Types

.Function

.Details

.Description
Comprehensions are a notation inspired by mathematical http://en.wikipedia.org/wiki/Set-builder_notation[set-builder notation]
and http://en.wikipedia.org/wiki/List_comprehension[list comprehensions]
that help to write succinct definitions of lists and sets. They are also inspired by queries as found in a language like SQL.

Rascal generalizes comprehensions in various ways. Comprehensions exist for lists, sets and maps. 
A comprehension consists of an expression that determines the successive elements to be included in the 
result and a list of enumerators and tests (boolean expressions). 
The enumerators produce values and the tests filter them. 

See link:/Rascal#Expressions-Comprehensions[Comprehensions], 
link:/Rascal#List-Comprehension[List Comprehension], 
link:/Rascal#Set-Comprehension[Set Comprehension], and
link:/Rascal#Map-Comprehension[Map Comprehension] for details.

.Examples
A standard example is

[source,rascal-shell]
----
{ x * x | int x <- [1 .. 10], x % 3 == 0 }
----
i.e., the squares of the integers in the range `[ 1 .. 10 ]` that 
are divisible by 3. A more intriguing example (that we do not give in full detail) is

[source,rascal]
----
{name | /asgStat(Id name, _) <- P}
----
which traverses program `P` (using the _descendant match_ operator `/`, see link:/Rascal#Patterns-Abstract[Patterns]) 
and constructs a set of all identifiers that occur on the left hand 
side of assignment statements in `P`.

.Benefits

.Pitfalls

