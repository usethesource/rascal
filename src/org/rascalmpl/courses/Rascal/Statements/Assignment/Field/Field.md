---
title: "Field"
keywords: "."
---

#### Synopsis

Assign to a field of a tuple, relation or datatype.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

The value `V` of _Assignable_ is determined and should be of a type that has a field _Name_.
The value of that field is replaced in _V_ by the value of _Exp_ resulting in a new value _V_' that is assigned to _Assignable_.

#### Examples

```rascal-shell
data FREQ = wf(str word, int freq);
W = wf("rascal", 1000);
W.freq = 100000;
```

#### Benefits

#### Pitfalls

