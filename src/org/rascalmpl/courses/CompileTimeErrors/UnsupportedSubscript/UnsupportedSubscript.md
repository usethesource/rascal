# UnsupportedSubscript

.Synopsis
A subscript is applied to a value that does not support it.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
Subscription is available for values of various types including: 
link:/Rascal#String-Subscription[string], 
link:/Rascal#Node-Subscription[node],
link:/Rascal#List-Subscription[list], 
link:/Rascal#Map-Subscription[map], 
link:/Rascal#Tuple-Subscription[tuple] and 
link:/Rascal#Relation-Subscription[relation].
This error is generated when subscription is applied to a value for which it is not defined.

Remedies:

*  Use another operation than subscription to extract the values you want.
*  Use another type (that does support subscription) to represent your data.

.Examples
Here are some correct uses of subscription:
[source,rascal-shell]
----
"abc"[1];
[1,2,3][1];
"f"(1,2,3)[1];
("a":1, "b":2, "c":3)["b"]
----
Here are some erroneous examples:
[source,rascal-shell,error]
----
true[1];
123[1];
{1,2,3}[1];
----

.Benefits

.Pitfalls

