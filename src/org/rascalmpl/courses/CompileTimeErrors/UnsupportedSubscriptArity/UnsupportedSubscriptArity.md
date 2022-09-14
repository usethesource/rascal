---
title: UnsupportedSubscriptArity
---

.Synopsis
Wrong number of subscripts is used. 

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
This error is generated when subscription is applied to a value that does support subscription but not the number
of indices that are used.

Remedies: correct the number of indices used in the subscription.

.Examples
```rascal-shell,error
[1,2,3][2,1];
("a":1, "b":2, "c":3)["c", "d"];
<1, 2, 3>[5,6];
```

.Benefits

.Pitfalls

