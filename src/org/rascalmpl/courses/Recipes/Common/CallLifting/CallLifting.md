---
title: Call Lifting
---

#### Synopsis

Lift procedure calls to component calls.

#### Syntax

#### Types

#### Function

#### Description

A frequently occurring problem is that we know the call relation of a system but that we want to understand it at the component level rather than at the procedure level. If it is known to which component each procedure belongs, it is possible to lift the call relation to the component level. Actual lifting amounts to translating each call between procedures by a call between components. 

#### Examples

Consider the following figure:


![]((parts.png))


(a) Shows the calls between procedures;
(b) shows how procedures are part of a system component.
(c) shows how the call relation given in (a) can be lifted to the component level.

The situation can be characterized by:

*  A call relation between procedures
*  A partOf relation between procedures and components


The problem is now to lift the call relation using the information in the partOf relation.
In other words: a call between two procedures will be lifted to
a call between the components to which each procedure belongs.

Here is a solution:
```rascal
include::{LibDir}demo/common/Lift.rsc[tags=module]
```

And we can use it as follows:

```rascal-shell
import demo::common::Lift;
```
Encode the call relation and partOf relation:
```rascal-shell,continue
calls = {<"main", "a">, <"main", "b">, <"a", "b">, <"a", "c">, <"a", "d">, <"b", "d">};        
partOf = {<"main", "Appl">, <"a", "Appl">, <"b", "DB">, <"c", "Lib">, <"d", "Lib">};
```
and do the lifting:
```rascal-shell,continue
lift(calls, partOf);
```
Please verify that this corresponds exactly to (c) in the figure above.
```rascal-shell,continue
```

#### Benefits

#### Pitfalls

