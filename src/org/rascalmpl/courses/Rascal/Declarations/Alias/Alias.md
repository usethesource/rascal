---
title: Alias Declaration
keywords:
  - alias
  - type
---

#### Synopsis

Declare an alias for a type.

#### Syntax

* `alias Name  = Type;`
* `alias Name[&T1, ...] = Type;`

#### Types

#### Function

#### Description

Everything can be expressed using the elementary types and values that are provided by Rascal. 
However, for the purpose of documentation and readability it is sometimes better to use a descriptive name as type indication, rather than an elementary type.  The use of aliases is a good way to document your intentions. 

An alias declaration states that _Name_ can be used everywhere instead of the already defined type _Type_. 
Both types are thus considered to be type equivalent, and one could always be substituted for the other. 

Often an alias defines a type with labeled fields (see below) such that accessing a complex relation becomes
easier. 

Aliases can be type-parametrized as well more generic applications (see below for an example).

#### Examples

Introduce two aliases `ModuleId` and `Frequency` for the type str.
```rascal-shell
alias ModuleId = str;
alias Frequency = int;
```

Another example is an alias definition for a graph containing edges between integer nodes:
```rascal-shell,continue
alias IntGraph = rel[int from, int to];
```

Note that the Rascal Standard Library provides a graph data type that is defined as follows:
```rascal-shell,continue
alias Graph[&T] = rel[&T from, &T to];
```

In other words the standard graph datatype can be parameterized with any element type.

Alias types are type _equivalences_: in every situation the alias is substitutable for its definition _and vice versa_.
Here is an example to show type equivalence:
```rascal-shell,continue
Graph[int,int] myGraph = {<1,2>,<2,3>};
rel[int,int] myRel = {<4,5>,<5,6>,<6,4>};
myGraph = myRel;
myRel = myGraph;
```

When the static type is know and it is an alias, we can use the field names in its definition for projection:
```rascal-shell,continue
nodes = myGraph<from> + myGraph<to>;
```

See ((Type Parameters)) for other examples of parameterized alias declarations.

#### Benefits

#### Pitfalls

