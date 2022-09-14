---
title: PartiallyLabeledFields
---

.Synopsis
In a tuple or relation all fields should have names or none at all.

.Syntax

.Types

.Function
       
.Usage

.Description
The fields in tuples and relations can optionally be labelled with a name.
There are only two legal situations:

*  All fields have a label.
*  No field has a label.


This error signals the case that thei fields are partially labelled.


.Examples
```rascal-shell,error
tuple[int n, str] T;
rel[str name, int] R;
```
.Benefits

.Pitfalls

