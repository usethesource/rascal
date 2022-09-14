---
title: UnsupportedSubscript
---

.Synopsis
A subscript is applied to a value that does not support it.

.Syntax

.Types

.Function
       
.Usage

.Description
Subscription is available for values of various types including: 
[string]((Rascal:String-Subscription)), 
[node]((Rascal:Node-Subscription)),
[list]((Rascal:List-Subscription)), 
[map]((Rascal:Map-Subscription)), 
[tuple]((Rascal:Tuple-Subscription)) and 
[relation]((Rascal:Relation-Subscription)).
This error is generated when subscription is applied to a value for which it is not defined.

Remedies:

*  Use another operation than subscription to extract the values you want.
*  Use another type (that does support subscription) to represent your data.

.Examples
Here are some correct uses of subscription:
```rascal-shell
"abc"[1];
[1,2,3][1];
"f"(1,2,3)[1];
("a":1, "b":2, "c":3)["b"]
```
Here are some erroneous examples:
```rascal-shell,error
true[1];
123[1];
{1,2,3}[1];
```

.Benefits

.Pitfalls

