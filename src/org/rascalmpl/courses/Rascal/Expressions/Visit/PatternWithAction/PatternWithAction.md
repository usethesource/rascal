# Pattern With Action

.Synopsis
A pattern with an associated action that is executed on a successful match.

.Index
=> :

.Syntax

*  `_Pattern_ => _Exp_`
*  `_Pattern_: _Statement_`

.Types

.Function

.Details

.Description
Patterns can be used in various contexts, but a common context is a PatternWithAction, 
which in its turn, may be used in various statements such ((Switch)) and ((Visit)).

There are two variants as listed above:

*  When the subject matches _Pattern_, the expression _Exp_ is evaluated and the subject is replaced with the result.

*  When the subject matches Pat, the Statement is executed. More statements can be executed by including them in a ((Block)).


In ((Switch)) statements, only the form `_Pattern_ : _Statement_` is allowed. 
When the subject matches _Pattern_, the _Statement_ is executed and the execution of the switch statement is complete. 
However, when a fail statement is executed in _Statement_  further alternatives of
_Pattern_ are tried. If no alternatives remain, PatternWithAction as a whole fails and subsequent cases of 
the switch statement are tried.

In ((Visit)) expressions, the form `_Pattern_ => _Exp_` describes subtree replacement: 
the current subtree of the subject of the visit expression is replaced by the value of _Exp_. 
The form `_Pattern_ : _Statement_` is as described for switch statements, with the addition that execution of an 
((Statements-Insert)) statement will replace the current subtree. After both success or failure of the PatternWithAction, 
the traversal of the subject continues.

.Examples
Two examples of variant 1 (replacement):
[source,rascal]
----
case red(CTree l, CTree r) => red(r,l)
case red(l, r) => green(l, r)
----
Three examples of variant 2 (Statement):
[source,rascal]
----
case /Leila/: println("The topic is Starwars");
case red(_, _):    println("A red root node");
case red(_,_): c = c + 1; 
----
The action may also be a ((Block)):
[source,rascal]
----
case red(_,_): { c = c + 1; println("c = <c>"); }
----

.Benefits

.Pitfalls

