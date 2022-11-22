---
title: Constructor
---

#### Synopsis

Constructors create values for user-defined datatypes (Algebraic Datatypes).

#### Syntax

```rascal
Name ( Exp~1~, Exp~2~, ... )
```

#### Types

//

| `Exp~1~` | `Exp~2~` | ... |  `Name ( Exp~1~, Exp~2~, ... )`  |
| --- | --- | --- | --- |
| `T~1~`   | `T~2~`   | ... | Depends on ADT declaration           |


#### Function

#### Description

Constructors are created using the ((Expressions-Call)) notation.

In ordinary programming languages record types or classes exist to introduce a new type name for a collection of related, 
named, values and to provide access to the elements of such a collection through their name. 

In Rascal, ((Declarations-AlgebraicDataType))s provide this facility. They have to be declared, see ((Algebraic Data Type)), and
then values can be created using calls to the declared constructor functions.
The constructor _Name_ should correspond (regarding name, arity and argument types) to one of the alternatives
in the ADT declaration.

First, the actual parameter expressions _Exp_~i~ are evaluated resulting in values _V_~i~.
Next, a data value is constructed in accordance with the declared data type
using the values _V_~i~ as arguments for the constructor. This data value is used as value of the constructor. 
Constructors are functions that can be used in all contexts where functions can be used.

Observe that the syntax of a constructor is identical to the syntax of an function ((Call)).

Also, all instances of ((Values-Constructor))s are instances of ((Values-Node)) values. This means that the generic
operations on ((Values-Node)) also work on ((Values-Constructor)). If the ((AlgebraicDataType)) provides more precies types than `value` for fields or keyword fields of a constructor, then the ((Values-Node)) operations are checked using these more precise types, both statically and at run-time.

#### Examples

First, define a datatype `WF` for word frequencies:
```rascal-shell,continue
data WF = wf(str word, int freq);
```
Then construct a new `WF` value by calling the constructor `wf` with appropriate arguments:
```rascal-shell,continue
wf("Rascal", 10000);
```

Constructors with keyword parameters are also interesting:
```rascal-shell
data Shape
    = rectangle(int width, int height, int area=width * height);
rectangle(100,200).area
x = rectangle(100,200, area=100000);
x.area
y = rectangle(100,200);
y.area
```

#### Benefits

#### Pitfalls

