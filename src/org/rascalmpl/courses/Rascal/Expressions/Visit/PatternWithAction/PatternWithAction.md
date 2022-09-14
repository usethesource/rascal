---
title: "Pattern With Action"
keywords: "=>,:"
---

.Synopsis
A pattern with an associated action that is executed on a successful match.

.Syntax

*  `Pattern => Exp`
*  `Pattern: Statement`

.Types

.Function

.Details

.Description
Patterns can be used in various contexts, but a common context is a PatternWithAction, 
which in its turn, may be used in various statements such ((Switch)) and ((Expressions-Visit)).

There are two variants as listed above:

*  When the subject matches _Pattern_, the expression _Exp_ is evaluated and the subject is replaced with the result.

*  When the subject matches Pat, the Statement is executed. More statements can be executed by including them in a ((Block)).


In ((Switch)) statements, only the form `Pattern : Statement` is allowed. 
When the subject matches _Pattern_, the _Statement_ is executed and the execution of the switch statement is complete. 
However, when a fail statement is executed in _Statement_  further alternatives of
_Pattern_ are tried. If no alternatives remain, PatternWithAction as a whole fails and subsequent cases of 
the switch statement are tried.

In ((Expressions-Visit)) expressions, the form `Pattern => Exp` describes subtree replacement: 
the current subtree of the subject of the visit expression is replaced by the value of _Exp_. 
The form `Pattern : Statement` is as described for switch statements, with the addition that execution of an 
((Statements-Insert)) statement will replace the current subtree. After both success or failure of the PatternWithAction, 
the traversal of the subject continues.

.Examples
Two examples of variant 1 (replacement):
```rascal
case red(CTree l, CTree r) => red(r,l)
case red(l, r) => green(l, r)
```
Three examples of variant 2 (Statement):
```rascal
case /Leila/: println("The topic is Starwars");
case red(_, _):    println("A red root node");
case red(_,_): c = c + 1; 
```
The action may also be a ((Block)):
```rascal
case red(_,_): { c = c + 1; println("c = <c>"); }
```

.Benefits

.Pitfalls

