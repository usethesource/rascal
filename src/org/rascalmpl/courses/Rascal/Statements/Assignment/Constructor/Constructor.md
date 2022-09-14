---
title: "Constructor"
keywords: "="
---

#### Synopsis

Assign to constructor.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

First the value _Exp_ is determined and should be a data value of the form `_Name_(_V_~1~, _V_~2~, ..., _V_~n~). 
Next the assignments `Assignable~i~ = V~i~` are performed for 1 \<= i \<= n.

#### Examples

```rascal-shell
data FREQ = wf(str word, int freq);
W = wf("rascal", 1000);
wf(S, I) = W;
S;
I;
```

#### Benefits

#### Pitfalls

