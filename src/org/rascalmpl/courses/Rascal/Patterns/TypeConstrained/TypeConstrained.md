---
title: Type Constrained Pattern
keywords:
  - "["
  - "]"

---

#### Synopsis

Type constrained abstract pattern.

#### Syntax

```rascal
[Type] Pattern
```

#### Types

#### Function

#### Description

A type constrained pattern matches provided that the subject has type _Type_ and _Pattern_ matches. This can be handy in case of ambiguity (say more than one constructor with the same name), or in case the pattern is completely general. See an example below:

There are special cases in which the type constrained pattern calls a parser or validator for the given type:

* if the subject is of type `str` and the Type is a syntax non-terminal the string is parsed using the non-terminal
* if the subject is of type `loc` and the Type is a syntax non-terminal the contents of the resource that the subject location is referring to is parsed using the non-terminal


:::warning
Warning: This does not seem to work properly. There is a bug.
:::

#### Examples

```rascal-shell
import IO;
```
Some example data type which contains generic values as well as specific expressions:
```rascal-shell,continue
data Exp = val(value v) | add(Exp l, Exp r) | sub(Exp l, Exp r);
ex = add(add(val("hello"(1,2)),val("bye")), sub(val(1),val(2)));
```
Here we constrain the match to find only Exps:
```rascal-shell,continue
visit (ex) {
  case [Exp] str name(_,_) : println("node name is <name>");
}
```
Here we do not constrain the same pattern:
```rascal-shell,continue
visit (ex) {
  case str name(_,_) : println("node name is <name>");
}
```

#### Benefits

#### Pitfalls

