# Patterns

.Synopsis
Patterns are a notation for pattern matching used to detect if a value has a certain shape, 
and then to bind variables to parts of the matched value. 

.Syntax

For most of the ((Values)), there is a corresponding pattern matching operator. Then there are
some "higher-order" matching operators which make complex patterns out of simpler ones. 
This is the complete list:
 
| Pattern              | Syntax                                                                       |
| --- | --- |
| Literal              | ((Values-Boolean)), ((Values-Integer)), ((Values-Real)), ((Values-Number)), ((Values-String)), ((Values-Location)), or ((Values-DateTime)) |
| Regular Expression   | `/<Regular Expression>/` |
| Variable declaration | `Type Var`                                                               |
| Multi-variable       | `*Var`, `*Type Var`                                                    |
| Variable             | `Var`                                                                      |
| List                 | `[ Pat~1~, Pat~2~, ..., Pat~n~ ]`                                         |
| Set                  | `{ Pat~1~, Pat~2~, ..., Pat~n~ }`                                         |
| Tuple                | `< Pat~1~, Pat~2~, ..., Pat~n~ >`                                         |
| Node                 | `Name ( Pat~1~, Pat~2~, ..., Pat~n~ )`                                  |
| Descendant           | `/ Pat`                                                                    |
| Labelled             | `Var : Pat`                                                               |
| TypedLabelled        | `Type Var : Pat`                                                       |
| TypeConstrained      |  `[Type] Pat` |
| Concrete             | (Symbol) ` Token~1~ Token~2~ ... Token~n~ `                                                          |


.Types

.Function

.Details

.Description

Patterns are used to *dispatch* functions and conditional control flow, to *extract* information 
from values and to conditionally *filter* values. The pattern following pattern kinds can be arbitrarily nested, following
the above syntax:

(((TOC)))

All these patterns may be used in:

*  cases of a ((Switch)) or [visit statements]((Statements-Visit)) or [visit expressions]((Expressions-Visit)), 
*  on the left of the ((Boolean-Match)) operator (`:=`),
*  on the left of the ((Enumerator)) operator (`<-`), and
*  as formal parameters of ((Declarations-Function))s. 
*  ((TryCatch)) statements to match thrown exceptions.

Each pattern binds variables in a conditional scope:

* in further patterns to the right of the name which is bound in the same pattern
* in the body of case statement (either a replacement or a statement body) 
* in the conditions and bodies of <If>, <For>, and <While> control flow statements
* in the yielding expressions of comprehensions and in furter conditions of the comprehensions

.Examples


.Benefits

.Pitfalls

* If a pattern does not match, then it may be hard to find out why. A small test case is the best thing to create. Often a default alternative
which <Throw>s an exception with the value which is not matched can be used to find out why this is happening.
* If a variable is bound in the scope of a pattern, then it acts as an `==` test, so make sure to use fresh variables
to avoid such accidental collisions. 

